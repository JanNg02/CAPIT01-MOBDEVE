package com.example.s11.ng.jan.capit01_mobdeve.dashboard

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.missing.MissingPersonAdapter
import com.example.s11.ng.jan.capit01_mobdeve.missing.getMissingPersonAPI
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_rt
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.net.URLEncoder

data class missingPersonDataReported(
    @SerializedName("age") val age : Int,
    @SerializedName("areaLastSeen") val areaLastSeen : String,
    @SerializedName("contactNum") val contactNum : String,
    @SerializedName("dateSubmitted") val dateSubmitted : String,
    @SerializedName("description") val description : String,
    @SerializedName("filedBy") val filedBy : String,
    @SerializedName("isFound") val isFound : Boolean,
    @SerializedName("missingFullName") val missingFullName : String,
    @SerializedName("sex") val sex : String,
    @SerializedName("teamdID") val teamID : String,
    @SerializedName("timeLastSeen") val timeLastSeen : String,
    @SerializedName("miaID") val miaID : String,
)

interface getUserMissingReportAPI {
    @GET("getUserMissingReports")
    fun getUserMissingReport(@Query("userName") userName: String): Call<List<missingPersonDataReported>>
}
class reportedMissing_rt : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MissingPersonReportedAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reported_missing_rt)

        recyclerView = findViewById(R.id.getMissingPersons_RV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Call the retrieveData function and pass the result to the adapter
        retrieveData { missingPersonData ->
            adapter = MissingPersonReportedAdapter(missingPersonData)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
        }

        setupFooter_rt()

    }

    fun retrieveData(callback: (List<missingPersonDataReported>) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getMissingPerson = retrofit.create(getUserMissingReportAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)


        val sp = getSharedPreferences("userSession", MODE_PRIVATE)
        val userName = sp.getString("residentFullName", "null")
        //val encodedUserName = URLEncoder.encode(userName, "UTF-8")

        //Log.e("userName", "Response code: $encodedUserName")

        val call = getMissingPerson.getUserMissingReport(userName.toString())

        call.enqueue(object :
            Callback<List<missingPersonDataReported>> {
            override fun onResponse(call: Call<List<missingPersonDataReported>>, response: Response<List<missingPersonDataReported>>) {
                if (response.isSuccessful) {
                    val userMissingPersonData = response.body()
                    if (userMissingPersonData != null) {
                        callback(userMissingPersonData)
                    } else {
                        Toast.makeText(this@reportedMissing_rt, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@reportedMissing_rt, "Invalid username or password or NULL", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<missingPersonDataReported>>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@reportedMissing_rt, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}