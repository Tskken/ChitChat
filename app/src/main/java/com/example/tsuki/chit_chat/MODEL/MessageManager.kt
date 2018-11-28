package com.example.tsuki.chit_chat.MODEL

import com.google.gson.annotations.SerializedName

class MessageManager(
        @SerializedName("count") var count: Int = 0,
        @SerializedName("date") var date: String = "",
        @SerializedName("messages") var messages: Array<Message> = arrayOf()
)
