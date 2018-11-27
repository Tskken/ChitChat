package com.example.tsuki.chit_chat.CONTROLLER

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.tsuki.chit_chat.R
import com.example.tsuki.chit_chat.UTIL.CCApiTask
import org.json.JSONObject

class MessageRecyclerControler : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // create api task to get initial data
        val apiTask = CCApiTask(
                sServerURL,
                CCApiTask.Companion.METHODS.GET,
                { data -> onFeedData(data) },
                { onRequestFailure() }
        )

        // execute api call
        apiTask.execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun onFeedData(data: String) {
        //textView.text = data

        val jsonData = JSONObject(data)

        val count = jsonData.getInt("count")
        val date = jsonData.getString("date")
        val messages = jsonData.getJSONArray("messages")

        /*
        message format:

        {
          "_id" : "UUID",
          "client" : "email@champlain.edu",
          "date" : "datestring",
          "dislikes" : 0,
          "ip" : "255.255.255.255",
          "likes" : 0,
          "loc" : [
            null,
            null
          ],
          "message" : "lorem ipsum dolor sit amet"
        }

        */
    }

    private fun likeMessage(messageId: String) {
        // create api task
        val apiTask = CCApiTask(
                "$sServerURL/like/$messageId",
                CCApiTask.Companion.METHODS.GET,
                { data -> /* todo: determine how we deal with data */ },
                { onRequestFailure() }
        )

        // execute api call
        apiTask.execute()
    }

    private fun dislikeMessage(messageId: String) {
        // create api task
        val apiTask = CCApiTask(
                "$sServerURL/dislike/$messageId",
                CCApiTask.Companion.METHODS.GET,
                { data -> /* todo: determine how we deal with data */ },
                { onRequestFailure() }
        )

        // execute api call
        apiTask.execute()
    }

    private fun postMessage(message: String) {
        // create api task
        val apiTask = CCApiTask(
                sServerURL,
                CCApiTask.Companion.METHODS.POST,
                { data -> /* todo: determine how we deal with data */ },
                { onRequestFailure() },
                message
        )

        // execute api call
        apiTask.execute()
    }

    private fun onRequestFailure() {
        // TODO: Establish feed request failure method
    }

    private companion object {
        const val sServerURL = "https://www.stepoutnyc.com/chitchat"
    }
}