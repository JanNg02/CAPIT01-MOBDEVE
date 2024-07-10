package com.example.s11.ng.jan.capit01_mobdeve.home

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.OnDataFetchedListener
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.adapter.AnnouncementAdapter
import com.example.s11.ng.jan.capit01_mobdeve.adapter.modelPost
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.fetchPost
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.map.mapActivity_rt
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.RemoteMessage


class homeActivity_rt : AppCompatActivity(), OnDataFetchedListener{

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AnnouncementAdapter

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_rt)

        recyclerView = findViewById(R.id.rvHome_RT)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = AnnouncementAdapter(mutableListOf(), this)
        recyclerView.adapter = adapter

        // Initialize Firebase Messaging
        FirebaseMessaging.getInstance().subscribeToTopic("announcements")

        val fetchPost = fetchPost(this)
        fetchPost.execute()

        val pnabutton: ImageButton = findViewById(R.id.pna_RT)
        pnabutton.setOnClickListener{
            moveToPnaRT()
        }

        val mapbutton: ImageButton = findViewById(R.id.map_RT)
        mapbutton.setOnClickListener{
            moveToMapRT()
        }

        val helpbutton: ImageButton = findViewById(R.id.help_RT)
        helpbutton.setOnClickListener{
            moveToHelpRT()
        }

        val filebutton: ImageButton = findViewById(R.id.file_RT)
        filebutton.setOnClickListener{
            moveToFileRT()
        }

        val dashbutton: ImageButton = findViewById(R.id.dashboard_RT)
        dashbutton.setOnClickListener{
            moveToDashboardRT()
        }
    }

    override fun onDataFetched(data: List<modelPost>) {
        adapter.updateData(data)
    }
    fun moveToPnaRT(){
        val intent = Intent(applicationContext, homeActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToMapRT(){
        val intent = Intent(applicationContext, mapActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToHelpRT(){
        val intent = Intent(applicationContext, helpActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToFileRT(){
        val intent = Intent(applicationContext, fileActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToDashboardRT(){
        val intent = Intent(applicationContext, dashboardActivity_rt::class.java)
        startActivity(intent)
        finish()
    }
}