package com.example.duos.ui.main.chat

import com.example.duos.data.entities.ChatListItem

interface ChatListView {
    fun onGetChatListSuccess(chatList: List<ChatListItem>)
    fun  onGetChatListFailure(code: Int, message: String)
}