package com.example.tsuki.chit_chat.CONTROLLER

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tsuki.chit_chat.MODEL.MessageManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.tsuki.chit_chat.MODEL.Message
import com.example.tsuki.chit_chat.R
import com.example.tsuki.chit_chat.UTIL.CCApiTask
import kotlinx.android.synthetic.main.recycler_cell.*

class MessageRecyclerController : Fragment() {
    // Message list manager //
    private var mMessageManager: MessageManager? = null

    // Recycler view //
    private var mRecyclerView: RecyclerView? = null

    // Recycler adapter //
    private var mAdapter: MessageAdapter? = null

    // Message text input //
    private var mMessageEntry: EditText? = null

    // Message send button //
    private var mMessageSend: Button? = null

    // Pull to refresh //
    private var mSwipeRefresh: SwipeRefreshLayout? = null

    // List of liked messages //
    private var mLiked = Array(0){""}

    // List of disliked messages //
    private var mDisliked = Array(0){""}

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.recycler_view, container, false)

        // Make GET request to server for messages //
        fetchFeed()

        // Initialize RecyclerView //
        mRecyclerView = view.findViewById(R.id.recycler_view) as RecyclerView
        mRecyclerView!!.layoutManager = LinearLayoutManager(activity!!)

        // Update UI //
        updateUI()

        // Init Message Sending //
        mMessageEntry = view.findViewById(R.id.message_entry)
        mMessageSend = view.findViewById(R.id.message_send)

        // Create onClick handler for sending message //
        mMessageSend!!.setOnClickListener {
            postMessage(mMessageEntry!!.text.toString())
        }

        // Setup Swipe-to-Refresh //
        mSwipeRefresh = view.findViewById(R.id.swipe_refresh)
        mSwipeRefresh?.setOnRefreshListener { fetchFeed() }

        return view
    }

    // Reload messages //
    override fun onResume() {
        super.onResume()

        // Refresh feed //
        fetchFeed()
    }

    // Recycler View class //
    private inner class MessageView(inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
                inflater.inflate(
                        R.layout.recycler_cell,
                        parent,
                        false
                )

        )
    {

        // User name text view //
        private var mUName: TextView = itemView.findViewById(R.id.user_name)

        // Message date text view //
        private var mDateTime: TextView = itemView.findViewById(R.id.date_time)

        // Message content text view //
        private var mMessageText: TextView = itemView.findViewById(R.id.message)

        // Like button //
        private var mLikeButton: Button = itemView.findViewById(R.id.like_button)

        // Dislike button //
        private var mDislikeButton: Button = itemView.findViewById(R.id.dislike_button)

        // Current message held in view //
        private var message: Message? = null

        // Bind message data to cell //
        fun bind(message: Message) {
            // Split user name to remove email //
           val uName = message.client.split("@")[0]
                   .split(".")
                   .joinToString(
                           separator = " "
                   )

            // Set message to current message held in view //
            this.message = message

            // Set user name text to split text above //
            mUName.text = uName

            // Set date text to message date //
            mDateTime.text = message.date

            // Set message content to message Text //
            mMessageText.text = this.message!!.message

            // Set like button text to message like count //
            mLikeButton.text =  "Likes - ${this.message!!.likes}"

            // Set dislike button text to message dislike count //
            mDislikeButton.text = "Dislikes - ${this.message!!.dislikes}"

            // Create onClick handler for like button //
            mLikeButton.setOnClickListener {
                // Send GET like request to server for message ID //
                likeMessage(message._id, this)
            }

            // Create onClick handler for dislike button //
            mDislikeButton.setOnClickListener {
                // Send GET dislike request to server for message ID //
                dislikeMessage(message._id, this)
            }
        }

        // MessageView like wrapper around message.Like() //
        fun like() {
            // Increments message likes count and sets like button text to new count //
            mLikeButton.text = "Likes - ${this.message!!.like()}"
        }

        // MessageView dislike wrapper around message.Dislike() //
        fun dislike() {
            // Increments message dislikes count and sets dislike button text to new count //
            mDislikeButton.text = "Dislikes - ${this.message!!.dislike()}"
        }
    }

    // Recycler view adapter //
    private inner class MessageAdapter : RecyclerView.Adapter<MessageView>() {
        // Override adapter getCount //
        override fun getItemCount(): Int {
            // Async check for null MessageManager //
            // - Return message count if not null
            // - Return zero if null
            return if (mMessageManager != null) {
                mMessageManager!!.count
            } else {
                0
            }
        }

        // Override adapter CrateViewHolder //
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageView {
            // Create Inflater //
            val layoutInflater = LayoutInflater.from(activity)
            // Create new view for inflater //
            // - Returns a MessageView
            return MessageView(layoutInflater, parent)
        }

        // Override adapter onBind //
        override fun onBindViewHolder(holder: MessageView, position: Int) {
            // Bind MessageView to Recycler View cell position //
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

        mSwipeRefresh?.isRefreshing = false
    }

    // On receiving data from ChitChat GET response //
    private fun onFeedData(data: String) {
        // Decode JSON string in to MessageManager //
        this.mMessageManager = MessageManager.decode(data)

        // Update UI for new MessageManager data //
        updateUI()
    }

    // GET request to ChitChat for liking a message //
    private fun likeMessage(messageId: String, message: MessageView) {
        // Check if message ID exists in liked list //
        // - Ignore if it exists
        if (mLiked.contains(messageId)) {
            return
        }

        // create api task
        val apiTask = CCApiTask(
                // URL address to API //
                mAddress = "$sServerURL/like/$messageId",

                // Method request type //
                mMethod = CCApiTask.Companion.METHODS.GET,

                // On return success //
                // - Update message like count
                mSuccess = { message.like() },

                // On return failure //
                mFailure = { onRequestFailure("GET like request failed") }
        )

        // execute api call
        apiTask.execute()

        // Add message ID to liked messages list //
        mLiked = mLiked.plus(messageId)
    }

    // GET request to ChitChat for disliking a message //
    private fun dislikeMessage(messageId: String, message: MessageView) {
        // Check if message ID exists in disliked list //
        // - Ignore if it exists
        if (mDisliked.contains(messageId)) {
            return
        }

        // create api task
        val apiTask = CCApiTask(
                // URL address to API //
                mAddress = "$sServerURL/dislike/$messageId",

                // Method request type //
                mMethod = CCApiTask.Companion.METHODS.GET,

                // On return success //
                // - Update message dislike count
                mSuccess = {  message.dislike() },

                // On return failure //
                mFailure = { onRequestFailure("GET dislike request failed") }
        )

        // execute api call
        apiTask.execute()

        // Add message ID to disliked messages list //
        mDisliked = mDisliked.plus(messageId)
    }

    // POST request to ChitChat for sending a message //
    private fun postMessage(message: String) {
        // create api task
        val apiTask = CCApiTask(
                // URL address to API //
                mAddress =  sServerURL,

                // Method request type //
                mMethod = CCApiTask.Companion.METHODS.POST,

                // On return success //
                mSuccess = {
                    // update EditText to contain nothing //
                    mMessageEntry?.setText("")

                    // hide input keyboard //
                    hideKeyboard()

                    // Get message feed and update UI //
                    fetchFeed()
                },

                // On return failure //
                mFailure = { onRequestFailure("POST request to ChitChat failed") },

                // Message data to send to API //
                mData = message
        )

        // execute api call
        apiTask.execute()
    }

    // GET request to ChitChat for messages from server //
    private fun fetchFeed() {
        // create api task to get initial data
        val apiTask = CCApiTask(
                // URL address to API //
                mAddress = sServerURL,

                // Method request type //
                mMethod = CCApiTask.Companion.METHODS.GET,

                // On return success //
                // - update MessageManager data
                mSuccess = { data -> onFeedData(data) },

                // On return failure //
                mFailure = { onRequestFailure("GET request to ChitChat failed") }
        )

        // execute api call
        apiTask.execute()
    }

    // Log failure on API request //
    private fun onRequestFailure(error: String) {
        Log.d("ChitChatRequest", error)
    }

    /*
    WE DID NOT WRITE THIS FUNCTION
    author: Gunhan
    url: https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
     */
    private fun Fragment.hideKeyboard() {
        activity?.hideKeyboard(view!!)
    }

    /*
    WE DID NOT WRITE THIS FUNCTION
    author: Gunhan
    url: https://stackoverflow.com/questions/41790357/close-hide-the-android-soft-keyboard-with-kotlin
     */
    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // ChitChat API URL //
    private companion object {
        const val sServerURL = "https://www.stepoutnyc.com/chitchat"
    }
}