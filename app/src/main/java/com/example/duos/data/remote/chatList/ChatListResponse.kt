package com.example.duos.data.remote.chatList

import com.example.duos.data.entities.ChatListItem
import com.google.gson.annotations.SerializedName

data class ChatListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<ChatListItem>
)
