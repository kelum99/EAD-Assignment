/********************************************************************************
 * Filename: AuthController.cs
 * Type: C# Source Code
 * Size: 5319 bytes
 * Author: Ayuwardhana H.M.K.J.J
 * Created: 2023-10-09
 * Last Modified: 2023-10-12
 * Description: This C# file contains the AuthController class, which provides
 *              the methods for User and Admin Authentication. 
 * Institue: Sri Lanka Institute of Information Technology,Malabe.
 ********************************************************************************/

using ead_backend.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using Microsoft.IdentityModel.Tokens;
using System.IdentityModel.Tokens.Jwt;
using System.Security.Claims;
using System.Text;

namespace ead_backend.Controllers
{
    [Route("api/auth")]
    [ApiController]
    public class AuthController : ControllerBase
    {
        private readonly IMongoCollection<Admin> _adminCollection;
        private readonly IMongoCollection<User> _userCollection;
        private IConfiguration _config;

        //Public class to get and set Admin login data
        public class AdminLoginData
        {
            public string? Email { get; set; }
            public string? Password { get; set; }
        }

        //Public class to get and set User login data
        public class UserLoginData
        {
            public string? Nic { get; set; }
            public string? Password { get; set; }
        }

        //AuthController constructor
        public AuthController(IOptions<Database> database, IConfiguration config)
        {
            //Initialize the MongoDB collection and Configuration
            var mongoClient = new MongoClient(database.Value.ConnectionString);
            var mongoDatabase = mongoClient.GetDatabase(database.Value.DatabaseName);
            _userCollection = mongoDatabase.GetCollection<User>("User");
            _adminCollection = mongoDatabase.GetCollection<Admin>("Admin");

            _config = config;
        }

        //Fuction to generate json web token for admin (return jwt token)
        private string GenerateAdminToken(Admin admin)
        {
            var securityKey = new SymmetricSecurityKey(Encoding.UTF8.GetBytes(_config["Jwt:Key"]));
            var credentials = new SigningCredentials(securityKey, SecurityAlgorithms.HmacSha256);
            var claims = new[]
            {
                new Claim("Name", admin.Name!),
                new Claim("Email", admin.Email!),
                new Claim("Role", admin.Role!),
                new Claim("Id", admin.Id!)
            };
            var token = new JwtSecurityToken(_config["Jwt:Issuer"],
                _config["Jwt:Audience"],
                claims,
                expires: DateTime.Now.AddMinutes(120),
                signingCredentials: credentials);


            return new JwtSecurityTokenHandler().WriteToken(token);

        }

        //Function for admin login
        [Route("admin-login")]
        [HttpPost]
        public async Task<IActionResult> AdminLogin(AdminLoginData loginData)
        {
            var admin = await _adminCollection.Find(x => x.Email == loginData.Email).FirstOrDefaultAsync();
            if (admin == null)
            {
                return NotFound();
            }

            if (!BCrypt.Net.BCrypt.Verify(loginData.Password, admin.Password))
            {
                return Unauthorized();
            }

            var adminToken = GenerateAdminToken(admin);
            return Ok(new { token = adminToken, role = admin.Role });
        }

        //Function for user login
        [Route("user-login")]
        [HttpPost]
        public async Task<IActionResult> UserLogin(UserLoginData userLoginData)
        {
            var user = await _userCollection.Find(x => x.NIC == userLoginData.Nic).FirstOrDefaultAsync();
            if (user == null)
            {
                return NotFound();
            }

            if (!BCrypt.Net.BCrypt.Verify(userLoginData.Password, user.Password))
            {
                return Unauthorized();
            }

            return Ok(user);

        }
    }
}
