﻿using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace ead_backend.Models
{
    public class Reservation
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        public string? UserID { get; set; }

        public DateTime BookingDate { get; set; }
        public DateTime ReservationDate { get; set; }
        public int NoOfTickets { get; set; }

        public string? Train { get; set; }

        public string? Time { get; set; }

        public string? UserType { get; set; }
    }
}
