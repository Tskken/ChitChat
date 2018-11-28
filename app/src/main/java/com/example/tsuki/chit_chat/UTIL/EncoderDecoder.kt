package com.example.tsuki.chit_chat.UTIL

import com.example.tsuki.chit_chat.MODEL.Message
import com.example.tsuki.chit_chat.MODEL.MessageManager
import com.google.gson.Gson

class EncoderDecoder {
    companion object {
        fun Encode(message: Message): String {
            return Gson().toJson(message)
        }

        fun Decode(json: String): MessageManager {
            return Gson().fromJson<MessageManager>(json, MessageManager::class.java)
        }
    }
}