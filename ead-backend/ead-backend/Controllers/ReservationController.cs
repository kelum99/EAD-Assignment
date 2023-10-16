/********************************************************************************
 * Filename: ReservationController.cs
 * Type: C# Source Code
 * Size: 5319 bytes
 * Author: Roshani O.V.D.E.
 * Created: 2023-10-09
 * Last Modified: 2023-10-12
 * Description: This C# file contains the ReservationController class, which provides
 *              the methods for the crud operation of Reservations 
 * Institue: Sri Lanka Institute of Information Technology,Malabe.
 ********************************************************************************/

using ead_backend.Models;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Options;
using MongoDB.Driver;
namespace ead_backend.Controllers
{
    [Route("api/reservation")]
    [ApiController]

    //ReservationController Constrcutor
    public class ReservationController : ControllerBase
    {
        private readonly IMongoCollection<Reservation> _reservationCollection;
        private readonly ILogger<ReservationController> _logger;

        public ReservationController(ILogger<ReservationController> logger, IOptions<Database> database)
        {
            // Initialize the MongoDB collection for Reservations and loggers
            _logger = logger;
            var mongoClient = new MongoClient(database.Value.ConnectionString);
            var mongoDatabase = mongoClient.GetDatabase(database.Value.DatabaseName);
            _reservationCollection = mongoDatabase.GetCollection<Reservation>("Reservation");
        }

        //Method to create a new reservation
        [Route("add-reservation")]
        [HttpPost]
        public async Task<IActionResult> CreateReservation(Reservation reservation)
        {
            //Check eligibility for creation
            var daysDifference = (reservation.ReservationDate - reservation.BookingDate).TotalDays;

            if (daysDifference >= 0 && daysDifference <= 30)
            {
                var userReservations = await GetReservationsByUser(reservation.UserNIC);

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
                return BadRequest(errorMessage); 
            }
        }

        //Method to fetch all the reservations
        [Route("get-all-reservations")]
        [HttpGet]
        public async Task<List<Reservation>> GetAllReservations()
        {
            return await _reservationCollection.Find(_ => true).ToListAsync();
        }

        //Method to fetch all reservations for a particular user
        [Route("get-reservations-byUser/{userid?}")]
        [HttpGet]
        public async Task<List<Reservation?>> GetReservationsByUser(string userid)
        {
            return await _reservationCollection.Find(x => x.UserNIC == userid).ToListAsync();

        }

        //Method to fetch a specific reservation
        [Route("get-reservation/{id?}")]
        [HttpGet]
        public async Task<Reservation?> GetReservationById(string id)
        {
            return await _reservationCollection.Find(x => x.Id == id).FirstOrDefaultAsync();

        }

        //Method to update a reservation
        [Route("update-reservation/{id?}")]
        [HttpPut]
        public async Task<IActionResult> UpdateReservation(string id, Reservation updatedReservation)
        {
            var reservation = await GetReservationById(id);
            if (reservation == null)
            {
                return NotFound();
            }

            //Check eligibility for update (at least 5 days from current date)
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

        //Method for cancelling a reservation
        [Route("cancel-reservation/{id?}")]
        [HttpDelete]
        public async Task<IActionResult> CancelReservation(string id)
        {
            // Retrieve the existing Reservation by its ID
            var reservation = await GetReservationById(id);
            if (reservation == null)
            {
                return NotFound();
            }

            //Check eligibility for delete (at least 5 days from current date)
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
