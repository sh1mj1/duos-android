package com.example.duos.data.remote.signUp


import com.example.duos.data.entities.PhoneAuthNum
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface SignUpRetrofitInterface {
    @POST("/api/signup/auth/num")
    fun signUpCreateAuthNum(@Body phoneNum : String): Call<SignUpCreateAuthNumResponse>

    @POST("/api/signup/auth")
    fun signUpVerifyAuthNum(@Body phoneAuthNum : PhoneAuthNum): Call<SignUpVerifyAuthNumResponse>

    @GET("api/signup/dupli")
    fun signUpNickNameDupli(
        @Query("nickname") nickName: String
    ): Call<SignUpNickNameDuplicateResponse>


}