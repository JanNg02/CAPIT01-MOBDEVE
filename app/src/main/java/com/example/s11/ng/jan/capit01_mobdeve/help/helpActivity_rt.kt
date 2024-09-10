package com.example.s11.ng.jan.capit01_mobdeve.help

//import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt
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


data class SOS(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("currentAddress") val currentAddress: String,
    @SerializedName("dateLastSent") val dateLastSent: String,
    @SerializedName("age") val age: Int,
    @SerializedName("teamID") val teamID: String,
    @SerializedName("isFound") val isFound: Boolean
)

class helpActivity_rt : AppCompatActivity() {
    interface sosAPI {
        @POST("sendSOS")
        fun postSOS(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_rt)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        var sosButton : ImageButton = findViewById(R.id.imageButton)

        sosButton.setOnClickListener{
            if (checkPermission()) {
                getCurrentLocation()
            } else {
                requestPermission()
            }
        }

        setCheckBoxes()

        setupFooter_rt() // Call the footer setup function
    }

    private fun setCheckBoxes(){
        val pwdCheckbox: CheckBox = findViewById(R.id.pwd_checkbox)
        val sickCheckbox: CheckBox = findViewById(R.id.sick_checkbox)
        val pregnantCheckbox: CheckBox = findViewById(R.id.pregnant_checkbox)
        val pwdDropdown: LinearLayout= findViewById(R.id.pwd_dropdown)
        val sickDropdown: LinearLayout = findViewById(R.id.sick_dropdown)
        val pregnantRadiogroup: RadioGroup = findViewById(R.id.pregnant_radiogroup)

        pwdCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                pwdDropdown.setVisibility(View.VISIBLE)
            } else {
                pwdDropdown.setVisibility(View.GONE)
                uncheckPwdCheckboxes()
            }
        }

        sickCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                sickDropdown.setVisibility(View.VISIBLE)
            } else {
                sickDropdown.setVisibility(View.GONE)
                uncheckSickCheckboxes()
            }
        }

        pregnantCheckbox.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                pregnantRadiogroup.setVisibility(View.VISIBLE)
            } else {
                pregnantRadiogroup.setVisibility(View.GONE)
                uncheckPregnantRadiobuttons()
            }
        }
    }

    private fun uncheckPwdCheckboxes() {
        findViewById<CheckBox>(R.id.hearing_impairment_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.vision_impairment_checkbox).isChecked = false
        // Add more checkboxes for other PWD options
    }

    private fun uncheckSickCheckboxes() {
        findViewById<CheckBox>(R.id.diabetes_checkbox).isChecked = false
        findViewById<CheckBox>(R.id.heart_problems_checkbox).isChecked = false
        // Add more checkboxes for other sick options

        findViewById<EditText>(R.id.other_sick_edittext).text.clear()
    }

    private fun uncheckPregnantRadiobuttons() {
        findViewById<RadioGroup>(R.id.pregnant_radiogroup).clearCheck()
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
                val geocoder = Geocoder(this, Locale.getDefault())
                val addresses: List<Address> =
                    geocoder.getFromLocation(location.latitude, location.longitude, 1)!!
                val currentAddress = addresses[0].getAddressLine(0)

                //get the currest Session or current User
                val sp = getSharedPreferences("userSession", MODE_PRIVATE)
                val fullNameData = sp.getString("residentFullName", "null")
                val residentEmail = sp.getString("residentEmail", "null")

                val fullName = fullNameData.toString()
                val email = residentEmail.toString()
                val dateLastSent = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
                val age = 10
                val teamID = "None"
                val isFound = false

                val sos = SOS(fullName, email, currentAddress, dateLastSent, age, teamID, isFound)
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
                Toast.makeText(this, "Location Sent", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Unable to get current location", Toast.LENGTH_SHORT).show()
            }
        }
    }
}