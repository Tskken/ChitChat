package com.example.tsuki.chit_chat.MODEL

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.example.tsuki.chit_chat.R
import com.example.tsuki.chit_chat.UTIL.CCApiTask
import com.example.tsuki.chit_chat.UTIL.CCEventListener

class MainActivity : AppCompatActivity(), CCEventListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiTask = CCApiTask(
                this,
                "https://www.stepoutnyc.com/chitchat",
                CCApiTask.Companion.METHODS.GET
        )

        apiTask.execute()
    }

    override fun OnEventCompletion(resp: String) {
        val textView = findViewById<TextView>(R.id.respview)

        textView.text = resp
    }

    override fun OnEventFailure() {

    }

    fun noop() : Unit {
        println("ok")
    }
}
