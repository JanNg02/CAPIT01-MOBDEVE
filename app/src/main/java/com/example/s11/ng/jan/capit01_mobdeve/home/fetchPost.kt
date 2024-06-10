//package com.example.s11.ng.jan.capit01_mobdeve.home
//
//import androidx.recyclerview.widget.RecyclerView
//import com.example.s11.ng.jan.capit01_mobdeve.adapter.modelPost
//import com.mongodb.client.model.Filters.eq
//import com.mongodb.kotlin.client.coroutine.MongoClient
//import com.mongodb.kotlin.client.coroutine.MongoDatabase
//import io.github.cdimascio.dotenv.dotenv
//import kotlinx.coroutines.flow.firstOrNull
//import kotlinx.coroutines.runBlocking
//
//interface OnDataFetchedListener {
//    fun onDataFetched(data: List<modelPost>)
//}
//class fetchPost {
//
//    private lateinit var recyclerView: RecyclerView
//    val url = "mongodb+srv://Root:DLSU123@bdrss.absaalg.mongodb.net/?retryWrites=true&w=majority&appName=BDRSS"
//    val client = MongoClient.create(url)
//    val databaseName = "post"
//    val db: MongoDatabase = client.getDatabase(databaseName = databaseName)
//}