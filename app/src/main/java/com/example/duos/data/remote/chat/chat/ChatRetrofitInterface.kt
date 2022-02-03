package com.example.duos.data.remote.chat.chat

import com.example.duos.data.entities.ChatRoom
import com.example.duos.data.entities.MessageData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ChatRetrofitInterface {
    @POST("api/chat/user")
    fun createChatRoom(@Body chatRoom: ChatRoom): Call<CreateChatRoomResponse>

    @POST("api/chat")
    fun sendMessage(@Body messageData: MessageData): Call<SendMessageResponse>
}