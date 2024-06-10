package com.example.s11.ng.jan.capit01_mobdeve.login

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt

class login_act : AppCompatActivity() {

    companion object {
        const val usernameKey: String = "nameKey"
        const val passKey: String = "passKey"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_rt)

        val loginRTbutton: Button = findViewById(R.id.loginRT)
        loginRTbutton.setOnClickListener{
//            val username: EditText = findViewById(R.id.usernameRT)
//            val userString = username.text.toString()
//            val password: EditText = findViewById(R.id.passwordRT)
//            val passwordString = password.text.toString()
//            val userDocument = userString+passwordString
//            var bool = false

            moveToHomeRT()
        }
        val loginBObutton: Button = findViewById(R.id.loginBO)
        loginBObutton.setOnClickListener{

            moveToHomeBO()
        }
    }

    fun moveToHomeRT(){
        val intent = Intent(this, homeActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToHomeBO(){
        val intent = Intent(this, homeActivity_bo::class.java)
        startActivity(intent)
        finish()
    }
}