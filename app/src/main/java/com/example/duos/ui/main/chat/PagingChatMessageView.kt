package com.example.duos.ui.main.chat

import com.example.duos.data.remote.chat.chat.PagingChatMessageResult

interface PagingChatMessageView {
    fun onPagingChatMessageSuccess(pagingChatMessageResult: PagingChatMessageResult)
    fun onPagingChatMessageFailure(code: Int, message: String)
}