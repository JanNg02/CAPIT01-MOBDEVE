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
        navigateTo(homeActivity_rt::class.java)
    }

    val mapbutton: ImageButton = findViewById(R.id.map_RT)
    mapbutton.setOnClickListener {
        navigateTo(mapActivity_rt::class.java)
    }

    val helpbutton: ImageButton = findViewById(R.id.help_RT)
    helpbutton.setOnClickListener {
        navigateTo(helpActivity_rt::class.java)
    }

    val dashbutton: ImageButton = findViewById(R.id.dashboard_RT)
    dashbutton.setOnClickListener {
        navigateTo(dashboardActivity_rt::class.java)
    }
}

private fun AppCompatActivity.navigateTo(destinationActivity: Class<*>) {
    if (this::class.java != destinationActivity) {
        val intent = Intent(applicationContext, destinationActivity)
        startActivity(intent)
        finish()
    }
}