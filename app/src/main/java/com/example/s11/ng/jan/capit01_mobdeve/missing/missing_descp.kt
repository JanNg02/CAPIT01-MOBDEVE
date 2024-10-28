package com.example.s11.ng.jan.capit01_mobdeve.missing

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo

class missing_descp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.missing_desc_layout)


        val titleTextView = findViewById<TextView>(R.id.titleDetailsReported_TV)
        val fullNameTextView = findViewById<TextView>(R.id.fullNameReported_TV)
        val ageTextView = findViewById<TextView>(R.id.ageReported_TV)
        val sexTextView = findViewById<TextView>(R.id.sexReported_TV)
        val filedByTextView = findViewById<TextView>(R.id.filedBy_Reported_TV)
        val descriptionTextView = findViewById<TextView>(R.id.description_missing_Reported_TV)
        val areaLastSeenTextView = findViewById<TextView>(R.id.areaLastSeenReported_TV)
        val contactNumTextView = findViewById<TextView>(R.id.ContactNumberReported_TV)
        val missingPersonImageView = findViewById<ImageView>(R.id.missingPersonImage)

        val intent = intent
        val extras = intent.extras

        extras?.let {
            val title = it.getString("title")
            val missingFullName = it.getString("missingFullName")
            val age = it.getInt("age")
            val sex = it.getString("gender")
            val areaLastSeen = it.getString("areaLastSeen")
            val description = it.getString("description")
            val filedBy = it.getString("filedBy")
            val dateSubmitted = it.getString("dateSubmitted")
            val contactNum = it.getString("contactNum")
            val missingPersonImage = it.getString("missingPersonImage")

            titleTextView.text = title
            fullNameTextView.text = missingFullName
            descriptionTextView.text = description
            areaLastSeenTextView.text = areaLastSeen
            ageTextView.text = age.toString()
            sexTextView.text = sex
            filedByTextView.text = "Filed By: " + filedBy + " on " + dateSubmitted
            contactNumTextView.text = contactNum

            if (!missingPersonImage.isNullOrBlank()) {
                val imgBytes = Base64.decode(missingPersonImage, Base64.DEFAULT)
                val decodedImg = BitmapFactory.decodeByteArray(imgBytes, 0, imgBytes.size)
                missingPersonImageView.setImageBitmap(decodedImg)
            }
        }

        setupFooter_bo()
    }
}