//package com.example.s11.ng.jan.capit01_mobdeve
//
//import android.os.AsyncTask
//import android.renderscript.ScriptGroup.Input
//import org.json.JSONArray
//import java.io.BufferedReader
//import java.io.InputStreamReader
//import java.net.HttpURLConnection
//import java.net.URL
//
//interface OnDataFetchedListener {
//    fun onDataFetched(data: List<newsData>)
//}
//class fetchNews (private val listener: OnDataFetchedListener): AsyncTask<Void, Void, List<newsData>>() {
//
//    private val mongoDBApiURL = "https://asia-south1.gcp.data.mongodb-api.com/app/mobile_bdrss-fcluenw/endpoint/getNews"
//
//    override fun doInBackground(vararg params: Void?): List<newsData> {
//        try {
//            val url = URL(mongoDBApiURL)
//            val urlConnection: HttpURLConnection = url.openConnection() as HttpURLConnection
//            val inputStreamReader = InputStreamReader(urlConnection.inputStream)
//            val bufferedReader = BufferedReader(inputStreamReader)
//
//            val stringBuilder = StringBuilder()
//            var line: String?
//            while (bufferedReader.readLine().also() { line = it } != null) {
//                stringBuilder.append(line)
//            }
//
//            val jsonArray = JSONArray(stringBuilder.toString())
//            val dataList = ArrayList<newsData>()
//
//            for (i in 0..jsonArray.length()-1) {
//                val jsonObject = jsonArray.getJSONObject(i)
//
//                val announcementID = jsonObject.getString("announcementID")
//                val announcementTitle = jsonObject.getString("announcementTitle")
//                val announcementContent = jsonObject.getString("announcementContent")
//                val announcementAuthor = jsonObject.getString("announcementAuthor")
//                val announcementUpload = jsonObject.getString("announcementUploadDate")
//                val announcementStatus = jsonObject.getString("announcementTitle")
//
//                dataList.add(newsData(
//                        announcementID,
//                        announcementTitle,
//                        announcementContent,
//                        announcementAuthor,
//                        announcementUpload,
//                        announcementStatus
//                    )
//                )
//
//            }
//
//            return dataList
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//        return ArrayList()
//    }
//    override fun onPostExecute(result: List<newsData>){
//        super.onPostExecute(result)
//        listener.onDataFetched(result)
//    }
//}
