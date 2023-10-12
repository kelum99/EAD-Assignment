using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;
using System;

namespace ead_backend.Models
{
    //[BsonIgnoreIfDefault]
    public enum TrainStatus
    {
        Inactive,
        Active,
        Published
    }

    public class Train
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]

        public string? Id { get; set; }

        [BsonElement("Name")]

        public string? Name { get; set; }

        public string? Schedule { get; set; }

        //[BsonElement("Time")]
        public string? Time { get; set; }

        public string? Route { get; set; }

        [BsonIgnoreIfDefault]
        public List<String>? Stations { get; set; }

        [BsonElement("Date")]
        [BsonDateTimeOptions(DateOnly = true)]
        public DateTime Date { get; set; }

        public TrainStatus? Status { get; set; }




    }
}
