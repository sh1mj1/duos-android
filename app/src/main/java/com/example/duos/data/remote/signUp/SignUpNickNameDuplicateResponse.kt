package com.example.duos.data.remote.signUp

import com.google.gson.annotations.SerializedName

data class SignUpNickNameDuplicateResultData(@SerializedName("isDuplicated") val isDuplicated : Boolean,
                                        @SerializedName("resultMessage") val resultMessage: String)

data class SignUpNickNameDuplicateResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpNickNameDuplicateResultData
)
