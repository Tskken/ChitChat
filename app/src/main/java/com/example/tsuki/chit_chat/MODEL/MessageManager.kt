package com.example.tsuki.chit_chat.MODEL

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

class MessageManager(
        @SerializedName("count") var count: Int = 0,
        @SerializedName("date") var date: String = "",
        @SerializedName("messages") var messages: Array<Message> = arrayOf()
) {
    companion object {
        fun decode(json: String): MessageManager {
            return Gson().fromJson<MessageManager>(json, MessageManager::class.java)
        }
    }
}
