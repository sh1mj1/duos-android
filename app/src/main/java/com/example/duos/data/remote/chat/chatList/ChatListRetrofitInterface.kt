package com.example.duos.data.remote.chat.chatList

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatListRetrofitInterface {
    @GET("/api/chat/rooms")
    fun chatList(@Query("userIdx") userIdx : Int
    ): Call<ChatListResponse>
}