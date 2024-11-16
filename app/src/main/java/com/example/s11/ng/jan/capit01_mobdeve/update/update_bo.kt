package com.example.s11.ng.jan.capit01_mobdeve.update

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment.SavedState
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.home.currentAssignments
import com.example.s11.ng.jan.capit01_mobdeve.home.currentassignmentBO.teamDataAPI
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.teamsData
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
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class teamsData(
    @SerializedName("teamName") val teamName: String,
    @SerializedName("teamMembers") val teamMembers: List<String>,
    @SerializedName("teamStatus") val teamStatus: Boolean,
    @SerializedName("teamTasks") val teamTasks: String,
    @SerializedName("currentAssignment") val currentAssignment: List<currentAssignments>,
    @SerializedName("teamArea") val teamArea: String,
    @SerializedName("teamID") val teamID: Int
)

data class updateReport(
    @SerializedName("updateReportID") val reportID: String,
    @SerializedName("updateTitle") val reportTitle: String,
    @SerializedName("updateDetails") val reportDetails: String,
    @SerializedName("dateSubmitted") val dateSubmitted: String,
    @SerializedName("filedBy") val filedBy: String
)

data class updateSOScase(
    @SerializedName("updateSosID") val reportID: String,
    @SerializedName("updateTitle") val reportTitle: String,
    @SerializedName("updateDetails") val reportDetails: String,
    @SerializedName("dateSubmitted") val dateSubmitted: String,
    @SerializedName("filedBy") val filedBy: String
)

data class updateMissin(
    @SerializedName("updateMiaID") val reportID: String,
    @SerializedName("updateTitle") val reportTitle: String,
    @SerializedName("updateDetails") val reportDetails: String,
    @SerializedName("dateSubmitted") val dateSubmitted: String,
    @SerializedName("filedBy") val filedBy: String
)

class update_bo : AppCompatActivity() {

