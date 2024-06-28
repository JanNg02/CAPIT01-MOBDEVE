package com.example.s11.ng.jan.capit01_mobdeve.help

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.map.mapActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.file.Missing
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.HeaderMap
import retrofit2.http.Headers
import retrofit2.http.POST
import java.net.URLEncoder

data class SOS(
    @SerializedName("missingFullName") val missingFullName: String,
    @SerializedName("description") val description: String,
    @SerializedName("areaLastSeen") val areaLastSeen: String,
    @SerializedName("timeLastSeen") val timeLastSeen: String,
    @SerializedName("age") val age: Int
)

class helpActivity_rt : AppCompatActivity() {
    interface sosAPI {
        @POST("sendSOS")
        fun postSOS(@Body requestBody: RequestBody): Call<ResponseBody>
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.help_rt)

        var sosButton : ImageButton = findViewById(R.id.imageButton)

        sosButton.setOnClickListener{
            val fullName = "TestName"
            val description = "Description"
            val areaLastSeen = "Never Seen"
            val timeLastSeen = "Last Time"
            val age = 10

            val sos = SOS(fullName, description, areaLastSeen, timeLastSeen, age)
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

        val pnabutton: ImageButton = findViewById(R.id.pna_RT)
        pnabutton.setOnClickListener{
            moveToPnaRT()
        }

        val mapbutton: ImageButton = findViewById(R.id.map_RT)
        mapbutton.setOnClickListener{
            moveToMapRT()
        }

        val helpbutton: ImageButton = findViewById(R.id.help_RT)
        helpbutton.setOnClickListener{
            moveToHelpRT()
        }

        val filebutton: ImageButton = findViewById(R.id.file_RT)
        filebutton.setOnClickListener{
            moveToFileRT()
        }

        val dashbutton: ImageButton = findViewById(R.id.dashboard_RT)
        dashbutton.setOnClickListener{
            moveToDashboardRT()
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