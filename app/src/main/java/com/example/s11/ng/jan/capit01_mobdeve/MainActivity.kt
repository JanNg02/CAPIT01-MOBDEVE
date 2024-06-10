package com.example.s11.ng.jan.capit01_mobdeve

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(){
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rvHome_RT)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val database = Database()
        val dataRepository = DataRepository(database)
        val data = dataRepository.getData()
        adapter = MyAdapter(data)
        recyclerView.adapter = adapter
    }
}