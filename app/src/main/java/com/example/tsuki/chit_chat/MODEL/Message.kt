package com.example.tsuki.chit_chat.MODEL

import com.google.gson.annotations.SerializedName
import java.util.*

class Message(
        @SerializedName("_id") var _id: String = "",
        @SerializedName("message") var message: String = "",
        @SerializedName("likes") var likes: Int = 0,
        @SerializedName("dislikes") var dislikes: Int = 0,
        @SerializedName("client") var client: String = ""
) {

    fun like() {
        this.likes += 1
    }

    fun dislike() {
        this.dislikes += 1
    }
}