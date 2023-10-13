/********************************************************************************
 * Filename: Database.cs
 * Type: C# Source Code
 * Size: 5319 bytes
 * Author: Ayuwardhana H.M.K.J.J
 * Created: 2023-10-09
 * Last Modified: 2023-10-12
 * Description: This C# file contains the Database class, which provides
 *              the model for the MongoDB connection.
 * Institue: Sri Lanka Institute of Information Technology,Malabe.
 ********************************************************************************/

namespace ead_backend.Models
{
    public class Database
    {
        public string ConnectionString { get; set; } = null!;

        public string DatabaseName { get; set; } = null!;

        public string CollectionName { get; set; } = null!;
    }
}
