package com.example.duos.data.entities.chat

import com.google.gson.annotations.SerializedName

data class PagingChatMessageRequestBody(
    val userIdx: Int,
    val chatRoomIdx: String,
    val pageNum: Int,
    val listNum: Int
)
