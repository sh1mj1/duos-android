package com.example.duos.ui.main.chat

import com.example.duos.data.remote.chat.chat.SendMessageResultData

interface SendMessageView {
    fun onSendMessageLoading()
    fun onSendMessageSuccess(sendMessageResultData: SendMessageResultData)
    fun onSendMessageFailure(code: Int, message: String)
}