package com.example.duos.data.remote.chat.chatList

import com.example.duos.data.entities.chat.ChatRoom
import com.google.gson.annotations.SerializedName

data class ChatListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<ChatRoom>
)