    private lateinit var teamFound: teamsData
    private lateinit var updateTitleBO_ET: EditText
    private lateinit var updateDetailsBO_ET: EditText
    private lateinit var submitButton: Button
    private var teamDataLoaded = false

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    interface updateTanodsAPI {
        @POST("updateTanods")
        fun updateTanods(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    interface updateSOSAPI {
        @POST("updateSOS")
        fun updateSOSc(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    interface updateMissingAPI{
        @POST("updateMissing")
        fun updateMissings(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    interface teamDataAPI {
        @GET("getTeamTasks")
        fun getTeamTasks(@Query("userName") userName: String): Call<teamsData>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.update_bo)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val spTask = getSharedPreferences("saveTaskID", MODE_PRIVATE)
        val savedTaskID = spTask.getString("taskID", "null")
        Log.d("TaskIDUpdates", savedTaskID.toString())

        submitButton = findViewById(R.id.updatesubmit)

        submitButton.setOnClickListener {
            if (checkPermission()) {
                retrieveTeamData()
            }
            else {
                requestPermission()
            }
        }

        val backbutton: ImageButton = findViewById(R.id.updateBack)
        backbutton.setOnClickListener{
            navigateTo(homeActivity_bo::class.java)
        }
    }

    fun updateReport(){
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
                updateTitleBO_ET = findViewById(R.id.updateTitleBO)
                updateDetailsBO_ET = findViewById(R.id.updateDetailsBO)

                val editTexts = listOf(updateTitleBO_ET, updateDetailsBO_ET)

                //get the information from the input fields
                val updateText = updateTitleBO_ET.text.toString()
                val detailsText = updateDetailsBO_ET.text.toString()

                //get the current Session or current User from shared preference
                val sp = getSharedPreferences("userSession", MODE_PRIVATE)
                val fullNameData = sp.getString("residentFullName", "null")

                val spTask = getSharedPreferences("saveTaskID", MODE_PRIVATE)
                val savedTaskID = spTask.getString("taskID", "null")
                Log.d("TaskIDUpdates", savedTaskID.toString())

                val dateSubmitted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(
                    Date()
                )
                val ID = savedTaskID.toString()
                val filedBy = fullNameData.toString() //Add Sessioned User

                if (updateText.isEmpty() ||detailsText.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }

                //DATA SET TO BE SENT
                val report = updateReport(ID,updateText,detailsText,dateSubmitted,filedBy)
                val gson = Gson()
                val json = gson.toJson(report)


                val requestBody = json.toRequestBody("application/json".toMediaType())


                val retrofit = Retrofit.Builder()
                    .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val updateTanodAPI = retrofit.create(updateTanodsAPI::class.java)

                Toast.makeText(this, "Update Sent", Toast.LENGTH_SHORT).show()
                editTexts.forEach { it.text.clear() }

                val call = updateTanodAPI.updateTanods(requestBody)
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

    fun updateMissingReport(){
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
                updateTitleBO_ET = findViewById(R.id.updateTitleBO)
                updateDetailsBO_ET = findViewById(R.id.updateDetailsBO)

                val editTexts = listOf(updateTitleBO_ET, updateDetailsBO_ET)

                //get the information from the input fields
                val updateText = updateTitleBO_ET.text.toString()
                val detailsText = updateDetailsBO_ET.text.toString()

                //get the current Session or current User from shared preference
                val sp = getSharedPreferences("userSession", MODE_PRIVATE)
                val fullNameData = sp.getString("residentFullName", "null")

                val spTask = getSharedPreferences("saveTaskID", MODE_PRIVATE)
                val savedTaskID = spTask.getString("taskID", "null")
                Log.d("TaskIDUpdates", savedTaskID.toString())

                val dateSubmitted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(
                    Date()
                )
                val ID = savedTaskID.toString()
                val filedBy = fullNameData.toString() //Add Sessioned User

                if (updateText.isEmpty() ||detailsText.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }

                //DATA SET TO BE SENT
                val report = updateMissin(ID,updateText,detailsText,dateSubmitted,filedBy)
                val gson = Gson()
                val json = gson.toJson(report)


                val requestBody = json.toRequestBody("application/json".toMediaType())


                val retrofit = Retrofit.Builder()
                    .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val updateMissingAPI = retrofit.create(updateMissingAPI::class.java)

                Toast.makeText(this, "Update Sent", Toast.LENGTH_SHORT).show()
                editTexts.forEach { it.text.clear() }

                val call = updateMissingAPI.updateMissings(requestBody)
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

    fun updateSOSReport(){
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
                updateTitleBO_ET = findViewById(R.id.updateTitleBO)
                updateDetailsBO_ET = findViewById(R.id.updateDetailsBO)

                val editTexts = listOf(updateTitleBO_ET, updateDetailsBO_ET)

                //get the information from the input fields
                val updateText = updateTitleBO_ET.text.toString()
                val detailsText = updateDetailsBO_ET.text.toString()

                //get the current Session or current User from shared preference
                val sp = getSharedPreferences("userSession", MODE_PRIVATE)
                val fullNameData = sp.getString("residentFullName", "null")

                val spTask = getSharedPreferences("saveTaskID", MODE_PRIVATE)
                val savedTaskID = spTask.getString("taskID", "null")
                Log.d("TaskIDUpdates", savedTaskID.toString())

                val dateSubmitted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(
                    Date()
                )
                val ID = savedTaskID.toString()
                val filedBy = fullNameData.toString() //Add Sessioned User

                if (updateText.isEmpty() ||detailsText.isEmpty()) {
                    Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }

                //DATA SET TO BE SENT
                val report = updateSOScase(ID,updateText,detailsText,dateSubmitted,filedBy)
                val gson = Gson()
                val json = gson.toJson(report)


                val requestBody = json.toRequestBody("application/json".toMediaType())


                val retrofit = Retrofit.Builder()
                    .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

                val updateSOSAPI = retrofit.create(updateSOSAPI::class.java)

                Toast.makeText(this, "Update Sent", Toast.LENGTH_SHORT).show()
                editTexts.forEach { it.text.clear() }

                val call = updateSOSAPI.updateSOSc(requestBody)
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

    fun retrieveTeamData() {

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val teamAPI = retrofit.create(teamDataAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        val sp = getSharedPreferences("userSession", MODE_PRIVATE)
        val fullNameData = sp.getString("residentFullName", "null")

        val call = teamAPI.getTeamTasks(fullNameData.toString())
        Log.d("TeamString", call.toString())

        call.enqueue(object : Callback<teamsData> {
            override fun onResponse(call: Call<teamsData>, response: Response<teamsData>) {
                if (response.isSuccessful) {

                    val teamsData = response.body();

                    val spTask = getSharedPreferences("saveCurrentAssignment", MODE_PRIVATE)
                    val checkAssignmentID = spTask.getString("assignmentID", "null")
                    Log.d("CurrentAssignment", checkAssignmentID.toString())

                    if (teamsData != null) {
                        teamFound = teamsData
                        //if team is a patrol team go to retrieve patrol data
                        if (checkAssignmentID.toString().contains("p")) {
                            teamDataLoaded = true
                            updateReport()
                        } else if (checkAssignmentID.toString().contains("mia")) {
                            teamDataLoaded = true
                            updateMissingReport()
                        } else if (checkAssignmentID.toString().contains("sos")) {
                            teamDataLoaded = true
                            updateSOSReport()
                        }
                    }
                } else {
                    teamDataLoaded = true
                    Toast.makeText(this@update_bo, "task id not found", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<teamsData>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@update_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                teamDataLoaded = true
            }
        })
    }

    fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 100)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateReport()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

fun AppCompatActivity.navigateTo(destinationActivity: Class<*>) {
    if (this::class.java != destinationActivity) {
        val intent = Intent(applicationContext, destinationActivity)
        startActivity(intent)
        finish()
    }
}