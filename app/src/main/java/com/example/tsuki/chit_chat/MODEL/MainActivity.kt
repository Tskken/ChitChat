package com.example.tsuki.chit_chat.MODEL

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.tsuki.chit_chat.R
import com.example.tsuki.chit_chat.UTIL.CCApiTask

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // create api task to get initial data
        val apiTask = CCApiTask(
                "https://www.stepoutnyc.com/chitchat",
                CCApiTask.Companion.METHODS.GET,
                { data -> onFeedData(data) },
                { onFeedRequestFailure() }
        )

        // execute api call
        apiTask.execute()
    }

    private fun onFeedData(data: String) {
        val textView = findViewById<TextView>(R.id.respview)

        textView.text = data

        // TODO: Process feed data
    }

    private fun onFeedRequestFailure() {
        // TODO: Establish feed request failure method
    }
}
