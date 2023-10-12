/********************************************************************************
 * Filename: Train.cs
 * Type: C# Source Code
 * Size: 1456 bytes
 * Author: Weerasekara B.J.D.A.
 * Created: 2023-10-09
 * Last Modified: 2023-10-12
 * Description: This C# file contains the Train class, which provides
 *              the model for a train shedule with all the attributes
 * Institue: Sri Lanka Institute of Information Technology,Malabe.
 ********************************************************************************/

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
