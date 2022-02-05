package com.example.duos.ui.login

import com.example.duos.data.remote.auth.Auth

interface LoginCreateAuthNumView {
    fun onLoginCreateAuthNumSuccess()
    fun onLoginCreateAuthNumFailure(code: Int, message: String)
}