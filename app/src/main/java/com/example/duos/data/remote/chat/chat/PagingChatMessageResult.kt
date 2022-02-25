package com.example.duos.data.remote.chat.chat

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class PagingChatMessageList(
    val chatIdx: String,
    val uuid: String,
    val chatRoomIdx: String,
    val msgType: String,
    val msgContent: String,
    val senderIdx: Int,
    val sentAt: LocalDateTime
)

data class PagingChatMessageResult(
    @SerializedName("pageNumber") val pageNumber : Int,
    @SerializedName("currentItemCnt") val currentItemCnt: Int,
    @SerializedName("isNextPageAvailable") val isNextPageAvailable : Boolean,
    @SerializedName("messageList") val messageList: List<MessageListData>
)
