package com.example.duos.ui.signup

interface SignUpNickNameView {
    fun onSignUpNickNameSuccess()
    fun onSignUpNickNameFailure(code: Int, message: String)
}