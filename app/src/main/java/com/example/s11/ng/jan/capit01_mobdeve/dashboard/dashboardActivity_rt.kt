package com.example.s11.ng.jan.capit01_mobdeve.dashboard

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.map.mapActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.login.login_act
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt
import com.google.android.material.floatingactionbutton.FloatingActionButton

class dashboardActivity_rt : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_rt)

        val missingPersonReportedButton: Button = findViewById(R.id.missingReported_button_rt)

       missingPersonReportedButton.setOnClickListener{
           val intent = Intent(applicationContext, reportedMissing_rt::class.java)
           startActivity(intent)
       }

        val logoutButton: FloatingActionButton = findViewById(R.id.logout_button_RT)
        logoutButton.setOnClickListener{
            val sp = getSharedPreferences("userSession", MODE_PRIVATE)
            val editor = sp.edit()

            editor.clear()
            editor.apply()
            moveToLogin()
        }

        setupFooter_rt() // Call the footer setup function
    }

    fun moveToLogin(){
        val intent = Intent(applicationContext, login_act::class.java)
        startActivity(intent)
        finish()
    }
}