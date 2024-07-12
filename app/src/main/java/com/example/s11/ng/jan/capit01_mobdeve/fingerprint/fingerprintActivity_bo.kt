package com.example.s11.ng.jan.capit01_mobdeve.fingerprint

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo

class fingerprintActivity_bo : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fingerprint_bo)

        setupFooter_bo() // Call the footer setup function
    }
}