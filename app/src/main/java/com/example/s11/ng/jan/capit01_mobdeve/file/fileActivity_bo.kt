package com.example.s11.ng.jan.capit01_mobdeve.file

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.fingerprint.fingerprintActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.missing.missingActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class tanodReport(
    @SerializedName("reportID") val reportID: Int,
    @SerializedName("reportName") val reportName: String,
    @SerializedName("dateSubmitted") val dateSubmitted: String,
    @SerializedName("filedBy") val filedBy: String,
    @SerializedName("teamName") val teamName: String,
    @SerializedName("fullReport") val fullReport: String,
    @SerializedName("reportStatus") val reportStatus: Boolean,
    @SerializedName("reportFrom") val reportFrom : String
)

class fileActivity_bo : AppCompatActivity() {

    private lateinit var locationBO_ET: EditText
    private lateinit var descriptionBO_ET: EditText
    private lateinit var submitButton: Button

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    interface tanodsReportAPI {
        @POST("tanodsReport")
        fun reportTanod(@Body requestBody: RequestBody): Call<ResponseBody>
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_bo)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        submitButton = findViewById(R.id.submit_file_bo)

        submitButton.setOnClickListener {
            if (checkPermission()) {
                fileReport()
            } else {
                requestPermission()
            }
        }

        setupFooter_bo() // Call the footer setup function
    }

    private fun fileReport(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result
            if (location != null) {
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                val currentAddress = addresses[0].getAddressLine(0)
                locationBO_ET = findViewById(R.id.locationBO)
                descriptionBO_ET = findViewById(R.id.detailsBO)

                val editTexts = listOf(locationBO_ET, descriptionBO_ET)

                //get the information from the input fields
                val locationText = locationBO_ET.text.toString()
                val descriptionText = descriptionBO_ET.text.toString()

                //get the current Session or current User from shared preference
                val sp = getSharedPreferences("userSession", MODE_PRIVATE)
                val fullNameData = sp.getString("residentFullName", "null")
                val teamName = sp.getString("teamName", "null")

                val dateSubmitted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(
                    Date()
                )
                val ID = 0
                val filedBy = fullNameData.toString() //Add Sessioned User
                val teamID = teamName.toString()

                if (locationText.isEmpty() ||descriptionText.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }

                val report = tanodReport(ID, locationText, dateSubmitted, filedBy, teamID, descriptionText, true, currentAddress)
                val gson = Gson()
                val json = gson.toJson(report)

                val requestBody = json.toRequestBody("application/json".toMediaType())


                val retrofit = Retrofit.Builder()
                    .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val tanodReportAPI = retrofit.create(tanodsReportAPI::class.java)

                Toast.makeText(this, "Report Sent", Toast.LENGTH_SHORT).show()
                editTexts.forEach { it.text.clear() }

                val call = tanodReportAPI.reportTanod(requestBody)
                call.enqueue(object : Callback<ResponseBody> {
                    override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                        // handle failure
                        Log.e("Error", t.message.toString())
                    }

                    override fun onResponse(
                        call: Call<ResponseBody>,
                        response: Response<ResponseBody>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null) {
                                Log.d("Response", responseBody.toString())
                            } else {
                                Log.w("Response", "Response body is null")
                            }
                        } else {
                            Log.e("Error", "Response code: ${response.code()}")
                            Log.e("Error", "Response error message: ${response.message()}")
                            if (response.errorBody() != null) {
                                Log.e(
                                    "Error",
                                    "Response error body: ${response.errorBody()!!.string()}"
                                )
                            }
                        }
                    }
                })
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fileReport()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}