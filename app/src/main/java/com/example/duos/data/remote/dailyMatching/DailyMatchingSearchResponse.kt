package com.example.duos.data.remote.dailyMatching

import com.google.android.datatransport.runtime.dagger.multibindings.IntoMap
import com.google.gson.annotations.SerializedName
import java.util.*


data class DailyMatchingSearchResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: DailyMatchingSearchResultData
)

data class DailyMatchingSearchResultData(
    @SerializedName("resultSize") val resultSize: Int,
    @SerializedName("searchResult") val searchResult: List<SearchResultItem>
)

data class SearchResultItem(
    @SerializedName("boardIdx") val boardIdx: Int,
    @SerializedName("title") val title: String,
    @SerializedName("content") val content: String,
    @SerializedName("matchPlace") val matchPlace: String,
    @SerializedName("matchDate") val matchDate: Date, // Date
    @SerializedName("startTime") val startTime: org.threeten.bp.LocalDateTime, // LocalDateTime
    @SerializedName("endTime") val endTime: org.threeten.bp.LocalDateTime,     // LocalDateTime
    @SerializedName("recruitmentStatus") val recruitmentStatus: String,
    @SerializedName("userProfileImage") val userProfileImage: String,
    @SerializedName("userNickname") val userNickname: String,
    @SerializedName("userAge") val userAge: Int,
    @SerializedName("userGender") val userGender: Int,
    @SerializedName("viewCount") val viewCount: Int,
    @SerializedName("messageCount") val messageCount: Int,
)

