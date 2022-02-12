package com.example.duos.ui.main.chat

import com.example.duos.data.remote.chat.chat.SyncChatMessageData

interface ChatMessageView {
    fun onSyncChatMessageSuccess(syncChatMessageData: SyncChatMessageData)
    fun onSyncChatMessageFailure(code: Int, message: String)
}