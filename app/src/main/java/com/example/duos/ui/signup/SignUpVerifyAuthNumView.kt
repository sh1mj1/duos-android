package com.example.duos.ui.signup

interface SignUpVerifyAuthNumView {
    fun onSignUpVerifyAuthNumSuccess()
    fun onSignUpVerifyAuthNumFailure(code: Int, message: String)
}