package com.example.duos.data.remote.signUp


import com.example.duos.data.entities.PhoneAuthNum
import com.example.duos.data.entities.SignUpRequestInfo
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface SignUpRetrofitInterface {
    @POST("/api/signup/auth/num")
    fun signUpCreateAuthNum(@Body phoneNum: String): Call<SignUpCreateAuthNumResponse>

    @POST("/api/signup/auth")
    fun signUpVerifyAuthNum(@Body phoneAuthNum: PhoneAuthNum): Call<SignUpVerifyAuthNumResponse>

    @GET("api/signup/dupli")
    fun signUpNickNameDupli(
        @Query("nickname") nickName: String
    ): Call<SignUpNickNameDuplicateResponse>

    @Multipart
    @POST("api/signup")
    fun signUpRequest(
        @Part("mFile") profileImg : MultipartBody.Part,
        @Part("createUserReqDTO") signUpRequestInfo : SignUpRequestInfo
    ): Call<SignUpRequestResponse>
}
