package com.example.tsuki.chit_chat.UTIL

import com.example.tsuki.chit_chat.MODEL.Message
import com.google.gson.Gson

class EncoderDecoder {
    fun Encode(message: Message): String {
        return Gson().toJson(message)
    }

    fun Decode(json: String): Message {
        var message: Message()
        Gson().fromJson<Message>(json, message)
    }
}