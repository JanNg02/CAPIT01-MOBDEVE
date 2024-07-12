package com.example.s11.ng.jan.capit01_mobdeve.map

import android.os.AsyncTask
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

interface OnDataFetchedListener {
    fun onDataFetched(data: List<modelEvacCenter>)
}

class getEvacCenter(private val listener: OnDataFetchedListener) : AsyncTask<Void, Void, List<modelEvacCenter>>() {
    override fun doInBackground(vararg params: Void?): List<modelEvacCenter> {
        try {
            val url = URL("https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/getEvacCenter")
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"

            Log.d("Request URL", url.toString())
            Log.d("Request Method", urlConnection.requestMethod)

            urlConnection.connect()

            if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                val inputStreamReader = InputStreamReader(urlConnection.inputStream)
                val bufferedReader = BufferedReader(inputStreamReader)

                try {
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while (bufferedReader.readLine().also { line = it }!= null) {
                        stringBuilder.append(line)
                    }

                    val jsonResponse = stringBuilder.toString()
                    Log.d("JSON Response", jsonResponse)

                    val jsonArray = JSONArray(jsonResponse)
                    val dataList = ArrayList<modelEvacCenter>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val evacID = jsonObject.getString("evacID")
                        val evacName = jsonObject.getString("evacName")
                        val evacTotalCapacity = jsonObject.getString("evacTotalCapacity").toInt()
                        val evacStatus = jsonObject.getString("evacStatus")
                        val evacAddress = jsonObject.getString("evacAddress")

                        dataList.add(
                            modelEvacCenter(
                                evacID,
                                evacName,
                                evacTotalCapacity,
                                evacStatus,
                                evacAddress
                            )
                        )
                    }

                    Log.d("DataList", "Size: ${dataList.size}")

                    return dataList
                } finally {
                    bufferedReader.close()
                    inputStreamReader.close()
                }
            } else {
                Log.e("Error", "Failed to retrieve data. Response code: ${urlConnection.responseCode}")
                Log.e("Error", "Response message: ${urlConnection.responseMessage}")
                return ArrayList()
            }
        } catch (e: Exception) {
            Log.e("Error", "Error fetching data", e)
            return ArrayList()
        }
    }

    override fun onPostExecute(result: List<modelEvacCenter>){
        listener.onDataFetched(result)
    }
}