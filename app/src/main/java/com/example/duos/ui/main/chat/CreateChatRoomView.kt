package com.example.duos.ui.main.chat

interface CreateChatRoomView {
    fun onCreateChatRoomLoading()
    fun onCreateChatRoomSuccess()
    fun onCreateChatRoomFailure(code: Int, message: String)
}