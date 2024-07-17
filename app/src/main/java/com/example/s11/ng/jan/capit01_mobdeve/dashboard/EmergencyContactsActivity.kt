package com.example.s11.ng.jan.capit01_mobdeve.dashboard

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt

class EmergencyContactsActivity : AppCompatActivity() {

    private lateinit var emergencyContactsList: ListView
    private var emergencyContacts: String = "09190785452"
    private val CALL_PHONE_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emergency_contacts)

        emergencyContactsList = findViewById(R.id.emergency_contacts_list)

        val emergencyContacts = listOf(
            EmergencyContact("Police", "911"),
            EmergencyContact("Ambulance", "112"),
            EmergencyContact("Fire Department", "101"),
            EmergencyContact("Emergency Services", "09190785452")
        )

        val adapter = ArrayAdapter<EmergencyContact>(
            this,
            android.R.layout.simple_list_item_1,
            emergencyContacts
        )

        emergencyContactsList.adapter = adapter

        emergencyContactsList.setOnItemClickListener { _, _, position, _ ->
            val selectedContact = emergencyContacts[position]
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                makeCall(selectedContact.phoneNumber)
            } else {
                requestPermission(selectedContact.phoneNumber)
                Toast.makeText(this, "Permission denied. Cannot make call.", Toast.LENGTH_SHORT).show()
            }
        }

        setupFooter_rt()
    }

    private fun requestPermission(phoneNumber: String) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)) {
            Toast.makeText(this, "This app needs permission to make a call.", Toast.LENGTH_SHORT).show()
        }
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CALL_PHONE), CALL_PHONE_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CALL_PHONE_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                makeCall(emergencyContacts)
            } else {
                Toast.makeText(this, "Permission denied. Cannot make call.", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumber")
        startActivity(intent)
    }
}