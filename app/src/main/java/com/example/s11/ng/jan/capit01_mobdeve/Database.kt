package com.example.s11.ng.jan.capit01_mobdeve

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.bson.Document
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoDatabase

class Database {
    private val mongoClient: MongoClient

    init {
        val connectionString = "mongodb+srv://Root:DLSU1234@bdrss.absaalg.mongodb.net/?retryWrites=true&w=majority&appName=BDRSS";
        mongoClient = MongoClients.create(connectionString)
    }

     fun getCollection(): MongoCollection<Document> {
        val database = mongoClient.getDatabase("BDRSS")
        return database.getCollection("announcement")
    }
}