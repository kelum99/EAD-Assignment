using ead_backend.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using MongoDB.Driver;

namespace ead_backend.Controllers
{
    [Route("api/user")]
    [ApiController]
    public class UserController : ControllerBase
    {
        private readonly IMongoCollection<User> _userCollection;
        private readonly ILogger<UserController> _logger;

        public UserController(ILogger<UserController> logger, IOptions<Database> database)
        {
            _logger = logger;
            var mongoClient = new MongoClient(database.Value.ConnectionString);
            var mongoDatabase = mongoClient.GetDatabase(database.Value.DatabaseName);
            _userCollection = mongoDatabase.GetCollection<User>("User");
        }

        [Route("add-user")]
        [HttpPost]
        public async Task<IActionResult> CreateUser(User user)
        {
            await _userCollection.InsertOneAsync(user);
            return CreatedAtAction(nameof(CreateUser), new { id = user.Id }, user);

        }

        [Route("get-all-users")]
        [HttpGet]
        public async Task<List<User>> GetAllUsers()
        {
            return await _userCollection.Find(_ => true).ToListAsync();

        }

        [Route("get-user/{id?}")]
        [HttpGet]
        public async Task<User?> GetUserById(string id)
        {
            return await _userCollection.Find(x => x.Id == id).FirstOrDefaultAsync();

        }

        [Route("update-user/{id?}")]
        [HttpPut]
        public async Task<IActionResult> UpdateUser(string id, User updatedUser)
        {
            var user = await GetUserById(id);
            if(user == null) {
                return NotFound();
            }

            updatedUser.Id = user.Id;
            await _userCollection.ReplaceOneAsync(x => x.Id == id, updatedUser);

            return NoContent();

        }

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

            return NoContent();

        }
    }
}
