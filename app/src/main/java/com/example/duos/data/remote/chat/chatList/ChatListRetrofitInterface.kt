package com.example.duos.data.remote.chat.chatList

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ChatListRetrofitInterface {
    @GET("/test/chat/list")
    fun chatList(
        @Query("userIdx") userIdx : Int
    ): Call<ChatListResponse>
}