/********************************************************************************
 * Filename: User.cs
 * Type: C# Source Code
 * Size: 5319 bytes
 * Author: Rodrigo P.H.M.S
 * Created: 2023-10-09
 * Last Modified: 2023-10-12
 * Description: This C# file contains the User class, which provides
 *              the model for User MongoDB Collection
 * Institue: Sri Lanka Institute of Information Technology,Malabe.
 ********************************************************************************/

using MongoDB.Bson;
using MongoDB.Bson.Serialization.Attributes;

namespace ead_backend.Models
{
    public class User
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        public string? NIC { get; set; }
        public string? Name { get; set; }
        public string? Email { get; set; }
        public string? Password { get; set; }
        public string? Status { get; set; }
        public string? Mobile { get; set; }

    }
}
