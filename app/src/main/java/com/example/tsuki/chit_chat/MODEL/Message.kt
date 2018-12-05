package com.example.tsuki.chit_chat.MODEL

import com.google.gson.annotations.SerializedName

// Message model with GSON support //
class Message(
        @SerializedName("_id") var _id: String = "",
        @SerializedName("message") var message: String = "",
        @SerializedName("date") var date: String = "",
        @SerializedName("likes") var likes: Int = 0,
        @SerializedName("dislikes") var dislikes: Int = 0,
        @SerializedName("client") var client: String = ""
) {
    // Like message and return like count //
    fun like(): Int {
        return ++this.likes
    }

    // Dislike message and return dislike count //
    fun dislike(): Int {
        return ++this.dislikes
    }
}