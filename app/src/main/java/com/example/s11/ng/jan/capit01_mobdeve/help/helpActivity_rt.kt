package com.example.s11.ng.jan.capit01_mobdeve.help

//import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import android.Manifest
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.map.OnDataFetchedListener
import com.example.s11.ng.jan.capit01_mobdeve.map.getEvacCenter
import com.example.s11.ng.jan.capit01_mobdeve.map.modelEvacCenter
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
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
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import android.graphics.Color
import androidx.core.content.ContentProviderCompat.requireContext

data class SOS(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("currentAddress") val currentAddress: String,
    @SerializedName("dateLastSent") val dateLastSent: String,
    @SerializedName("age") val age: Int,
    @SerializedName("sex") val sex: String,
    @SerializedName("teamID") val teamID: String,
    @SerializedName("isFound") val isFound: Boolean,
    @SerializedName("pwd") val pwd: List<String>,
    @SerializedName("sick") val sick: List<String>,
    @SerializedName("pregnant") val pregnant: List<String>,
    @SerializedName("currentSituation") val currentSituation: String
)

class helpActivity_rt : AppCompatActivity() {
    interface sosAPI {
        @POST("sendSOS")
        fun postSOS(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var pwdCheckedBoxes = mutableListOf<String>()
    private var sickCheckedBoxes = mutableListOf<String>()
    private var pregnantCheckedBoxes = mutableListOf<String>()
    private var currentSituation = ""
    private var otherSickText = ""
    private var isSickChecked = false
    private var evacuationCenters: List<modelEvacCenter> = emptyList()
    private lateinit var openpwdModal : CheckBox
    private lateinit var opensickModal : CheckBox
    private lateinit var openpregnantModal : CheckBox
    private lateinit var pwdsharedPreferences: SharedPreferences
    private lateinit var sicksharedPreferences: SharedPreferences
    private lateinit var pregnantsharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_rt)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        openpwdModal = findViewById(R.id.pwd_button)
        pwdsharedPreferences = getSharedPreferences("PwdModalPrefs", Context.MODE_PRIVATE)
        opensickModal = findViewById(R.id.sick_button)
        sicksharedPreferences = getSharedPreferences("SickModalPrefs", Context.MODE_PRIVATE)
        openpregnantModal = findViewById(R.id.pregnant_button)
        pregnantsharedPreferences = getSharedPreferences("PregnantModalPrefs", Context.MODE_PRIVATE)

        // Fetch evacuation centers
        getEvacCenter(object : OnDataFetchedListener {
            override fun onDataFetched(data: List<modelEvacCenter>) {
                evacuationCenters = data
            }
        }).execute()


        setCheckBoxes()

        // SOS Button
        var sosButton : ImageButton = findViewById(R.id.imageButton)

        sosButton.setOnClickListener{
            if (checkPermission()) {
                if (isSickChecked && otherSickText.isNotEmpty() && !sickCheckedBoxes.contains(otherSickText)) {
                    sickCheckedBoxes.add(otherSickText)
                }
                getCurrentLocation()
            } else {
                requestPermission()
            }
        }

        setupFooter_rt() // Call the footer setup function
    }


