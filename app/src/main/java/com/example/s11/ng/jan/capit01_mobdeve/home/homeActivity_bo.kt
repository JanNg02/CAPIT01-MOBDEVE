package com.example.s11.ng.jan.capit01_mobdeve.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.missing.missingActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.fingerprint.fingerprintActivity_bo
import com.example.s11.ng.jan.capit01_mobdeve.login.loginAPI
import com.example.s11.ng.jan.capit01_mobdeve.login.userInfo
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import com.google.gson.reflect.TypeToken
import okhttp3.RequestBody
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

data class teamData(
    @SerializedName("teamName") val teamName: String,
    @SerializedName("teamMembers") val teamMembers: List<String>,
    @SerializedName("teamStatus") val teamStatus: Boolean ,
    @SerializedName("teamTasks") val teamTasks: String,
    @SerializedName("currentAssignment") val currentAssignment: String,
    @SerializedName("teamArea") val teamArea: String,
    @SerializedName("teamID") val teamID: Int
)

data class patrolsData(
    @SerializedName("patrolArea") val patrolArea: String,
    @SerializedName("patrolDescription") val patrolDescription: String,
    @SerializedName("dateCreated") val dateCreated: String,
    @SerializedName("teamName") val teamName: String,
    @SerializedName("patrolID") val patrolID: Int,
)

data class securityData(
    @SerializedName("patrolArea") val patrolArea: String,
    @SerializedName("patrolDescription") val patrolDescription: String,
    @SerializedName("dateCreated") val dateCreated: String,
    @SerializedName("teamName") val teamName: String,
    @SerializedName("patrolID") val patrolID: Int,
)

class homeActivity_bo : AppCompatActivity(){

    interface teamDataAPI {
        @GET("getTeamTasks")
        fun getTeamTasks(@Query("userName") userName: String): Call<teamData>
    }

    interface patrolTaskAPI {
        @GET("getPatrolTasks")
        fun getPatrolTasks(@Query("teamName") teamName: String): Call<patrolsData>
    }

    private lateinit var teamFound: teamData
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingOverlay: View
    private var teamDataLoaded = false
    private var patrolDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_bo)

        loadingOverlay = findViewById(R.id.loading_overlay)
        progressBar = findViewById(R.id.loading_progress_bar)
        progressBar.visibility = View.VISIBLE

        retrieveTeamData()

        setupFooter_bo() // Call the footer setup function
    }

    fun placeTeamData(){
        val teamMembersString = teamFound.teamMembers.joinToString(", ")
        var teamTitleTV : TextView = findViewById(R.id.teamDescriptionTitle_TV)
        var teamTaskTV : TextView = findViewById(R.id.teamTask_TV)
        var teamMembersTV : TextView = findViewById(R.id.teamMembers_TV)

        teamTitleTV.text = "Team Name: "+teamFound.teamName
        teamTaskTV.text = teamFound.teamTasks + " " + teamFound.teamArea
        teamMembersTV.text = teamMembersString
    }

    fun noTeamData(){
        var teamTitleTV : TextView = findViewById(R.id.teamDescriptionTitle_TV)
        var teamTaskTV : TextView = findViewById(R.id.teamTask_TV)
        var teamTask : TextView = findViewById(R.id.teamTask)
        var teamMembersTV : TextView = findViewById(R.id.teamMembers_TV)
        var teamMembers: TextView = findViewById(R.id.teamMembers)
        var teamDescriptionTV : TextView = findViewById(R.id.task_description_TV)
        var teamDescription : TextView = findViewById(R.id.task_description)

        teamTitleTV.text = "No Team Found"
        teamTaskTV.visibility = View.GONE
        teamMembersTV.visibility = View.GONE
        teamDescriptionTV.visibility = View.GONE
        teamDescription.visibility = View.GONE
        teamTask.visibility = View.GONE
        teamMembers.visibility = View.GONE
    }

    fun placePatrolData(patrolData: patrolsData){
        var patrolDescriptionTV : TextView = findViewById(R.id.task_description_TV)

        patrolDescriptionTV.text = patrolData.patrolDescription
    }

    //Check Team
    fun retrieveTeamData() {

        progressBar.visibility = View.VISIBLE
        loadingOverlay.visibility = View.VISIBLE

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

        call.enqueue(object : Callback<teamData> {
            override fun onResponse(call: Call<teamData>, response: Response<teamData>) {
                if (response.isSuccessful) {

                    val teamData = response.body();

                    if (teamData!= null) {
                        teamFound = teamData
                        Log.d("teamData", teamData.toString())
                        //if team is a patrol team go to retrieve patrol data
                        if(teamData.teamTasks == "Patrol") {
                            teamDataLoaded = true
                            checkIfAllDataLoaded()
                            retrievePatrolData()
                        }
                    } else {
                        teamDataLoaded = true
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        noTeamData()
                        Toast.makeText(this@homeActivity_bo, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    teamDataLoaded = true
                    checkIfAllDataLoaded()
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@homeActivity_bo, "Invalid username or password or NULL", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<teamData>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@homeActivity_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                teamDataLoaded = true
                checkIfAllDataLoaded()
            }
        })
    }

    //Patrol Data to be Retrieved
    fun retrievePatrolData() {

        progressBar.visibility = View.VISIBLE
        loadingOverlay.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val patrolAPI = retrofit.create(patrolTaskAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        val call = patrolAPI.getPatrolTasks(teamFound.teamName)
        Log.d("UserString", call.toString())

        call.enqueue(object : Callback<patrolsData> {
            override fun onResponse(call: Call<patrolsData>, response: Response<patrolsData>) {
                if (response.isSuccessful) {
                    val patrolData = response.body();
                    if (patrolData!= null) {
                        placeTeamData()
                        placePatrolData(patrolData)
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        Log.d("patrolData", patrolData.toString())
                        Toast.makeText(this@homeActivity_bo,"yehey " + patrolData.patrolDescription, Toast.LENGTH_SHORT).show()
                    } else {
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        Toast.makeText(this@homeActivity_bo, "Invalid username or password", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    patrolDataLoaded = true
                    checkIfAllDataLoaded()
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@homeActivity_bo, "Invalid username or password or NULL", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<patrolsData>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@homeActivity_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                patrolDataLoaded = true
                checkIfAllDataLoaded()
            }
        })
    }

    private fun checkIfAllDataLoaded() {
        if (teamDataLoaded && patrolDataLoaded) {
            progressBar.visibility = View.GONE
            loadingOverlay.visibility = View.GONE
        }
    }
}