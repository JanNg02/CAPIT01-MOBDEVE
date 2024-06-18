package com.example.s11.ng.jan.capit01_mobdeve

import android.content.Intent
import android.os.AsyncTask
import android.os.Binder
import android.os.IBinder
import android.util.Log
import org.json.JSONArray
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import com.example.s11.ng.jan.capit01_mobdeve.adapter.modelPost

interface OnDataFetchedListener {
    fun onDataFetched(data: List<modelPost>)
}

class fetchPost(private val listener: OnDataFetchedListener) : AsyncTask<Void, Void, List<modelPost>>() {

    private val mongoDBApiURL = "https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/getNews"


    override fun doInBackground(vararg params: Void?): List<modelPost> {
        try {
            val url = URL(mongoDBApiURL)
            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlConnection.requestMethod = "GET"
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

                    val jsonArray = JSONArray(stringBuilder.toString())
                    val dataList = ArrayList<modelPost>()

                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)

                        val announcementAuthor = jsonObject.getString("announcementAuthor")
                        val announcementContent = jsonObject.getString("announcementContent")

                        dataList.add(modelPost(
                            announcementAuthor,
                            announcementContent
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
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e("Error", "Error fetching data: ${e.message}")
        }
        return ArrayList()
    }

    override fun onPostExecute(result: List<modelPost>){
//        super.onPostExecute(result)
//        Log.d("OnPostExecute", "Result size: ${result.size}")
        listener.onDataFetched(result)
    }
}