package com.example.tsuki.chit_chat.UTIL

import android.os.AsyncTask

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CCApiTask(
        private val mAddress: String,
        private val mMethod: METHODS,
        private val mSuccess: (String) -> Unit,
        private val mFailure: () -> Unit,
        private val mData: String? = null
) : AsyncTask<String, String, String>() {

    companion object {
        enum class METHODS {
            GET, POST
        }

        // TODO: Determine better way to manage this data and auth users
        private const val API_KEY = "436a85b4-9966-428c-b608-0552b9c55a01"
        private const val EMAIL = "easter@champlain.edu"
    }

    private var mResponse = ""

    // is run upon execute()
    override fun doInBackground(vararg params: String): String? {
        // create initial api url with auth params
        var urlString = "$mAddress?key=$API_KEY&client=$EMAIL"

        // if we are posting data we append that as well
        if (mData != null) {
            urlString += "&message=$mData"
        }

        // create url from strong
        val url = URL(urlString)

        // open connection to url
        val con = url.openConnection() as HttpURLConnection

        try {
            // establish the current request method
            con.requestMethod = this.mMethod.name

            // make request and grab resp code
            val responseCode = con.responseCode
            println("Sending ${mMethod.name} Request to URL: $mAddress")
            println("Response Code: $responseCode")

            // create input stream for request data
            val inStream = BufferedReader(
                    InputStreamReader(con.inputStream)
            )

            // establish variables for data
            var inputLine: String? = inStream.readLine()
            val servResponse = StringBuilder()

            // get data while there is data to grab
            while (inputLine != null) {
                servResponse.append(inputLine)
                inputLine = inStream.readLine()
            }

            // close stream when finished
            inStream.close()

            //print result
            println("Server responded with " + servResponse.toString())
            this.mResponse = servResponse.toString()

        } catch (e: Exception) {
            // if there is an issue we call the failure event
            mFailure()
        }

        return this.mResponse
    }

    override fun onPostExecute(resp: String) {
        // send response data to callback
        this.mSuccess(mResponse)
    }
}
