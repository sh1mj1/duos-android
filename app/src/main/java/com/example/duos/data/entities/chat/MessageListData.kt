package com.example.duos.data.entities.chat

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class MessageListData (
    var chatIdx: String,  // 채팅 메세지 복합 인덱스 (chatRoomIdx @ uuid)
    var uuid: String, // 채팅 메세지 자체 인덱스
    var chatRoomIdx: String,
    var msgType: String,
    var msgContent: String,
    var senderIdx: Int,
    var sentAt: LocalDateTime
)