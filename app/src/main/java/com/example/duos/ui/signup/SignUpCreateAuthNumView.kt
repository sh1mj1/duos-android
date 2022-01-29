package com.example.duos.ui.signup

interface SignUpCreateAuthNumView {
    fun onSignUpCreateAuthNumSuccess()
    fun onSignUpCreateAuthNumFailure(code: Int, message: String)
}