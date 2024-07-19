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
import com.example.s11.ng.jan.capit01_mobdeve.help.SOS
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
import kotlin.reflect.typeOf

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
    @SerializedName("evacuationSecurityID") val evacuationSecurityID: Int,
    @SerializedName("evacuationSecurityArea") val evacuationSecurityArea: String,
    @SerializedName("teamName") val teamName: List<String>,
    @SerializedName("dateCreated") val dateCreated: String,
)

data class evacInventoryRequested(
    @SerializedName("itemID") val itemID: String,
    @SerializedName("evacInventoryName") val evacInventoryName: String,
    @SerializedName("evacInventoryQuantity") val evacInventoryQuantity: String,
    @SerializedName("itemReceived") val itemReceived: String,
    @SerializedName("evacInventoryCategory") val evacInventoryCategory: String,
)

data class dispatchData(
    @SerializedName("dispatchID") val dispatchID: String,
    @SerializedName("evacName") val evacName: String,
    @SerializedName("evacInventoryRequested") val evacInventoryRequested: List<evacInventoryRequested>,
    @SerializedName("dispatchStatus") val dispatchStatus: String,
    @SerializedName("dateRequested") val dateRequested: String,
    @SerializedName("teamAssigned") val teamAssigned: String,
    @SerializedName("itemsOversawBy") val itemsOversawBy: String,
)

data class missingPersonData(
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

data class SOSData(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("currentAddress") val currentAddress: String,
    @SerializedName("dateLastSent") val dateLastSent: String,
    @SerializedName("age") val age: Int,
    @SerializedName("teamID") val teamID: String,
    @SerializedName("isFound") val isFound: Boolean
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

        //loading requirements
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

    fun placeSecurityData(securityData: securityData){
        var securityDescriptionTV : TextView = findViewById(R.id.task_description_TV)

        securityDescriptionTV.text =
            "Standby and provide security to " + securityData.evacuationSecurityArea
    }

    fun placePatrolData(patrolsData: patrolsData){
        var patrolDescriptionTV : TextView = findViewById(R.id.task_description_TV)

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
                        }else if(teamData.currentAssignment.lowercase().contains("sos")) {
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

        val call = securityAPI.getSecurityTasks(teamFound.currentAssignment)
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

        val call = dispatchAPI.getDispatchData(teamFound.currentAssignment)
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

        val call = missingPersonTaskAPI.getMissingPersonTaskData(teamFound.currentAssignment)
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

        val call = sosTaskAPI.getSOSDataTask(teamFound.currentAssignment)
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
}