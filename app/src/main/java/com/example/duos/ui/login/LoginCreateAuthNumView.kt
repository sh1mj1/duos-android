package com.example.duos.ui.login

interface LoginCreateAuthNumView {
    fun onLoginCreateAuthNumSuccess()
    fun onLoginCreateAuthNumFailure(code: Int, message: String)
}