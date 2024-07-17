package com.example.s11.ng.jan.capit01_mobdeve.home

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.s11.ng.jan.capit01_mobdeve.OnDataFetchedListener
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.adapter.AnnouncementAdapter
import com.example.s11.ng.jan.capit01_mobdeve.adapter.modelPost
import com.example.s11.ng.jan.capit01_mobdeve.fetchPost
import com.example.s11.ng.jan.capit01_mobdeve.login.login_act
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.messaging.FirebaseMessaging


class homeActivity_rt : AppCompatActivity(), OnDataFetchedListener, SwipeRefreshLayout.OnRefreshListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_rt)

        recyclerView = findViewById(R.id.rvHome_RT)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AnnouncementAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        swipeRefreshLayout = findViewById(R.id.refreshAnnouncement)
        swipeRefreshLayout.setOnRefreshListener(this)

        // Initialize Firebase Messaging
        FirebaseMessaging.getInstance().subscribeToTopic("announcements")

        val fetchPost = fetchPost(this)
        fetchPost.execute()

        val logoutButton: FloatingActionButton = findViewById(R.id.logout_button_RT)
        logoutButton.setOnClickListener{
            logout()
        }

        setupFooter_rt() // Call the footer setup function
    }

    override fun onDataFetched(data: List<modelPost>) {
        adapter.updateData(data)
    }

    override fun onRefresh() {
        // Call your data fetching function here
        val fetchPost = fetchPost(this)
        fetchPost.execute()
        swipeRefreshLayout.isRefreshing = false // Reset the refresh indicator
    }

    fun moveToLogin(){
        val intent = Intent(applicationContext, login_act::class.java)
        startActivity(intent)
        finish()
    }

    fun logout(){
        val sp = getSharedPreferences("userSession", MODE_PRIVATE)
        val editor = sp.edit()

        editor.clear()
        editor.apply()
        moveToLogin()
    }
}