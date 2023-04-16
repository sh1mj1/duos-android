package com.example.duos.ui.main.chat

import com.example.duos.data.remote.chat.chat.QuitChatRoomResult

interface QuitChatRoomView {
    fun onQuitChatRoomSuccess(quitChatRoomResult: QuitChatRoomResult)
    fun onQuitChatRoomFailure(code: Int, message: String)
}