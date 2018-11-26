package com.example.tsuki.chit_chat.UTIL

interface CCEventListener {
    fun OnEventCompletion(resp: String)
    fun OnEventFailure()
}