package com.example.duos.ui.login

import com.example.duos.data.remote.login.LoginVerifyAuthNumResultData

interface LoginVerifyAuthNumView {
    fun onLoginVerifyAuthNumSuccess(result : LoginVerifyAuthNumResultData)
    fun onLoginVerifyAuthNumFailure(code: Int, message: String)
}