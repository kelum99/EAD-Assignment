/********************************************************************************
 * Filename: Admin.cs
 * Type: C# Source Code
 * Size: 5319 bytes
 * Author: Ayuwardhana H.M.K.J.J
 * Created: 2023-10-09
 * Last Modified: 2023-10-12
 * Description: This C# file contains the Admin class, which provides
 *              the model for Admin MongoDB Collection
 * Institue: Sri Lanka Institute of Information Technology,Malabe.
 ********************************************************************************/

using MongoDB.Bson.Serialization.Attributes;
using MongoDB.Bson;

namespace ead_backend.Models
{
    public class Admin
    {
        [BsonId]
        [BsonRepresentation(BsonType.ObjectId)]
        public string? Id { get; set; }

        public string? Name { get; set; }
        public string? Email { get; set; }
        public string? Password { get; set; }
        public string? Role { get; set; }
    }
}
