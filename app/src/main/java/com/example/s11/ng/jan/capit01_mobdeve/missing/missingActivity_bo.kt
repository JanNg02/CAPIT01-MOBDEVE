package com.example.s11.ng.jan.capit01_mobdeve.missing

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.fingerprint.fingerprintActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.login.loginAPI
import com.example.s11.ng.jan.capit01_mobdeve.login.login_act
import com.example.s11.ng.jan.capit01_mobdeve.login.userInfo
import com.example.s11.ng.jan.capit01_mobdeve.missing.missingActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.rescue.rescueActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class missingPersonData(
    @SerializedName("age") val residentID : Int,
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

interface getMissingPersonAPI {
    @GET("getMissingPersons")
    fun getMissingPeople(): Call<List<missingPersonData>>
}

class missingActivity_bo : AppCompatActivity(){

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: MissingPersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.missing_bo)

        recyclerView = findViewById(R.id.getMissingPersons_RV)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Call the retrieveData function and pass the result to the adapter
        retrieveData { missingPersonData ->
            adapter = MissingPersonAdapter(missingPersonData)
            recyclerView.adapter = adapter
            adapter.notifyDataSetChanged() // Notify the adapter that the data has changed
        }

        val logoutButton: FloatingActionButton = findViewById(R.id.logout_button_BO)
        logoutButton.setOnClickListener{
            logout()
        }

        setupFooter_bo() // Call the footer setup function
    }

    fun retrieveData(callback: (List<missingPersonData>) -> Unit) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val getMissingPerson = retrofit.create(getMissingPersonAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        val call = getMissingPerson.getMissingPeople()

        call.enqueue(object : Callback<List<missingPersonData>> {
            override fun onResponse(call: Call<List<missingPersonData>>, response: Response<List<missingPersonData>>) {
                if (response.isSuccessful) {
                    val missingPersonData = response.body()
                    if (missingPersonData != null) {
                        callback(missingPersonData)
                    } else {
                        Toast.makeText(this@missingActivity_bo, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@missingActivity_bo, "Invalid username or password or NULL", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<List<missingPersonData>>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@missingActivity_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun moveToLogin(){
        val intent = Intent(applicationContext, login_act::class.java)
        startActivity(intent)
        finish()
    }

    fun logout(){
        val sp = getSharedPreferences("userSession", MODE_PRIVATE)
        val editor = sp.edit()

        editor.clear()
        editor.apply()
        moveToLogin()
    }
}