package com.example.s11.ng.jan.capit01_mobdeve.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.adapter.homeBOadapter
import com.example.s11.ng.jan.capit01_mobdeve.login.login_act
import com.example.s11.ng.jan.capit01_mobdeve.setupFooter_bo
import com.example.s11.ng.jan.capit01_mobdeve.update.update_bo
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class teamData(
    @SerializedName("teamName") val teamName: String,
    @SerializedName("teamMembers") val teamMembers: List<String>,
    @SerializedName("teamStatus") val teamStatus: Boolean ,
    @SerializedName("teamTasks") val teamTasks: String,
    @SerializedName("currentAssignment") val currentAssignment: List<currentAssignment>,
    @SerializedName("teamArea") val teamArea: String,
    @SerializedName("teamID") val teamID: Int
)

data class currentAssignment(
    @SerializedName("assignmentID") val assignmentID: String,
    @SerializedName("assignmentDetails") val assignmentDetails: String
)

class homeActivity_bo : AppCompatActivity(){

    private lateinit var listView: ListView

    interface teamDataAPI {
        @GET("getTeamTasks")
        fun getTeamTasks(@Query("userName") userName: String): Call<teamData>
    }

    private lateinit var teamFound: teamData
    private lateinit var progressBar: ProgressBar
    private lateinit var loadingOverlay: View
    private var teamDataLoaded = false
    private var patrolDataLoaded = false

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_bo)

        listView = findViewById<ListView>(R.id.lvHome_BO)

        //loading requirements
        loadingOverlay = findViewById(R.id.loading_overlay)
        progressBar = findViewById(R.id.loading_progress_bar)
        progressBar.visibility = View.VISIBLE

        retrieveTeamData()

        val logoutButton: FloatingActionButton = findViewById(R.id.logout_button_BO)
        logoutButton.setOnClickListener{
            logout()
        }

        setupFooter_bo() // Call the footer setup function
    }

    fun placeTeamData(){
        val items: List<String> = teamFound.currentAssignment.map { it.assignmentDetails }
        val adapter = homeBOadapter(this, items)

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->

            //val clickedAssignment = items[position]
            val assignmentID = teamFound.currentAssignment[position].assignmentID
            saveCurrentAssignment(assignmentID)

            val intent = Intent(this, currentassignmentBO::class.java)
            startActivity(intent)
        }
    }

    fun noTeamData(){

        val items = listOf("no team assigned")
        val adapter = homeBOadapter(this, items)

        listView.adapter = adapter
    }

    //Check Team
    fun retrieveTeamData() {

        //loading requirements
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
                        teamDataLoaded = true
                        placeTeamData()
                        checkIfAllDataLoaded()
                    } else {
                        teamDataLoaded = true
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        noTeamData()
                        Toast.makeText(this@homeActivity_bo, "Not Assigned to a Team", Toast.LENGTH_SHORT).show()
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

    private fun checkIfAllDataLoaded() { //checks if loading is done
        if (teamDataLoaded) {
            progressBar.visibility = View.GONE
            loadingOverlay.visibility = View.GONE
        }
    }

    private fun saveTeamName (team: teamData){
        val sp = getSharedPreferences("userSession", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("teamName", teamFound.teamName)
        editor.apply()
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

    fun saveCurrentAssignment(taskID: String){
        val sp = getSharedPreferences("saveCurrentAssignment", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("assignmentID", taskID)
        editor.putInt("teamID", teamFound.teamID)
        editor.apply()
    }
}
private fun AppCompatActivity.navigateTo(destinationActivity: Class<*>) {
    if (this::class.java != destinationActivity) {
        val intent = Intent(applicationContext, destinationActivity)
        startActivity(intent)
        finish()
    }
}