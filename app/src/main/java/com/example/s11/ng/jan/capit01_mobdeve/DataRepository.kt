//package com.example.s11.ng.jan.capit01_mobdeve
//
//import org.bson.Document
//
//class DataRepository(private val database: Database) {
//    fun getData(): List<Document> {
//        val collection = database.getCollection()
//        val cursor = collection.find()
//        val data = mutableListOf<Document>()
//        cursor.forEach { data.add(it) }
//        return data
//    }
//}