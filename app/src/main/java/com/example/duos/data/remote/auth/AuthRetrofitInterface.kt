package com.example.duos.data.remote.auth

import com.example.duos.data.entities.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AuthRetrofitInterface {
    @POST("/users")
    fun signUp(@Body user: User): Call<AuthResponse>

    @POST("/users/login")
    fun login(@Body user: User): Call<AuthResponse>

    @GET("/users/auto-login")
    fun autoLogin(): Call<AuthResponse>

    @GET("v1/regcodes")
    fun getLargeReg(
        @Query("regcode_pattern") regcode_pattern : String? = null,
        @Query("is_ignore_zero") is_ignore_zero: String? = null
    ): Call<AuthResponse>

}