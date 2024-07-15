package com.example.s11.ng.jan.capit01_mobdeve.missing

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo

class missing_descp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.missing_desc_layout)


        val titleTextView = findViewById<TextView>(R.id.titleDetails_TV)
        val fullNameTextView = findViewById<TextView>(R.id.fullName_TV)
        val ageTextView = findViewById<TextView>(R.id.age_TV)
        val sexTextView = findViewById<TextView>(R.id.sex_TV)
        val filedByTextView = findViewById<TextView>(R.id.filedBy)
        val descriptionTextView = findViewById<TextView>(R.id.description_missing_TV)
        val areaLastSeenTextView = findViewById<TextView>(R.id.areaLastSeen_TV)
        val contactNumTextView = findViewById<TextView>(R.id.ContactNumber_TV)

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

            titleTextView.text = title
            fullNameTextView.text = missingFullName
            descriptionTextView.text = description
            areaLastSeenTextView.text = areaLastSeen
            ageTextView.text = age.toString()
            sexTextView.text = sex
            filedByTextView.text = "Filed By: " + filedBy + " on " + dateSubmitted
            contactNumTextView.text = contactNum
        }

        setupFooter_bo()
    }
}