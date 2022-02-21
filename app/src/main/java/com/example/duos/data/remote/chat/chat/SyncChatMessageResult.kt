package com.example.duos.data.remote.chat.chat

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class MessageListData (
    @SerializedName("chatIdx") var chatIdx: String,  // 채팅 메세지 복합 인덱스 (chatRoomIdx @ uuid)
    @SerializedName("uuid") var uuid: String, // 채팅 메세지 자체 인덱스
    @SerializedName("chatRoomIdx") var chatRoomIdx: String,
    @SerializedName("msgType") var type: String,
    @SerializedName("msgContent") var message: String,
    @SerializedName("senderIdx") var senderIdx: Int,
    @SerializedName("sentAt") var sentAt: LocalDateTime
)

data class SyncChatMessageData (
    @SerializedName("listSize") var listSize: Int,
    @SerializedName("messageList") var messageList: List<MessageListData>
)
