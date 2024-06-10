package com.example.s11.ng.jan.capit01_mobdeve.home

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.rescue.rescueActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.missing.missingActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.fingerprint.fingerprintActivity_bo


class homeActivity_bo : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView

//    private val viewHomeLauncher = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
//        if (result.resultCode == RESULT_OK) {
//
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_bo)

        val responsebutton: ImageButton = findViewById(R.id.response_BO)
        responsebutton.setOnClickListener{
            moveToResponseBO()
        }

        val rescuebutton: ImageButton = findViewById(R.id.rescueboard_BO)
        rescuebutton.setOnClickListener{
            moveToRescueBO()
        }

        val missingbutton: ImageButton = findViewById(R.id.missing_BO)
        missingbutton.setOnClickListener{
            moveToMissingBO()
        }

        val fileareabutton: ImageButton = findViewById(R.id.filearea_BO)
        fileareabutton.setOnClickListener{
            moveToFileareaBO()
        }

        val fingerprintbutton: ImageButton = findViewById(R.id.fingerprint_BO)
        fingerprintbutton.setOnClickListener{
            moveToFingerprintBO()
        }
    }

    fun moveToResponseBO(){
        val intent = Intent(applicationContext, homeActivity_bo::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToRescueBO(){
        val intent = Intent(applicationContext, rescueActivity_bo::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToMissingBO(){
        val intent = Intent(applicationContext, missingActivity_bo::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToFileareaBO(){
        val intent = Intent(applicationContext, fileActivity_bo::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToFingerprintBO(){
        val intent = Intent(applicationContext, fingerprintActivity_bo::class.java)
        startActivity(intent)
        finish()
    }

}