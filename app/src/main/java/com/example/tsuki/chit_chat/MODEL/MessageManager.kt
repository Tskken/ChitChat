package com.example.tsuki.chit_chat.MODEL

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

// MessageManager model with GSON support //
// - Handles holding server response for ChitChat GET
class MessageManager(
        @SerializedName("count") var count: Int = 0,
        @SerializedName("messages") var messages: Array<Message> = arrayOf()
) {
    companion object {
        // Decode JSON string to MessageManager class //
        // - Return MessageManager class
        fun decode(json: String): MessageManager {
            return Gson().fromJson<MessageManager>(json, MessageManager::class.java)
        }
    }
}
