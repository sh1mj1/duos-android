package com.example.duos.data.remote.signUp

import com.google.gson.annotations.SerializedName

data class SignUpCreateAuthNumResultData (@SerializedName("isPhoneNumAvail") val isPhoneNumAvail: Boolean,
                                          @SerializedName("resultMessage") val resultMessage: String)

data class SignUpCreateAuthNumResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpCreateAuthNumResultData
)