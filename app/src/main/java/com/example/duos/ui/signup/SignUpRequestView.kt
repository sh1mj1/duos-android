package com.example.duos.ui.signup

import com.example.duos.data.remote.signUp.SignUpRequestResultData

interface SignUpRequestView {
    fun onSignUpRequestSuccess(result : SignUpRequestResultData)
    fun onSignUpRequestFailure(code: Int, message: String)
}