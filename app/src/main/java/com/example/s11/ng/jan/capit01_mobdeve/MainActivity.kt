package com.example.s11.ng.jan.capit01_mobdeve

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), OnDataFetchedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val news = fetchNews(this)
        news.execute()
    }

    override fun onDataFetched(data: List<newsData>) {

        val listView: ListView = findViewById(R.id.listViewTest)
        val newsDetails = data.map { "${it.announcementTitle}\n Expiry Date: ${it.announcementContent}"}.toTypedArray()
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, newsDetails)
        listView.adapter = adapter
    }
}