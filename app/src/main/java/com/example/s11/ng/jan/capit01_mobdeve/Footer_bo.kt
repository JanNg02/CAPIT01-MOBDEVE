package com.example.s11.ng.jan.capit01_mobdeve

import android.content.Intent
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.fingerprint.fingerprintActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.missing.missingActivity_bo

fun AppCompatActivity.setupFooter_bo() {
    val responsebutton: ImageButton = findViewById(R.id.response_BO)
    responsebutton.setOnClickListener{
        navigateTo(homeActivity_bo::class.java)
    }

    val missingbutton: ImageButton = findViewById(R.id.missing_BO)
    missingbutton.setOnClickListener{
        navigateTo(missingActivity_bo::class.java)
    }

    val fileareabutton: ImageButton = findViewById(R.id.filearea_BO)
    fileareabutton.setOnClickListener{
        navigateTo(fileActivity_bo::class.java)
    }

    val fingerprintbutton: ImageButton = findViewById(R.id.fingerprint_BO)
    fingerprintbutton.setOnClickListener{
        navigateTo(fingerprintActivity_bo::class.java)
    }

    val filebutton: ImageButton = findViewById(R.id.file_RT)
    filebutton.setOnClickListener {
        navigateTo(fileActivity_rt::class.java)
    }
}
private fun AppCompatActivity.navigateTo(destinationActivity: Class<*>) {
    if (this::class.java != destinationActivity) {
        val intent = Intent(applicationContext, destinationActivity)
        startActivity(intent)
        finish()
    }
}