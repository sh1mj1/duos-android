package com.example.duos.data.remote.chat.chat

import com.google.gson.annotations.SerializedName
data class CreateChatRoomResultData(
    @SerializedName("createdNewChatRoom") val createdNewChatRoom: Boolean,
    @SerializedName("targetChatRoomIdx") val targetChatRoomIdx: String,
    @SerializedName("participantList") val participantList: List<Int>
)

data class CreateChatRoomResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: CreateChatRoomResultData
)
