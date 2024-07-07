package com.example.s11.ng.jan.capit01_mobdeve.file

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.map.mapActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.google.gson.Gson
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSyntaxException
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType
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
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Missing(
    @SerializedName("missingFullName") val missingFullName: String,
    @SerializedName("description") val description: String,
    @SerializedName("areaLastSeen") val areaLastSeen: String,
    @SerializedName("timeLastSeen") val timeLastSeen: String,
    @SerializedName("age") val age: Int,
    @SerializedName("sex") val sex: String,
    @SerializedName("dateSubmitted") val dateSubmitted: String,
    @SerializedName("filedBy") val filedBy: String,
    @SerializedName("contactNum") val contactNum: String,
    @SerializedName("teamID") val teamID: String
)

class fileActivity_rt : AppCompatActivity() {

    private lateinit var fullNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var areaLastSeenEditText: EditText
    private lateinit var timeLastSeenEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var sexEditText: EditText
    private lateinit var submitButton: Button

    interface MissingApi {
        @POST("postMissing")
        fun postMissing(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_rt)

        val pnabutton: ImageButton = findViewById(R.id.pna_RT)
        pnabutton.setOnClickListener {
            moveToPnaRT()
        }

        val mapbutton: ImageButton = findViewById(R.id.map_RT)
        mapbutton.setOnClickListener {
            moveToMapRT()
        }

        val helpbutton: ImageButton = findViewById(R.id.help_RT)
        helpbutton.setOnClickListener {
            moveToHelpRT()
        }

        val filebutton: ImageButton = findViewById(R.id.file_RT)
        filebutton.setOnClickListener {
            moveToFileRT()
        }

        val dashbutton: ImageButton = findViewById(R.id.dashboard_RT)
        dashbutton.setOnClickListener {
            moveToDashboardRT()
        }


        fullNameEditText = findViewById(R.id.missingFullName)
        descriptionEditText = findViewById(R.id.description)
        areaLastSeenEditText = findViewById(R.id.areaLastSeen)
        timeLastSeenEditText = findViewById(R.id.timeLastSeen)
        ageEditText = findViewById(R.id.age)
        sexEditText = findViewById(R.id.sex)

        val editTexts = listOf(fullNameEditText, descriptionEditText, areaLastSeenEditText, timeLastSeenEditText, ageEditText, sexEditText)

        submitButton = findViewById(R.id.filesubmitRT)

        submitButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val areaLastSeen = areaLastSeenEditText.text.toString()
            val timeLastSeen = timeLastSeenEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()
            val sex = sexEditText.text.toString()

            //get the currest Session or current User
            val sp = getSharedPreferences("userSession", MODE_PRIVATE)
            val fullNameData = sp.getString("residentFullName", "null")
            val residentContactNumber = sp.getString("residentContactNumber", "null")

            val dateSubmitted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val filedBy = fullNameData.toString() //Add Sessioned User
            val contactNum = residentContactNumber.toString() //Add Sessioned User
            val teamID = "None"

            if (fullName.isEmpty() || description.isEmpty() || areaLastSeen.isEmpty() || timeLastSeen.isEmpty() || age == null || sex.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val missing = Missing(fullName, description, areaLastSeen, timeLastSeen, age, sex, dateSubmitted, filedBy, contactNum, teamID)
            val gson = Gson()
            val json = gson.toJson(missing)

            val requestBody = json.toRequestBody("application/json".toMediaType())


            val retrofit = Retrofit.Builder()
                .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val missingApi = retrofit.create(MissingApi::class.java)

            Toast.makeText(this, "Missing Person Details Sent", Toast.LENGTH_SHORT).show()
            editTexts.forEach { it.text.clear() }

            val call = missingApi.postMissing(requestBody)
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
                        if (responseBody!= null) {
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
    fun moveToPnaRT(){
        val intent = Intent(applicationContext, homeActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToMapRT(){
        val intent = Intent(applicationContext, mapActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToHelpRT(){
        val intent = Intent(applicationContext, helpActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToFileRT(){
        val intent = Intent(applicationContext, fileActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToDashboardRT(){
        val intent = Intent(applicationContext, dashboardActivity_rt::class.java)
        startActivity(intent)
        finish()
    }
}
//
//        val fileRTbutton: Button = findViewById(R.id.filesubmitRT)
//        fileRTbutton.setOnClickListener{
//            val missingFullName = editTextMissingFullName.text.toString()
//            val description = editTextDescription.text.toString()
//            val areaLastSeen = editTextAreaLastSeen.text.toString()
//            val timeLastSeen = editTextTimeLastSeen.text.toString()
//            var age: Int? = null
//            val ageString = editTextAge.text.toString()
//            if (ageString.isNotEmpty()) {
//                try {
//                    age = ageString.toInt()
//                } catch (e: NumberFormatException) {
//                    // Handle the case where the user enters a non-numeric value
//                    Toast.makeText(this, "Please enter a valid age", Toast.LENGTH_SHORT).show()
//                    return@setOnClickListener
//                }
//            } else {
//                Toast.makeText(this, "Please enter an age", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }
//
//            val uploadMissing = uploadMissing(missingFullName, description, areaLastSeen, timeLastSeen, age!!, this)
//            uploadMissing.execute()
//        }
