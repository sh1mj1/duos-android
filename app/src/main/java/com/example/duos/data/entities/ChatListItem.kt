package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class ChatListItem(
    @SerializedName("chatRoomName") var chatRoomName : String = "",
    @SerializedName("chatRoomImageUrl") var chatRoomImg : String? = "",
    @SerializedName("lastMessage") var lastMessage : String = "",
    @SerializedName("updatedAt") var lastUpdatedAt : String = "",
    @SerializedName("hasNewMessage") var hasNewMessage : Boolean = false
)
