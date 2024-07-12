package com.example.s11.ng.jan.capit01_mobdeve

import android.content.Intent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.map.mapActivity_rt

fun AppCompatActivity.setupFooter_rt() {
    val pnabutton: ImageButton = findViewById(R.id.pna_RT)
    pnabutton.setOnClickListener {
        moveToPnaRT(this)
    }

    val mapbutton: ImageButton = findViewById(R.id.map_RT)
    mapbutton.setOnClickListener {
        moveToMapRT(this)
    }

    val helpbutton: ImageButton = findViewById(R.id.help_RT)
    helpbutton.setOnClickListener {
        moveToHelpRT(this)
    }

    val filebutton: ImageButton = findViewById(R.id.file_RT)
    filebutton.setOnClickListener {
        moveToFileRT(this)
    }

    val dashbutton: ImageButton = findViewById(R.id.dashboard_RT)
    dashbutton.setOnClickListener {
        moveToDashboardRT(this)
    }
}

fun AppCompatActivity.moveToPnaRT(appCompatActivity: AppCompatActivity) {
    val intent = Intent(applicationContext, homeActivity_rt::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.moveToMapRT(appCompatActivity: AppCompatActivity) {
    val intent = Intent(applicationContext, mapActivity_rt::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.moveToHelpRT(appCompatActivity: AppCompatActivity) {
    val intent = Intent(applicationContext, helpActivity_rt::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.moveToFileRT(appCompatActivity: AppCompatActivity) {
    val intent = Intent(applicationContext, fileActivity_rt::class.java)
    startActivity(intent)
    finish()
}

fun AppCompatActivity.moveToDashboardRT(appCompatActivity: AppCompatActivity) {
    val intent = Intent(applicationContext, dashboardActivity_rt::class.java)
    startActivity(intent)
    finish()
}