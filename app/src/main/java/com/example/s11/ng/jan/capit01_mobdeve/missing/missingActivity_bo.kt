package com.example.s11.ng.jan.capit01_mobdeve.missing

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.fingerprint.fingerprintActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.login.login_act
import com.example.s11.ng.jan.capit01_mobdeve.missing.missingActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.rescue.rescueActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo
import com.google.android.material.floatingactionbutton.FloatingActionButton

class missingActivity_bo : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.missing_bo)

        val logoutButton: FloatingActionButton = findViewById(R.id.logout_button_BO)
        logoutButton.setOnClickListener{
            val sp = getSharedPreferences("userSession", MODE_PRIVATE)
            val editor = sp.edit()

            editor.clear()
            editor.apply()
            moveToLogin()
        }

        setupFooter_bo() // Call the footer setup function
    }

    fun moveToLogin(){
        val intent = Intent(applicationContext, login_act::class.java)
        startActivity(intent)
        finish()
    }
}