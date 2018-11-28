package com.example.tsuki.chit_chat.MODEL

class Message {
    var id: Int,
    var message: String,
    var likes: Int,
    var dislikes: Int

    fun like() {
        this.likes += 1
    }

    fun dislike() {
        this.dislikes += 1
    }
}