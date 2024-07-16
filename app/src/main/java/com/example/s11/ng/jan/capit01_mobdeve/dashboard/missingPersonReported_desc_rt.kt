package com.example.s11.ng.jan.capit01_mobdeve.dashboard

import android.graphics.Color
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt

class missingPersonReported_desc_rt : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.missing_person_reported_desc_rt)

        val titleTextView = findViewById<TextView>(R.id.titleDetailsReported_TV)
        val fullNameTextView = findViewById<TextView>(R.id.fullNameReported_TV)
        val ageTextView = findViewById<TextView>(R.id.ageReported_TV)
        val sexTextView = findViewById<TextView>(R.id.sexReported_TV)
        val filedByTextView = findViewById<TextView>(R.id.filedBy_Reported_TV)
        val descriptionTextView = findViewById<TextView>(R.id.description_missing_Reported_TV)
        val areaLastSeenTextView = findViewById<TextView>(R.id.areaLastSeenReported_TV)
        val contactNumTextView = findViewById<TextView>(R.id.ContactNumberReported_TV)
        val assignedTeamTextView = findViewById<TextView>(R.id.assignedTeam_TV)
        val isFoundTextView = findViewById<TextView>(R.id.isFound_TV)

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
            val assignedTeam = it.getString("teamID")
            val isFound = it.getBoolean("isFound")

            titleTextView.text = title
            fullNameTextView.text = missingFullName
            descriptionTextView.text = description
            areaLastSeenTextView.text = areaLastSeen
            ageTextView.text = age.toString()
            sexTextView.text = sex
            filedByTextView.text = "Filed By: " + filedBy + " on " + dateSubmitted
            contactNumTextView.text = contactNum
            assignedTeamTextView.text =
                if (assignedTeam == "None") {
                    assignedTeamTextView.setTextColor(Color.RED); // Red for no team
                    "No Team Assigned Yet"
                } else {
                    assignedTeamTextView.setTextColor(Color.GREEN); // Green for team assigned
                    "Team " + assignedTeam // Add the team name if assigned
                }

            isFoundTextView.text =
                if (isFound) {
                    isFoundTextView.setTextColor(Color.GREEN); // Green for found
                    "Found Meet the Person in the Brgy Hall"
                } else {
                    isFoundTextView.setTextColor(Color.RED); // Red for not found
                    "Not Found Yet"
                }
        }

        setupFooter_rt()
    }
}