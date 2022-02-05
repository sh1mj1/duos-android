package com.example.duos.data.remote.login

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.PhoneAuthNum
import com.example.duos.ui.login.LoginCreateAuthNumView
import com.example.duos.ui.login.LoginVerifyAuthNumView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LoginService {
    fun loginCreateAuthNum(loginCreateAuthNumView: LoginCreateAuthNumView, phoneNumber: String) {
        val loginCreateAuthNumService = ApplicationClass.retrofit.create(LoginRetrofitInterface::class.java)

        loginCreateAuthNumService.loginCreateAuthNum(phoneNumber).enqueue(object :
            Callback<LoginCreateAuthNumResponse> {
            override fun onResponse(
                call: Call<LoginCreateAuthNumResponse>,
                response: Response<LoginCreateAuthNumResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> loginCreateAuthNumView.onLoginCreateAuthNumSuccess()
                    else -> loginCreateAuthNumView.onLoginCreateAuthNumFailure(
                        resp.code,
                        resp.message
                    )
                }
            }

            override fun onFailure(call: Call<LoginCreateAuthNumResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                loginCreateAuthNumView.onLoginCreateAuthNumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun loginVerifyAuthNum(
        loginVerifyAuthNumView: LoginVerifyAuthNumView,
        phoneNumber: String,
        authNumber: String
    ) {
        val loginVerifyAuthNumService = ApplicationClass.retrofit.create(LoginRetrofitInterface::class.java)

        val phoneAuthNum = PhoneAuthNum(phoneNumber, authNumber)

        loginVerifyAuthNumService.loginVerifyAuthNum(phoneAuthNum).enqueue(object :
            Callback<LoginVerifyAuthNumResponse> {
            override fun onResponse(
                call: Call<LoginVerifyAuthNumResponse>,
                response: Response<LoginVerifyAuthNumResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> loginVerifyAuthNumView.onLoginVerifyAuthNumSuccess(resp.result)
                    else -> loginVerifyAuthNumView.onLoginVerifyAuthNumFailure(
                        resp.code,
                        resp.message
                    )
                }
            }

            override fun onFailure(call: Call<LoginVerifyAuthNumResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                loginVerifyAuthNumView.onLoginVerifyAuthNumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }


}