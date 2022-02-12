package com.example.duos.data.remote.login

import com.example.duos.data.entities.LoginAuthNum

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginRetrofitInterface {
    @POST("/api/login/auth/num")
    fun loginCreateAuthNum(@Body phoneNum: String): Call<LoginCreateAuthNumResponse>

    @POST("/api/login/auth")
    fun loginVerifyAuthNum(@Body loginAuthNum: LoginAuthNum): Call<LoginVerifyAuthNumResponse>

}