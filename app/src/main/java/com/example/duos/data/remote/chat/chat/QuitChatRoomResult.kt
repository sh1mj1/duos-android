package com.example.duos.data.remote.chat.chat

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

data class QuitChatRoomResult(
    @SerializedName("type") val type: String,
    @SerializedName("chatRoomIdx") val chatRoomIdx: String,
    @SerializedName("userIdx") val userIdx: Int,
    @SerializedName("deletedAt") val deletedAt: LocalDateTime
)
