//package com.example.s11.ng.jan.capit01_mobdeve
//
//import android.os.Bundle
//import android.widget.ArrayAdapter
//import android.widget.ListView
//import androidx.appcompat.app.AppCompatActivity
//import androidx.recyclerview.widget.RecyclerView
//import com.example.s11.ng.jan.capit01_mobdeve.adapter.modelPost
//
//class MainActivity : AppCompatActivity(), OnDataFetchedListener{
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var adapter: MyAdapter
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.listviewtest)
//
////        recyclerView = findViewById(R.id.rvHome_RT)
////        recyclerView.layoutManager = LinearLayoutManager(this)
//
////        val database = Database()
////        val dataRepository = DataRepository(database)
////        val data = dataRepository.getData()
////        adapter = MyAdapter(data)
////        recyclerView.adapter = adapter
//        val news = fetchNews(this)
//        news.execute()
//    }
//
//    override fun onDataFetched(data: List<modelPost>) {
//
//        val listView: ListView = findViewById(R.id.listViewTest)
//        val newsDetails = data.map { "${it.announcementTitle}\n Expiry Date: ${it.announcementContent}"}.toTypedArray()
//        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, newsDetails)
//        listView.adapter = adapter
//    }
//
//}