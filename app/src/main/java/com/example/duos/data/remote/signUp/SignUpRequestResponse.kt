package com.example.duos.data.remote.signUp

import com.google.gson.annotations.SerializedName

data class SignUpRequestResultData(@SerializedName("isCreated") val isCreated : Boolean,
                                   @SerializedName("resultMessage") val resultMessage : String
)

data class SignUpRequestResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpRequestResultData
)