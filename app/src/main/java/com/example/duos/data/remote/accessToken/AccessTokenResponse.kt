package com.example.duos.data.remote.accessToken

import com.google.gson.annotations.SerializedName

data class AccessTokenResultData (@SerializedName("resultMessage") val resultMessage : String,
                                    @SerializedName("jwtAccessToken") val jwtAccessToken : String,
                                    @SerializedName("jwtRefreshToken") val jwtRefreshToken : String)

data class AccessTokenResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AccessTokenResultData
)
