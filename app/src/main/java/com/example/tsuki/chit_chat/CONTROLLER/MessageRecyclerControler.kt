package com.example.tsuki.chit_chat.CONTROLLER

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsuki.chit_chat.MODEL.MessageManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.TextView
import com.example.tsuki.chit_chat.MODEL.Message
import com.example.tsuki.chit_chat.R
import com.example.tsuki.chit_chat.UTIL.CCApiTask

class MessageRecyclerControler : Fragment() {
    private var mMessageManager: MessageManager? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MessageAdapter? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recycler_view, container, false)

        // create api task to get initial data
        val apiTask = CCApiTask(
                sServerURL,
                CCApiTask.Companion.METHODS.GET,
                { data -> onFeedData(data) },
                { onRequestFailure() }
        )

        // execute api call
        apiTask.execute()

        // Initialize RecyclerView //
        mRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity)



        updateUI()

        return view
    }

    private inner class MessageView(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(inflater.inflate(R.layout.recycler_view, parent, false)
    ) {
        private var mUName = parent.findViewById(R.id.user_name) as TextView
        private var mMessageText = parent.findViewById(R.id.message) as TextView
        private var mLikeButton = parent.findViewById(R.id.like_button) as Button
        private var mDislikeButton = parent.findViewById(R.id.dislike_button) as Button

        init {
            mLikeButton.setOnClickListener {
                //TODO: Have like post to server
            }
            mDislikeButton.setOnClickListener {
                //TODO: have dislike post to server
            }
        }

        fun bind(message: Message) {
           var uName = message.client.split("@", ".").joinToString(
                   separator = " ",
                   prefix = "",
                   postfix = "",
                   limit = 2
           )
            mUName.text = uName
            mMessageText.text = message.message
            mLikeButton.text = message.likes.toString()
            mDislikeButton.text = message.dislikes.toString()
        }
    }

    private inner class MessageAdapter : RecyclerView.Adapter<MessageView>() {
        override fun getItemCount(): Int {
            return mMessageManager!!.messages.size
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageView {
            val layoutInflater = LayoutInflater.from(activity)
            return MessageView(layoutInflater, parent)
        }

        override fun onBindViewHolder(holder: MessageView, position: Int) {
            holder.bind(mMessageManager!!.messages[position])
        }
    }

    // Updated UI //
    private fun updateUI() {
        // Check if an adapter exists //
        // -Create a new one if non exists
        if ( mAdapter == null) {
            this.mAdapter = MessageAdapter()
            mRecyclerView!!.adapter = mAdapter
        } else {
            // Update UI //
            activity!!.runOnUiThread { mAdapter!!.notifyDataSetChanged() }
        }
    }


    private fun onFeedData(data: String) {
        this.mMessageManager = MessageManager.decode(data)
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