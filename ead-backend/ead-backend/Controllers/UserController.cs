/********************************************************************************
 * Filename: UserController.cs
 * Type: C# Source Code
 * Size: 5319 bytes
 * Author: Rodrigo P.H.M.S
 * Created: 2023-10-09
 * Last Modified: 2023-10-12
 * Description: This C# file contains the UserController class, which provides
 *              the methods for the crud operation of User Management 
 * Institue: Sri Lanka Institute of Information Technology,Malabe.
 ********************************************************************************/

using ead_backend.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using BCrypt.Net;

namespace ead_backend.Controllers
{
    [Route("api/user")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly IMongoCollection<User> _userCollection;
        private readonly ILogger<UserController> _logger;

        //UpdateStatus public class
        public class UpdatedStatus
        {
            public string? Status { get; set; }
        }

        //UserController constructor
        public UserController(ILogger<UserController> logger, IOptions<Database> database)
        {
            //Initialize the MongoDB collection and logger 
            _logger = logger;
            var mongoClient = new MongoClient(database.Value.ConnectionString);
            var mongoDatabase = mongoClient.GetDatabase(database.Value.DatabaseName);
            _userCollection = mongoDatabase.GetCollection<User>("User");
        }

        //Create User function
        [Route("create-user")]
        [HttpPost]
        public async Task<IActionResult> CreateUser(User user)
        {
            var password = user.Password;
            user.Password = BCrypt.Net.BCrypt.HashPassword(password);
            user.Status = "Active";
            await _userCollection.InsertOneAsync(user);
            return CreatedAtAction(nameof(CreateUser), new { id = user.Id }, user);

        }

        //Get all user function
        [Route("get-all-users")]
        [HttpGet]
        public async Task<List<User>> GetAllUsers()
        {
            return await _userCollection.Find(_ => true).ToListAsync();

        }

        //Fuction to get a user by user id
        [Route("get-user/{id?}")]
        [HttpGet]
        public async Task<User?> GetUserById(string id)
        {
            return await _userCollection.Find(x => x.Id == id).FirstOrDefaultAsync();

        }

        //Fuction to update a user by user id
        [Route("update-user/{id?}")]
        [HttpPut]
        public async Task<IActionResult> UpdateUser(string id, User updatedUser)
        {
            var user = await GetUserById(id);
            if(user == null) {
                return NotFound();
            }

            updatedUser.Id = user.Id;
            updatedUser.Status = user.Status;
            updatedUser.Password = user.Password;
            await _userCollection.ReplaceOneAsync(x => x.Id == id, updatedUser);

            return Ok();

        }

        //Fuction to update a user status by user id (Activation and Deactivation)
        [Route("update-user-status/{id}")]
        [HttpPut]
        public async Task<IActionResult> UpdateUserStatus(string id, UpdatedStatus updatedStatus)
        {
            var user = await GetUserById(id);
            if (user == null)
            {
                return NotFound();
            }

            user.Status = updatedStatus.Status;
            await _userCollection.ReplaceOneAsync(x => x.Id == id, user);

            return Ok();

        }

        //Fuction to delete a user by user id
        [Route("delete-user/{id?}")]
        [HttpDelete]
        public async Task<IActionResult> DeleteUser(string id)
        {
            var user = await GetUserById(id);
            if (user == null)
            {
                return NotFound();
            }

            await _userCollection.DeleteOneAsync(x => x.Id == id);

            return Ok();

        }
    }
}
