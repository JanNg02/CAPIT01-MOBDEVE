//package com.example.s11.ng.jan.capit01_mobdeve.file
//
//import android.content.Intent
//import android.os.AsyncTask
//import android.os.Binder
//import android.os.IBinder
//import android.util.Log
//import org.json.JSONArray
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//import com.example.s11.ng.jan.capit01_mobdeve.adapter.modelPost
//import org.json.JSONObject
//import java.io.IOException
//import java.io.OutputStreamWriter
//import com.google.gson.Gson
////import java.nio.charset.Charsets
//
//interface OnDataSentListener {
//    fun onDataSent(success: Boolean)
//}
//
//class uploadMissing(private val missingFullName: String, private val description: String, private val areaLastSeen: String, private val timeLastSeen: String, private val age: Int, private val listener: OnDataSentListener) : AsyncTask<Void, Void, Boolean>() {
//
//    private val mongoDBApiURL = "https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/postMissing"
//    val gson = Gson()
//
//    override fun doInBackground(vararg params: Void?): Boolean {
//        try {
//            val url = URL(mongoDBApiURL)
//            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
//            urlConnection.requestMethod = "POST"
//            urlConnection.doOutput = true
//
////            val outputStream = urlConnection.outputStream
////            val writer = OutputStreamWriter(outputStream, "UTF-8")
////
////
////                        // Create a JSON object to send
////            val jsonObject = JSONObject()
////            jsonObject.put("missingFullName", missingFullName)
////            jsonObject.put("description", description)
////            jsonObject.put("areaLastSeen", areaLastSeen)
////            jsonObject.put("timeLastSeen", timeLastSeen)
////            jsonObject.put("age", age)
//
//
//
//
////            Log.d("JSON Object", jsonObject.toString()) // Log the JSON object to check its format
//
////            writer.write(jsonObject.toString())
////            writer.flush()
////            writer.close()
//
//            val jsonObject = JSONObject()
//            jsonObject.put("data", JSONObject().put("missingFullName", missingFullName)
//                .put("description", description)
//                .put("areaLastSeen", areaLastSeen)
//                .put("timeLastSeen", timeLastSeen)
//                .put("age", age))
//
//            Log.d("JSON Object", jsonObject.toString()) // Log the JSON object
//
//            val outputStream = urlConnection.outputStream
//            val writer = OutputStreamWriter(outputStream, "UTF-8")
//
//            writer.write(jsonObject.toString())
//            writer.flush()
//            writer.close()
//
//            val responseCode = urlConnection.responseCode
//            Log.d("Response Code", responseCode.toString())
//
//            if (responseCode == HttpURLConnection.HTTP_OK) {
//                Log.d("postMissing", "Data uploaded successfully")
//                return true
//            } else {
//                Log.e("Error", "Failed to upload data. Response code: $responseCode")
//                val errorStream = urlConnection.errorStream
//                val errorMessage = errorStream.bufferedReader().use { it.readText() }
//                Log.e("Error", "Error message: $errorMessage")
//            }
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Log.e("Error", "Error uploading data: ${e.message}")
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Log.e("Error", "Error uploading data: ${e.message}")
//        }
//        return false
//    }
//
//    override fun onPostExecute(result: Boolean) {
//        listener.onDataSent(result)
//    }
//}
//
