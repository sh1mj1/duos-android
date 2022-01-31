package com.example.duos.ui.main.chat

interface SendMessageView {
    fun onSendMessageSuccess()
    fun onSendMessageFailure(code: Int, message: String)
}