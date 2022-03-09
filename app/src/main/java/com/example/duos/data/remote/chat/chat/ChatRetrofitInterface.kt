package com.example.duos.data.remote.chat.chat

import com.example.duos.data.entities.chat.CreateChatRoomData
import com.example.duos.data.entities.chat.PagingChatMessageRequestBody
import com.example.duos.data.entities.chat.SyncChatMessageRequestBody
import com.example.duos.data.entities.chat.sendMessageData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatRetrofitInterface {

    @POST("api/chat/user")
    fun createChatRoom(@Body createChatRoomData: CreateChatRoomData): Call<CreateChatRoomResponse>

    @POST("api/chat")
    fun sendMessage(@Body sendMessageData: sendMessageData): Call<SendMessageResponse>

    @POST("api/chat/history")
    fun pagingChatMessage(@Body pagingChatMessageRequestBody: PagingChatMessageRequestBody): Call<PagingChatMessageResponse>

}

