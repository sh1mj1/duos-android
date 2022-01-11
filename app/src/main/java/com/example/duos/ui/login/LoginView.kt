package com.example.duos.ui.login

import com.example.duos.data.remote.auth.Auth

interface LoginView {
    fun onLoginLoading()
    fun onLoginSuccess(auth: Auth)
    fun onLoginFailure(code: Int, message: String)
}