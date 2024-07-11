package com.example.s11.ng.jan.capit01_mobdeve.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class userInfo(
    //@SerializedName("_id") val _id : ObjectId,
    @SerializedName("residentID") val residentID : String,
    @SerializedName("residentFirstName") val residentFirstName: String,
    @SerializedName("residentLastName") val residentLastName: String,
    @SerializedName("residentFullName") val residentFullName: String,
    @SerializedName("residentSex") val residentSex: String,
    @SerializedName("residentBirthDate") val residentBirthDate: String,
    @SerializedName("residentAddress") val residentAddress: String,
    @SerializedName("residentEmail") val residentEmail: String,
    @SerializedName("residentCivilStatus") val residentCivilStatus: String,
    @SerializedName("residentContactNumber") val residentContactNumber: String,
    @SerializedName("residentEmergencyContactName") val residentEmergencyContactName: String,
    @SerializedName("residentEmergencyContactNumber") val residentEmergencyContactNumber: String,
    @SerializedName("residentIsActive") val residentIsActive: String,
    @SerializedName("typeOfUser") val typeOfUser: String,
    //@SerializedName("__v") val __v : BsonInt32,
)

interface loginAPI {
    @GET("loginCheck")
    fun getUser(@Query("userString") userString: String): Call<userInfo>
}
class login_act : AppCompatActivity() {

    companion object {
        const val usernameKey: String = "nameKey"
        const val passKey: String = "passKey"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_rt)

        val loginRTbutton: Button = findViewById(R.id.loginRT)

        loginRTbutton.setOnClickListener {
            val username: EditText = findViewById(R.id.usernameRT)
            val userString = username.text.toString()
            //val password: EditText = findViewById(R.id.passwordRT)
            //val passwordString = password.text.toString()

//            if(userString.isEmpty()) {
//                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
//                return@setOnClickListener
//            }

            //moveToHomeRT()
            loginUser(userString)
        }
        val loginBObutton: Button = findViewById(R.id.loginBO)
        loginBObutton.setOnClickListener {
            moveToHomeBO()
        }
    }

    fun loginUser(userString: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val loginAPI = retrofit.create(loginAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        //val requestBody = userString.toRequestBody("text/plain".toMediaType())
        val call = loginAPI.getUser(userString)
        Log.d("UserString", call.toString())

        call.enqueue(object : Callback<userInfo> {
            override fun onResponse(call: Call<userInfo>, response: Response<userInfo>) {
                if (response.isSuccessful) {
                    val userInfo = response.body();
                    if (userInfo!= null) {
                        writeUser(userInfo)
                        if(userInfo.typeOfUser == "Resident") {
                            moveToHomeRT()
                        } else {
                            moveToHomeBO()
                        }
                        Toast.makeText(this@login_act,"yehey" + userInfo.residentFirstName, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@login_act, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@login_act, "Invalid username or password or NULL", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<userInfo>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@login_act, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun writeUser(userInfo: userInfo){
        val editor = getSharedPreferences("userSession", MODE_PRIVATE).edit()
        editor.putString("residentID", userInfo.residentID)
        editor.putString("residentFirstName", userInfo.residentFirstName)
        editor.putString("residentLastName", userInfo.residentLastName)
        editor.putString("residentFullName", userInfo.residentFullName)
        editor.putString("residentSex", userInfo.residentSex)
        editor.putString("residentBirthDate", userInfo.residentBirthDate)
        editor.putString("residentAddress", userInfo.residentAddress)
        editor.putString("residentEmail", userInfo.residentEmail)
        editor.putString("residentCivilStatus", userInfo.residentCivilStatus)
        editor.putString("residentContactNumber", userInfo.residentContactNumber)
        editor.putString("residentEmergencyContactName", userInfo.residentEmergencyContactName)
        editor.putString("residentEmergencyContactNumber", userInfo.residentEmergencyContactNumber)
        editor.putString("residentIsActive", userInfo.residentIsActive)
        editor.putString("typeOfUser", userInfo.typeOfUser)
        editor.apply()
    }

    fun moveToHomeRT(){
        val intent = Intent(this, homeActivity_rt::class.java)
        startActivity(intent)
        finish()
    }

    fun moveToHomeBO(){
        val intent = Intent(this, homeActivity_bo::class.java)
        startActivity(intent)
        finish()
    }
}