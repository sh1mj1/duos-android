package com.example.duos.data.entities

data class MessageData(
    var chatRoomIdx: String = "",
    var type: String = "",
    var senderIdx: Int = 0,   // from
    var receiverIdx: Int = 0, // to
    var message: String = "",
    var sendTime: Long = 0
)