    // A function to handle checkbox changes
    private fun handleCheckboxChange(checkboxId: Int, list: MutableList<String>, text: String) {
        findViewById<CheckBox>(checkboxId).setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                list.add(text)
            } else {
                list.remove(text)
            }
        }
    }

    // A function to handle edit text changes
    private fun handleEditTextChange(editTextId: Int, callback: (String) -> Unit) {
        findViewById<EditText>(editTextId).addTextChangedListener { editable ->
            callback(editable.toString())
        }
    }


    private fun setCheckBoxes(){
        val pwdButton: CheckBox = findViewById(R.id.pwd_button)
        val sickButton: CheckBox = findViewById(R.id.sick_button)
        val pregnantButton: CheckBox = findViewById(R.id.pregnant_button)

        val sp = getSharedPreferences("userSession", MODE_PRIVATE)
        val sex = sp.getString("residentSex", "null").toString()
        if(sex == "Male") {
            pregnantButton.visibility = View.GONE
        }

        pwdButton.setOnClickListener {
            val modalFragment = pwdDropdownModal()
            modalFragment.setSelectionListener(object : pwdDropdownModal.PwdSelectionListener {
                override fun onPwdSelection(selectedItems: List<String>) {
                    pwdCheckedBoxes.clear()
                    pwdCheckedBoxes.addAll(selectedItems)
                }
            })
            modalFragment.show(supportFragmentManager, "DropdownModal")
        }

        sickButton.setOnClickListener {
            val modalFragment = sickDropdownModal()
            modalFragment.setSelectionListener(object : sickDropdownModal.SickSelectionListener {
                override fun onSickSelection(selectedItems: List<String>) {
                    sickCheckedBoxes.clear()
                    sickCheckedBoxes.addAll(selectedItems)
                }
            })
            modalFragment.show(supportFragmentManager, "DropdownModal")
        }

        pregnantButton.setOnClickListener{
            val modalFragment = pregnantDropdownModal()
            modalFragment.setSelectionListener(object : pregnantDropdownModal.PregnantSelectionListener {
                override fun onPregnantSelection(selectedItems: List<String>) {
                    pregnantCheckedBoxes.clear()
                    pregnantCheckedBoxes.addAll(selectedItems)
                }
            })
            modalFragment.show(supportFragmentManager, "DropdownModal")
        }

        handleEditTextChange(R.id.situation_edittext) { currentSituation = it }
    }

    private fun uncheckPwdCheckboxes() {
        findViewById<CheckBox>(R.id.hearing_impairment_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.vision_impairment_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.mobility_disability_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.mental_disability_checkbox).isChecked = false
        // Add more checkboxes for other PWD options
    }

    private fun uncheckSickCheckboxes() {
        findViewById<CheckBox>(R.id.diabetes_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.heart_problems_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.flu_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.stroke_checkbox).isChecked = false
        // Add more checkboxes for other sick options

        findViewById<EditText>(R.id.other_sick_edittext).text.clear()
    }

    private fun uncheckPregnantRadiobuttons() {
        findViewById<CheckBox>(R.id.first_trimester_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.second_trimester_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.third_trimester_checkbox).isChecked = false
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
                getCurrentLocation()
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        fusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
            val location: Location? = task.result
            if (location != null) {
                val currentLatLng = LatLng(location.latitude, location.longitude)
                val nearestEvacCenter = getNearestEvacCenter(currentLatLng)

                if (nearestEvacCenter != null) {
                    val distance = calculateDistance(currentLatLng, nearestEvacCenter)

                    if (distance <= 150) {
                        Toast.makeText(
                            this,
                            "You are in/near the evacuation center",
                            Toast.LENGTH_SHORT
                        ).show()
                        Toast.makeText(this, "Distance: $distance Meters", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener // Do not proceed with SOS
                    } else {
                        // Proceed with sending SOS data
                        Toast.makeText(this, "Distance: $distance Meters", Toast.LENGTH_SHORT).show()
                        Toast.makeText(this, "Location Sent", Toast.LENGTH_SHORT).show()
                        sendSOSData(location)
                    }
                }
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getLatLngFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            val addressList = geocoder.getFromLocationName(address, 1)
            if (addressList != null && addressList.isNotEmpty()) {
                val location = addressList[0]
                LatLng(location.latitude, location.longitude)
            } else {
                null
            }
        } catch (e: IOException) {
            Log.e("Geocoder Error", "Unable to get location from address: $address", e)
            null
        }
    }

    private fun getNearestEvacCenter(currentLocation: LatLng): LatLng? {
        var nearestCenter: LatLng? = null
        var minDistance = Int.MAX_VALUE

        for (center in evacuationCenters) {
            val centerLatLng = getLatLngFromAddress(center.evacAddress) // Convert address to LatLng
            if (centerLatLng != null) {
                val distance = calculateDistance(currentLocation, centerLatLng)
                if (distance < minDistance) {
                    minDistance = distance
                    nearestCenter = centerLatLng
                }
            }
        }
        return nearestCenter
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Int {
        val startLocation = Location("start")
        startLocation.latitude = start.latitude
        startLocation.longitude = start.longitude

        val endLocation = Location("end")
        endLocation.latitude = end.latitude
        endLocation.longitude = end.longitude

        return startLocation.distanceTo(endLocation).toInt() // Distance in meters as Int
    }


    private fun sendSOSData(location: Location) {
        // Your existing code to send SOS data
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
        val currentAddress = addresses[0].getAddressLine(0)

        // Get the current Session or current User
        val sp = getSharedPreferences("userSession", MODE_PRIVATE)
        val fullNameData = sp.getString("residentFullName", "null")
        val residentEmail = sp.getString("residentEmail", "null")

        val fullName = fullNameData.toString()
        val email = residentEmail.toString()
        val dateLastSent = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())

        val birthdateString = sp.getString("residentBirthDate", "null")
        val birthdateCalendar = Calendar.getInstance()
        birthdateCalendar.time = SimpleDateFormat("MM/dd/yyyy").parse(birthdateString)

        val currentDateCalendar = Calendar.getInstance()

        val year1 = birthdateCalendar.get(Calendar.YEAR)
        val year2 = currentDateCalendar.get(Calendar.YEAR)
        var age = year2 - year1

        // adjust for months and days
        val month1 = birthdateCalendar.get(Calendar.MONTH)
        val month2 = currentDateCalendar.get(Calendar.MONTH)
        if (month2 < month1) {
            age--
        } else if (month2 == month1) {
            val day1 = birthdateCalendar.get(Calendar.DAY_OF_MONTH)
            val day2 = currentDateCalendar.get(Calendar.DAY_OF_MONTH)
            if (day2 < day1) {
                age--
            }
        }

        val sex = sp.getString("residentSex", "null").toString()
        val teamID = "None"
        val isFound = false

        val sos = SOS(fullName, email, currentAddress, dateLastSent, age, sex, teamID, isFound, pwdCheckedBoxes, sickCheckedBoxes, pregnantCheckedBoxes, currentSituation)
        val gson = Gson()
        val json = gson.toJson(sos)

        val requestBody = json.toRequestBody("application/json".toMediaType())

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sosAPI = retrofit.create(sosAPI::class.java)

        sosAPI.postSOS(requestBody).enqueue(object : Callback<ResponseBody> {
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
                    if (response.errorBody()!= null) {
                        Log.e("Error", "Response error body: ${response.errorBody()!!.string()}")
                    }
                }
            }
        })
    }
}