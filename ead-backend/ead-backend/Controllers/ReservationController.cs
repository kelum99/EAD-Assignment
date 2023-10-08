using ead_backend.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
namespace ead_backend.Controllers
{
    [Route("api/reservation")]
    [ApiController]
    public class ReservationController : ControllerBase
    {
        private readonly IMongoCollection<Reservation> _reservationCollection;
        private readonly ILogger<ReservationController> _logger;

        public ReservationController(ILogger<ReservationController> logger, IOptions<Database> database)
        {
            _logger = logger;
            var mongoClient = new MongoClient(database.Value.ConnectionString);
            var mongoDatabase = mongoClient.GetDatabase(database.Value.DatabaseName);
            _reservationCollection = mongoDatabase.GetCollection<Reservation>("Reservation");
        }

        [Route("add-reservation")]
        [HttpPost]
        public async Task<IActionResult> CreateReservation(Reservation reservation)
        {
            var daysDifference = (reservation.ReservationDate - reservation.BookingDate).TotalDays;

            if (daysDifference >= 0 && daysDifference <= 30)
            {
                var userReservations = await GetReservationsByUser(reservation.UserID);

                if (userReservations.Count >= 4)
                {
                    ModelState.AddModelError("", "Traveller can have a maximum of 4 reservations.");
                    var errorMessage = ModelState[""].Errors.FirstOrDefault()?.ErrorMessage;
                    return BadRequest(errorMessage);
                }
                await _reservationCollection.InsertOneAsync(reservation);
                return CreatedAtAction(nameof(CreateReservation), new { id = reservation.Id }, reservation);
            }
            else
            {
                ModelState.AddModelError("", "ReservationDate must be within 30 days of BookingDate.");
                var errorMessage = ModelState[""].Errors.FirstOrDefault()?.ErrorMessage;
                return BadRequest(errorMessage); //
            }
        }

        [Route("get-all-reservations")]
        [HttpGet]
        public async Task<List<Reservation>> GetAllReservations()
        {
            return await _reservationCollection.Find(_ => true).ToListAsync();
        }

        [Route("get-reservations-byUser/{userid?}")]
        [HttpGet]
        public async Task<List<Reservation?>> GetReservationsByUser(string userid)
        {
            return await _reservationCollection.Find(x => x.UserID == userid).ToListAsync();

        }

        [Route("get-reservation/{id?}")]
        [HttpGet]
        public async Task<Reservation?> GetReservationById(string id)
        {
            return await _reservationCollection.Find(x => x.Id == id).FirstOrDefaultAsync();

        }

        [Route("update-reservation/{id?}")]
        [HttpPut]
        public async Task<IActionResult> UpdateReservation(string id, Reservation updatedReservation)
        {
            var reservation = await GetReservationById(id);
            if (reservation == null)
            {
                return NotFound();
            }

            var daysDifference = (reservation.ReservationDate - DateTime.Now).TotalDays;

            if (daysDifference < 5)
            {
                ModelState.AddModelError("", "Reservation Date must be at least 5 days from the current date for updates.");
                var errorMessage = ModelState[""].Errors.FirstOrDefault()?.ErrorMessage;
                return BadRequest(errorMessage);
            }

            updatedReservation.Id = reservation.Id;
            await _reservationCollection.ReplaceOneAsync(x => x.Id == id, updatedReservation);

            return NoContent();

        }

        [Route("cancel-Reservation/{id?}")]
        [HttpDelete]
        public async Task<IActionResult> CancelReservation(string id)
        {
            var reservation = await GetReservationById(id);
            if (reservation == null)
            {
                return NotFound();
            }

            var daysDifference = (reservation.ReservationDate - DateTime.Now).TotalDays;

            if (daysDifference < 5)
            {
                ModelState.AddModelError("", "Reservation Date must be at least 5 days from the current date for cancellations.");
                var errorMessage = ModelState[""].Errors.FirstOrDefault()?.ErrorMessage;
                return BadRequest(errorMessage);
            }

            await _reservationCollection.DeleteOneAsync(x => x.Id == id);

            return NoContent();

        }
    }
}
