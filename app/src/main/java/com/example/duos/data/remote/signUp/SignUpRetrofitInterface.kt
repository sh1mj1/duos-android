package com.example.duos.data.remote.signUp


import com.example.duos.data.entities.PhoneAuthNum
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


interface SignUpRetrofitInterface {
    @POST("/api/signup/auth/num")
    fun signUpCreateAuthNum(@Body phoneNum : String): Call<SignUpCreateAuthNumResponse>

    @POST("/api/signup/auth/num")
    fun signUpVerifyAuthNum(@Body phoneAuthNum : PhoneAuthNum): Call<SignUpVerifyAuthNumResponse>

}