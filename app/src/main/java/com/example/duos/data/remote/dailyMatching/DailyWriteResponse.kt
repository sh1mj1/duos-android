package com.example.duos.data.remote.dailyMatching

import com.google.gson.annotations.SerializedName

data class DailyWriteResult ( @SerializedName("boardIdx") val boradIdx : Int)

data class DailyWriteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: DailyWriteResult
)
