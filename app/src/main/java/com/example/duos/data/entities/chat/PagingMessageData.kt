package com.example.duos.data.entities.chat

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class PagingMessageData(
    @SerializedName("chatIdx") var chatIdx: String,  // 채팅 메세지 복합 인덱스 (chatRoomIdx @ uuid)
    @SerializedName("uuid") var uuid: String, // 채팅 메세지 자체 인덱스
    @SerializedName("chatRoomIdx") var chatRoomIdx: String,
    @SerializedName("msgType") var type: String,
    @SerializedName("msgContent") var message: String,
    @SerializedName("senderIdx") var senderIdx: Int,
    @SerializedName("sentAt") var sentAt: LocalDateTime
)
