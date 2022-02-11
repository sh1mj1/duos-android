package com.example.duos.data.remote.duplicate

import com.google.gson.annotations.SerializedName

data class DuplicateNicknameResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)

