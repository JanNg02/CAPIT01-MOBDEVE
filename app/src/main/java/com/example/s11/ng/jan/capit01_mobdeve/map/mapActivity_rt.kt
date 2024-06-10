package com.example.s11.ng.jan.capit01_mobdeve.map

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.map.mapActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt

class mapActivity_rt : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_rt)

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