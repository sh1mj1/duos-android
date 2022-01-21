package com.example.duos.data.entities

data class MessageData(
    var type: String,
    var from: String,
    var to: String,
    var content: String,
    var sendTime: Long
)
