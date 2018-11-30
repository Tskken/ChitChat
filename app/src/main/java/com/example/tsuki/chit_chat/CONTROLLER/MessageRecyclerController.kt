package com.example.tsuki.chit_chat.CONTROLLER

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsuki.chit_chat.MODEL.MessageManager
import android.support.v7.widget.RecyclerView
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.tsuki.chit_chat.MODEL.Message
import com.example.tsuki.chit_chat.R
import com.example.tsuki.chit_chat.UTIL.CCApiTask
import kotlinx.android.synthetic.main.recycler_cell.*

class MessageRecyclerController : Fragment() {
    private var mMessageManager: MessageManager? = null
    private var mRecyclerView: RecyclerView? = null
    private var mAdapter: MessageAdapter? = null
    private var mMessageEntry: EditText? = null
    private var mMessageSend: Button? = null


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

        mMessageEntry = view.findViewById(R.id.message_entry)
        mMessageSend = view.findViewById(R.id.message_send)

        mMessageSend!!.setOnClickListener {
            postMessage(mMessageEntry!!.text.toString())
        }

        return view
    }

    private inner class MessageView(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
                inflater.inflate(
                        R.layout.recycler_cell,
                        parent,
                        false
                )

        )
    {

        private var mUName: TextView = itemView.findViewById(R.id.user_name)
        private var mMessageText: TextView = itemView.findViewById(R.id.message)
        private var mLikeButton: Button = itemView.findViewById(R.id.like_button)
        private var mDislikeButton: Button = itemView.findViewById(R.id.dislike_button)
        private var message: Message? = null

        fun bind(message: Message) {
           val uName = message.client.split("@")[0]
                   .split(".")
                   .joinToString(
                           separator = " "
                   )

            this.message = message
            mUName.text = uName
            mMessageText.text = this.message!!.message
            mLikeButton.text = "Likes - ${this.message!!.likes}"
            mDislikeButton.text = "Dislikes - ${this.message!!.dislikes}"

            mLikeButton.setOnClickListener {
                likeMessage(message._id, this)
            }

            mDislikeButton.setOnClickListener {
                dislikeMessage(message._id, this)
            }
        }

        fun like() {
            mLikeButton.text = "Likes - ${++this.message!!.likes}"
        }

        fun dislike() {
            mDislikeButton.text = "Dislikes - ${++this.message!!.dislikes}"
        }
    }

    private inner class MessageAdapter : RecyclerView.Adapter<MessageView>() {
        override fun getItemCount(): Int {
            return if (mMessageManager != null) {
                mMessageManager!!.messages.size
            } else {
                0
            }
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

        updateUI()
    }

    private fun likeMessage(messageId: String, message: MessageView) {
        // create api task
        val apiTask = CCApiTask(
                "$sServerURL/like/$messageId",
                CCApiTask.Companion.METHODS.GET,
                { message.like() },
                { onRequestFailure() }
        )

        // execute api call
        apiTask.execute()
    }

    private fun dislikeMessage(messageId: String, message: MessageView) {
        // create api task
        val apiTask = CCApiTask(
                "$sServerURL/dislike/$messageId",
                CCApiTask.Companion.METHODS.GET,
                {  message.dislike() },
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
                {
                    mMessageEntry?.setText("")

                    hideKeyboard()

                    val apiTask = CCApiTask(
                            sServerURL,
                            CCApiTask.Companion.METHODS.GET,
                            { data: String -> onFeedData(data) },
                            { onRequestFailure() }
                    )

                    // execute api call
                    apiTask.execute()
                },
                { onRequestFailure() },
                message
        )

        // execute api call
        apiTask.execute()
    }

    private fun onRequestFailure() {
        // TODO: Establish feed request failure method
    }

    /*
    WE DID NOT WRITE THIS FUNCTION
    author: Gunhan
    url: https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
     */
    fun Fragment.hideKeyboard() {
        activity?.hideKeyboard(view!!)
    }

    /*
    WE DID NOT WRITE THIS FUNCTION
    author: Gunhan
    url: https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
     */
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private companion object {
        const val sServerURL = "https://www.stepoutnyc.com/chitchat"
    }
}