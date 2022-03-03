package com.example.duos.data.remote.dailyMatching

import com.google.gson.annotations.SerializedName

data class AllDailyMatchingListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AllDailyMatchingListResult
)


data class PopularDailyMatchingListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PopularDailyMatchingListResult
)


data class ImminentDailyMatchingListResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: ImminentDailyMatchingListResult
)
