package com.example.s11.ng.jan.capit01_mobdeve.file

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.map.mapActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.fetchPost
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt
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
    @SerializedName("teamID") val teamID: String,
    @SerializedName("isFound") val isFound: Boolean
)

class fileActivity_rt : AppCompatActivity(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var fullNameEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var areaLastSeenEditText: EditText
    private lateinit var timeLastSeenEditText: EditText
    private lateinit var ageEditText: EditText
    private lateinit var sexRadioGroup: RadioGroup
    private lateinit var maleRadioButton: RadioButton
    private lateinit var femaleRadioButton: RadioButton
    private lateinit var submitButton: Button

    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    interface MissingApi {
        @POST("postMissing")
        fun postMissing(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.file_rt)

        swipeRefreshLayout = findViewById(R.id.refreshFileMissingPerson)
        swipeRefreshLayout.setOnRefreshListener(this)

        fileMissingPerson()

        setupFooter_rt() // Call the footer setup function
    }

    private fun fileMissingPerson() {
        fullNameEditText = findViewById(R.id.missingFullName)
        descriptionEditText = findViewById(R.id.description)
        areaLastSeenEditText = findViewById(R.id.areaLastSeen)
        timeLastSeenEditText = findViewById(R.id.timeLastSeen)
        ageEditText = findViewById(R.id.age)

        sexRadioGroup = findViewById(R.id.sexRadioGroup)
        maleRadioButton = findViewById(R.id.maleRadioButton)
        femaleRadioButton = findViewById(R.id.femaleRadioButton)

        val editTexts = listOf(fullNameEditText, descriptionEditText, areaLastSeenEditText, timeLastSeenEditText, ageEditText)

        submitButton = findViewById(R.id.filesubmitRT)

        submitButton.setOnClickListener {
            val fullName = fullNameEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val areaLastSeen = areaLastSeenEditText.text.toString()
            val timeLastSeen = timeLastSeenEditText.text.toString()
            val age = ageEditText.text.toString().toIntOrNull()
            val sex: String

            //get the currest Session or current User
            val sp = getSharedPreferences("userSession", MODE_PRIVATE)
            val fullNameData = sp.getString("residentFullName", "null")
            val residentContactNumber = sp.getString("residentContactNumber", "null")

            val dateSubmitted = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date())
            val filedBy = fullNameData.toString() //Add Sessioned User
            val contactNum = residentContactNumber.toString() //Add Sessioned User
            val teamID = "None"
            val isFound = false

            if (fullName.isEmpty() || description.isEmpty() || areaLastSeen.isEmpty() || timeLastSeen.isEmpty() || age == null) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            when (sexRadioGroup.checkedRadioButtonId) {
                R.id.maleRadioButton -> sex = "Male"
                R.id.femaleRadioButton -> sex = "Female"
                else -> {
                    Toast.makeText(this, "Please select a sex", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val missing = Missing(fullName, description, areaLastSeen, timeLastSeen, age, sex, dateSubmitted, filedBy, contactNum, teamID, isFound)
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

    override fun onRefresh() {
        fullNameEditText.text.clear()
        descriptionEditText.text.clear()
        areaLastSeenEditText.text.clear()
        timeLastSeenEditText.text.clear()
        ageEditText.text.clear()

        maleRadioButton.isChecked = false
        femaleRadioButton.isChecked = false

        swipeRefreshLayout.isRefreshing = false // Reset the refresh indicator
    }
}

