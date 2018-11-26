package com.example.tsuki.chit_chat.UTIL

import android.os.AsyncTask

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CCApiTask(
        private val mCallback: CCEventListener?,
        private val mAddress: String,
        private val mMethod: METHODS,
        private val mData: String? = null
) : AsyncTask<String, String, String>() {

    companion object {
        enum class METHODS {
            GET, POST
        }

        private const val API_KEY = "436a85b4-9966-428c-b608-0552b9c55a01"
        private const val EMAIL = "easter@champlain.edu"
    }

    private var mResponse = ""

//    override fun doInBackground(vararg voids: String): String? {
//        try {
//            val obj = URL(mAddress)
//            val con = obj.openConnection() as HttpURLConnection
//
//            con.requestMethod = mMethod.name
//            con.setRequestProperty("key", API_KEY)
//            con.setRequestProperty("client", EMAIL)
//
//            val responseCode = con.responseCode
//            println("Sending GET Request to URL: $mAddress")
//            println("Response Code: $responseCode")
//
//            val inStream = BufferedReader(
//                    InputStreamReader(con.inputStream))
//            var inputLine: String? = inStream.readLine()
//            val servResponse = StringBuilder()
//
//            while (inputLine != null) {
//                servResponse.append(inputLine)
//                inputLine = inStream.readLine()
//            }
//
//            inStream.close()
//
//            //print result
//            println("Server responded with " + servResponse.toString())
//            this.mResponse = servResponse.toString()
//        } catch (e: Exception) {
//            mCallback?.OnEventFailure()
//        }
//
//        return null
//    }

    override fun doInBackground(vararg params: String): String? {
        var urlString = "$mAddress?key=$API_KEY&client=$EMAIL"

        try {

            if (this.mMethod.equals(METHODS.POST.name)) {
                urlString += "&message=$mData"
            }

            val url = URL(urlString)

            val con = url.openConnection() as HttpURLConnection

            con.requestMethod = this.mMethod.name

            val responseCode = con.responseCode
            println("Sending ${mMethod.name} Request to URL: $mAddress")
            println("Response Code: $responseCode")

            val inStream = BufferedReader(
                    InputStreamReader(con.inputStream))
            var inputLine: String? = inStream.readLine()
            val servResponse = StringBuilder()

            while (inputLine != null) {
                servResponse.append(inputLine)
                inputLine = inStream.readLine()
            }
            inStream.close()

            //print result
            println("Server responded with " + servResponse.toString())
            this.mResponse = servResponse.toString()

        } catch (e: Exception) {
            mCallback!!.OnEventFailure()
        }

        return this.mResponse
    }

    override fun onPostExecute(resp: String) {
        if (this.mCallback != null) {
            this.mCallback.OnEventCompletion(mResponse)
        }
    }
}
