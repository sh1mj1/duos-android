package com.example.duos.ui.siginup

interface SignUpView {
    fun onSignUpLoading()
    fun onSignUpSuccess()
    fun onSignUpFailure(code: Int, message: String)
}