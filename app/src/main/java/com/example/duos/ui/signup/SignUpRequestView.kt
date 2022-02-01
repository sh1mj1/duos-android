package com.example.duos.ui.signup

interface SignUpRequestView {
    fun onSignUpRequestSuccess()
    fun onSignUpRequestFailure(code: Int, message: String)
}