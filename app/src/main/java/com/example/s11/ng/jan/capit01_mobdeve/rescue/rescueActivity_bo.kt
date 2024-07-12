package com.example.s11.ng.jan.capit01_mobdeve.rescue

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.fingerprint.fingerprintActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.missing.missingActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.rescue.rescueActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo

class rescueActivity_bo : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ondemand_bo)

        setupFooter_bo() // Call the footer setup function
    }
}