package com.example.duos.data.remote.logout

import com.google.gson.annotations.SerializedName

data class LogOutResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
