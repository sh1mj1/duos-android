package com.example.duos.ui.main.chat

import com.example.duos.data.remote.chat.chat.CreateChatRoomResultData

interface CreateChatRoomView {
//    fun onCreateChatRoomLoading()
    fun onCreateChatRoomSuccess(createChatRoomResultData: CreateChatRoomResultData)
    fun onCreateChatRoomFailure(code: Int, message: String)
}