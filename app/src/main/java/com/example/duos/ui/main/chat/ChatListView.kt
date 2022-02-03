package com.example.duos.ui.main.chat

import com.example.duos.data.entities.chat.ChatRoom

interface ChatListView {
    fun onGetChatListSuccess(chatList: List<ChatRoom>)
    fun  onGetChatListFailure(code: Int, message: String)
}