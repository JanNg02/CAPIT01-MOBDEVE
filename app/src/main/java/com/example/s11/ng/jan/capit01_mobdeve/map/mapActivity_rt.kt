package com.example.s11.ng.jan.capit01_mobdeve.map

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Geocoder
import android.os.Bundle
import android.os.Looper
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.s11.ng.jan.capit01_mobdeve.R
import com.example.s11.ng.jan.capit01_mobdeve.help.helpActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.file.fileActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.dashboard.dashboardActivity_rt
import com.example.s11.ng.jan.capit01_mobdeve.home.homeActivity_rt
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import java.io.IOException

class mapActivity_rt : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        private const val LOCATION_REQUEST_CODE = 1
    }
    private lateinit var mMap: GoogleMap
    private var isMapReady = false
    private lateinit var currentLocation: LatLng
    private lateinit var destination: LatLng
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        setContentView(R.layout.map_rt)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Request location permission
        requestLocationPermission()

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

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

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true

        // Add marker from address
        addMarkerFromAddress("2401 Taft Ave, Malate, Manila, 1004 Metro Manila")

        // Check if location permission is granted
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true

            // Get the current location using FusedLocationProviderClient
            val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    val latLng = LatLng(location.latitude, location.longitude)
                    currentLocation = latLng
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                    // Draw navigation route
                    drawNavigationRoute()
                }
            }
        }
    }

    private fun addMarkerFromAddress(address: String) {
        val geocoder = Geocoder(this)
        try {
            val addresses = geocoder.getFromLocationName(address, 1)
            if (addresses!!.isNotEmpty()) {
                val latLng = LatLng(addresses[0].latitude, addresses[0].longitude)
                mMap.addMarker(MarkerOptions().position(latLng).title(address))
                destination = latLng
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                Toast.makeText(this, "Address not found", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Error finding address", Toast.LENGTH_SHORT).show()
        }
    }

    private fun drawNavigationRoute() {
        val url = "https://maps.googleapis.com/maps/api/directions/json" +
                "?origin=" + currentLocation.latitude + "," + currentLocation.longitude +
                "&destination=" + destination.latitude + "," + destination.longitude +
                "&mode=driving" +
                "&key=AIzaSyDaka3Pso7shUImAerJ8SvrrmUSHsvmSXE"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            val routes = response.getJSONArray("routes")
            val route = routes.getJSONObject(0)
            val overviewPolyline = route.getJSONObject("overview_polyline")
            val polylineString = overviewPolyline.getString("points")

            val polylineList = PolyUtil.decode(polylineString)
            val polylineOptions = PolylineOptions()
            polylineOptions.color(Color.BLUE)
            polylineOptions.width(10f)

            for (point in polylineList) {
                polylineOptions.add(point)
            }

            mMap.addPolyline(polylineOptions)
        }, { error ->
            Toast.makeText(this, "Error drawing navigation route", Toast.LENGTH_SHORT).show()
        })

        Volley.newRequestQueue(this).add(request)
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )!= PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        val locationRequest = LocationRequest.Builder(10000L)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setMinUpdateIntervalMillis(10000L)
            .setMaxUpdateDelayMillis(20000L)
            .build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    if (locationResult.locations.isNotEmpty()) {
                        val location = locationResult.lastLocation
                        val latLng = LatLng(location!!.latitude, location!!.longitude)
                        currentLocation = latLng
                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

                        // Draw navigation route
                        drawNavigationRoute()
                    }
                }
            },
            Looper.getMainLooper()
        )
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                )
            ) {
                //We can show user a dialog why this permission is necessary
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf<String>(Manifest.permission.ACCESS_FINE_LOCATION),
                    LOCATION_REQUEST_CODE
                )
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (isMapReady) {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        mMap.isMyLocationEnabled = true
                    }
                    mMap.uiSettings.isMyLocationButtonEnabled = true
                }
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
    }

    override fun onStop() {
        super.onStop()
        fusedLocationClient.removeLocationUpdates(object : LocationCallback() {})
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