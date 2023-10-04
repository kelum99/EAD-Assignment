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
    }
}
