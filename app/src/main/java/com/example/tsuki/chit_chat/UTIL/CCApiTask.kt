package com.example.tsuki.chit_chat.UTIL

import android.os.AsyncTask

import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class CCApiTask(
        private val mCallback: CCEventListener?,
        private val mAddress: String,
        private val mMethod: () -> Unit,
        private val mData: String? = null
) : AsyncTask<Void, Void, Void>() {

    companion object {
        private const val API_KEY = "yvdkGTQbTvykiSxg11cK9QKbxFh1MMcXuaMFgcb7zqaB4gl3XA8DvMRR8J4NsewEwjUB3dyaKuC84YqA_dK7CqhQ8TPtJVr71Q_PNZJ8ig7uTNTpNuRHtgTBsdSeWnYx"
    }

    private var mResponse = ""

    override fun doInBackground(vararg voids: Void): Void? {
        try {
            val obj = URL(mAddress)
            val con = obj.openConnection() as HttpURLConnection

            con.requestMethod = "GET"
            con.setRequestProperty("Authorization", "Bearer $API_KEY")

            val responseCode = con.responseCode
            println("Sending GET Request to URL: $mAddress")
            println("Response Code: $responseCode")

            val inStream = BufferedReader(
                    InputStreamReader(con.inputStream))
            val inputLine: String? = inStream.readLine()
            val servResponse = StringBuilder()

            while (inputLine != null) {
                servResponse.append(inputLine)
            }
            inStream.close()

            //print result
            println("Server responded with " + servResponse.toString())
            this.mResponse = servResponse.toString()

        } catch (e: Exception) {
            mCallback!!.OnEventFailure()
        }

        return null
    }

    override fun onPostExecute(aVoid: Void) {
        if (this.mCallback != null) {
            this.mCallback.OnEventCompletion(mResponse)
        }
    }

    fun authUser(): Boolean {
        return false
    }

    fun getFeed(): Unit {

    }

    fun sendMessage(): Boolean {
        return false
    }
}
