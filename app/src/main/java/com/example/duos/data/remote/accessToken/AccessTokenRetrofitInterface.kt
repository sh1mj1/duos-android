package com.example.duos.data.remote.accessToken

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface AccessTokenRetrofitInterface {
    @POST("/api/accesstoken")
    fun getAccessToken(
        @Header("jwtAccessToken") jwtAccessToken : String,
        @Header("jwtRefreshToken") jwtRefreshToken : String,
        @Body userIdx : Int
    ): Call<AccessTokenResponse>
}