using ead_backend.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
using BCrypt.Net;

namespace ead_backend.Controllers
{
    [Route("api/admin")]
    [ApiController]
    public class AdminController : ControllerBase
    {
        private readonly IMongoCollection<Admin> _adminCollection;
        private readonly ILogger<AdminController> _logger;

        public AdminController(ILogger<AdminController> logger, IOptions<Database> database)
        {
            _logger = logger;
            var mongoClient = new MongoClient(database.Value.ConnectionString);
            var mongoDatabase = mongoClient.GetDatabase(database.Value.DatabaseName);
            _adminCollection = mongoDatabase.GetCollection<Admin>("Admin");
        }

        [Route("create-admin")]
        [HttpPost]
        public async Task<IActionResult> CreateAdmin(Admin admin)
        {
            var password = admin.Password;
            admin.Password = BCrypt.Net.BCrypt.HashPassword(password);

            await _adminCollection.InsertOneAsync(admin);
            return CreatedAtAction(nameof(CreateAdmin), new { id = admin.Id }, admin);
        }

        [Route("get-all-admins")]
        [HttpGet]
        public async Task<List<Admin>> GetAllAdmins()
        {
            return await _adminCollection.Find(_ => true).ToListAsync();

        }

        [Route("get-admin/{id?}")]
        [HttpGet]
        public async Task<Admin?> GetAdminById(string id)
        {
            return await _adminCollection.Find(x => x.Id == id).FirstOrDefaultAsync();

        }

        [Route("update-admin/{id?}")]
        [HttpPut]
        public async Task<IActionResult> UpdateAdmin(string id, Admin updateAdmin)
        {
            var admin = await GetAdminById(id);
            if (admin == null)
            {
                return NotFound();
            }

            updateAdmin.Id = admin.Id;
            updateAdmin.Password = admin.Password;
            await _adminCollection.ReplaceOneAsync(x => x.Id == id, updateAdmin);

            return Ok();

        }

        [Route("delete-admin/{id?}")]
        [HttpDelete]
        public async Task<IActionResult> DeleteAdmin(string id)
        {
            var admin = await GetAdminById(id);
            if (admin == null)
            {
                return NotFound();
            }

            await _adminCollection.DeleteOneAsync(x => x.Id == id);

            return Ok();

        }
    }
}
