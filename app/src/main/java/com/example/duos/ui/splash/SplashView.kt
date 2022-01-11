package com.example.duos.ui.splash

interface SplashView {
    fun onAutoLoginLoading()
    fun onAutoLoginSuccess()
    fun onAutoLoginFailure(code: Int, message: String)
}