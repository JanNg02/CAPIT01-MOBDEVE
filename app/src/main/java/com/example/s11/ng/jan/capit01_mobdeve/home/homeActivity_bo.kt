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

    interface patrolTaskAPI {
        @GET("getPatrolTasks")
        fun getPatrolTasks(@Query("teamName") teamName: String): Call<patrolsData>
    }

    interface securityAPI {
        @GET("getSecurityTasks")
        fun getSecurityTasks(@Query("evacuationSecurityID") evacuationSecurityID: String): Call<securityData>
    }

    interface dispatchAPI {
        @GET("getDispatchData")
        fun getDispatchData(@Query("currentAssignment") currentAssignment: String): Call<dispatchData>
    }

    interface missingPersonTaskAPI {
        @GET("getMissingPersonTaskData")
        fun getMissingPersonTaskData(@Query("currentAssignment") currentAssignment: String): Call<missingPersonData>
    }

    interface sosTaskAPI {
        @GET("getSOSDataTask")
        fun getSOSDataTask(@Query("currentAssignment") currentAssignment: String): Call<SOSData>
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
        val items: List<currentAssignment> = teamFound.currentAssignment
        val adapter = homeBOadapter(this, items)

        listView.adapter = adapter

        listView.setOnItemClickListener { parent, view, position, id ->

            val clickedAssignment = items[position]

            val intent = Intent(this, currentassignmentBO::class.java)
            intent.putExtra("assignmentID", clickedAssignment.assignmentID)
            startActivity(intent)
        }
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

    fun placeSecurityData(securityData: securityData){
        var securityDescriptionTV : TextView = findViewById(R.id.task_description_TV)

        securityDescriptionTV.text =
            "Standby and provide security to " + securityData.evacuationSecurityArea
    }

    fun placePatrolData(patrolsData: patrolsData){
        // Check if the TextView ID is correct
        val patrolDescriptionTV: TextView? = findViewById(R.id.task_description_TV)

        // Check if patrolDescriptionTV is null before using it
        if (patrolDescriptionTV == null) {
            Log.e("Error", "patrolDescriptionTV is null. Check the ID in your XML layout.")
            return // Exit the function if the view is null
        }

        patrolDescriptionTV.text = patrolsData.patrolDescription
    }

    fun placedispatchData(dispatchData: dispatchData){
        var dispatchDescriptionTV : TextView = findViewById(R.id.task_description_TV)

        val evacName = dispatchData.evacName
        val inventoryRequests = dispatchData.evacInventoryRequested

        // Create a formatted string
        val formattedText = StringBuilder()
        formattedText.append("Deliver the Requested Items to $evacName\n\n") // Start with the evacName
        formattedText.append("The Items are: \n")
        inventoryRequests.forEach { request ->
            formattedText.append("  - Item: ${request.evacInventoryName}, Quantity: ${request.evacInventoryQuantity}\n")
        }

        dispatchDescriptionTV.text = formattedText.toString()
    }

    fun placeMissingPersonsData(missingPersonData: missingPersonData){
        var missingPersonDescriptionTV : TextView = findViewById(R.id.task_description_TV)

        val formattedText = StringBuilder()
        formattedText.append(" Find the following missing person: ${missingPersonData.missingFullName}\n") // Start with the evacName
        formattedText.append("Last found at ${missingPersonData.areaLastSeen} on ${missingPersonData.timeLastSeen}\n\n")
        formattedText.append("Sex: ${missingPersonData.sex} \n")
        formattedText.append("Age: ${missingPersonData.age} \n\n")
        formattedText.append("Description:\n")
        formattedText.append("${missingPersonData.description}\n")


        missingPersonDescriptionTV.text = formattedText.toString()
    }

    fun placeSOSData(SOSData: SOSData){
        var sosDescriptionTV : TextView = findViewById(R.id.task_description_TV)

        sosDescriptionTV.text = "Save the following person: ${SOSData.fullName} at ${SOSData.currentAddress}"
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
                        //if team is a patrol team go to retrieve patrol data
                        if(teamData.teamTasks == "Patrol") {
                            teamDataLoaded = true
                            checkIfAllDataLoaded()
                            retrievePatrolData()
                        } else if (teamData.teamTasks == "Security") { //if team is part of security data
                            teamDataLoaded = true
                            checkIfAllDataLoaded()
                            retrieveSecurityData()
                        } else if (teamData.teamTasks == "Delivery") {
                            teamDataLoaded = true
                            checkIfAllDataLoaded()
                            retrieveDispatchData()
                        }else if(teamData.currentAssignment[0].assignmentID.contains("sos")) {
                            teamDataLoaded = true
                            checkIfAllDataLoaded()
                            retrieveSOSTaskData()
                        } else if (teamData.teamTasks == "Search and Rescue") {
                            teamDataLoaded = true
                            checkIfAllDataLoaded()
                            retrieveMissingPersonTaskData()
                        }
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

    //Patrol Data to be Retrieved
    fun retrievePatrolData() {

        //loading requirements
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
                        placeTeamData() //place the team ata on the screen
                        placePatrolData(patrolData) //place the patrol data on the screen
                        saveTaskID(patrolData.patrolID)//save the taskID
                        Log.d("TASKID", patrolData.patrolID)

                        //checks for loading
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        saveTeamName(teamFound)
                    } else {
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        Toast.makeText(this@homeActivity_bo, "No Patrol Task Found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    patrolDataLoaded = true
                    checkIfAllDataLoaded()
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@homeActivity_bo, "Response is not successful", Toast.LENGTH_SHORT).show()
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

    fun retrieveSecurityData() {

        //loading requirements
        progressBar.visibility = View.VISIBLE
        loadingOverlay.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val securityAPI = retrofit.create(securityAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        val call = securityAPI.getSecurityTasks(teamFound.currentAssignment[0].assignmentID)
        Log.d("UserString", call.toString())

        call.enqueue(object : Callback<securityData> {
            override fun onResponse(call: Call<securityData>, response: Response<securityData>) {
                if (response.isSuccessful) {
                    val securityData = response.body();
                    if (securityData!= null) {
                        placeTeamData() //place the team ata on the screen
                        placeSecurityData(securityData) //place the patrol data on the screen
                        //checks for loading
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        saveTeamName(teamFound)
                    } else {
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        Toast.makeText(this@homeActivity_bo, "You are not in Security", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    patrolDataLoaded = true
                    checkIfAllDataLoaded()
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@homeActivity_bo, "Response is not successful", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<securityData>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@homeActivity_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                patrolDataLoaded = true
                checkIfAllDataLoaded()
            }
        })
    }
    fun retrieveDispatchData() {

        //loading requirements
        progressBar.visibility = View.VISIBLE
        loadingOverlay.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val dispatchAPI = retrofit.create(dispatchAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        val call = dispatchAPI.getDispatchData(teamFound.currentAssignment[0].assignmentID)
        Log.d("UserString", call.toString())

        call.enqueue(object : Callback<dispatchData> {
            override fun onResponse(call: Call<dispatchData>, response: Response<dispatchData>) {
                if (response.isSuccessful) {
                    val dispatchData = response.body();
                    if (dispatchData!= null) {
                        placeTeamData() //place the team ata on the screen
                        placedispatchData(dispatchData) //place the patrol data on the screen
                        //checks for loading
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        saveTeamName(teamFound)
                    } else {
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        Toast.makeText(this@homeActivity_bo, "No Task Assigned to your team", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    patrolDataLoaded = true
                    checkIfAllDataLoaded()
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@homeActivity_bo, "Response is not successful", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<dispatchData>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@homeActivity_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                patrolDataLoaded = true
                checkIfAllDataLoaded()
            }
        })
    }

    fun retrieveMissingPersonTaskData() {
        //loading requirements
        progressBar.visibility = View.VISIBLE
        loadingOverlay.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val missingPersonTaskAPI = retrofit.create(missingPersonTaskAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        val call = missingPersonTaskAPI.getMissingPersonTaskData(teamFound.currentAssignment[0].assignmentID)
        Log.d("UserString", call.toString())

        call.enqueue(object : Callback<missingPersonData> {
            override fun onResponse(call: Call<missingPersonData>, response: Response<missingPersonData>) {
                if (response.isSuccessful) {
                    val missingPersonTaskData = response.body();
                    if (missingPersonTaskData!= null) {
                        placeTeamData() //place the team ata on the screen
                        placeMissingPersonsData(missingPersonTaskData) //place the patrol data on the screen
                        //checks for loading
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        saveTeamName(teamFound)
                    } else {
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        Toast.makeText(this@homeActivity_bo, "No Missing Person to look for assigned to your team", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    patrolDataLoaded = true
                    checkIfAllDataLoaded()
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@homeActivity_bo, "Response is not successful", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<missingPersonData>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@homeActivity_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                patrolDataLoaded = true
                checkIfAllDataLoaded()
            }
        })
    }
    fun retrieveSOSTaskData() {
        //loading requirements
        progressBar.visibility = View.VISIBLE
        loadingOverlay.visibility = View.VISIBLE

        val retrofit = Retrofit.Builder()
            .baseUrl("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val sosTaskAPI = retrofit.create(sosTaskAPI::class.java)

        val baseUrl = retrofit.baseUrl().toString()
        Log.d("Base URL", baseUrl)

        val call = sosTaskAPI.getSOSDataTask(teamFound.currentAssignment[0].assignmentID)
        Log.d("UserString", call.toString())

        call.enqueue(object : Callback<SOSData> {
            override fun onResponse(call: Call<SOSData>, response: Response<SOSData>) {
                if (response.isSuccessful) {
                    val SOSTaskData = response.body();
                    if (SOSTaskData!= null) {
                        placeTeamData() //place the team ata on the screen
                        placeSOSData(SOSTaskData) //place the patrol data on the screen
                        //checks for loading
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        saveTeamName(teamFound)
                    } else {
                        patrolDataLoaded = true
                        checkIfAllDataLoaded()
                        Toast.makeText(this@homeActivity_bo, "No SOS request assigned to your team", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    patrolDataLoaded = true
                    checkIfAllDataLoaded()
                    Log.e("Error", "Response code: ${response.code()}")
                    Log.e("Error", "Response error message: ${response.message()}")
                    Toast.makeText(this@homeActivity_bo, "Response is not successful", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<SOSData>, t: Throwable) {
                Log.e("Error", "Error: ${t.message}")
                Toast.makeText(this@homeActivity_bo, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                patrolDataLoaded = true
                checkIfAllDataLoaded()
            }
        })
    }
    private fun checkIfAllDataLoaded() { //checks if loading is done
        if (teamDataLoaded && patrolDataLoaded) {
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

    fun saveTaskID(taskID: String){
        val sp = getSharedPreferences("saveTaskID", MODE_PRIVATE)
        val editor = sp.edit()
        editor.putString("taskID", taskID)
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