package com.example.duos.data.remote.login

import com.google.gson.annotations.SerializedName

data class LoginCreateAuthNumResultData (@SerializedName("isPhoneNumAvail") val isPhoneNumAvail: Boolean,
                                          @SerializedName("resultMessage") val resultMessage: String)

data class LoginCreateAuthNumResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LoginCreateAuthNumResultData
)
