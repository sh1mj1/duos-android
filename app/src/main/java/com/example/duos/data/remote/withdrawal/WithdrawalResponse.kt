package com.example.duos.data.remote.withdrawal

import com.google.gson.annotations.SerializedName

data class WithdrawalResponse(

    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result : WithdrawalResultDto
)

data class WithdrawalResultDto(

    @SerializedName("isDeleted") val isDeleted : Boolean,
    @SerializedName("resultMessage") val resultMessage : String

)

