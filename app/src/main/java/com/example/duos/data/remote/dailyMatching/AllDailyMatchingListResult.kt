package com.example.duos.data.remote.dailyMatching

import com.example.duos.data.entities.dailyMatching.DailyMatching
import com.example.duos.data.remote.chat.chat.MessageListData
import com.example.duos.data.remote.chat.chat.PagingChatMessageResult
import com.google.gson.annotations.SerializedName

data class AllDailyMatchingListResult(
    @SerializedName("currentPage") val currentPage : Int,
    @SerializedName("currentItemCnt") val currentItemCnt: Int,
    @SerializedName("isNextPageExists") val isNextPageExists : Boolean,
    @SerializedName("dailyMatchingList") val dailyMatching: List<DailyMatching>
)


data class PopularDailyMatchingListResult(
    @SerializedName("currentPage") val currentPage : Int,
    @SerializedName("currentItemCnt") val currentItemCnt: Int,
    @SerializedName("isNextPageExists") val isNextPageExists : Boolean,
    @SerializedName("popularList") val popularList: List<DailyMatching>
)


data class ImminentDailyMatchingListResult(
    @SerializedName("currentPage") val currentPage : Int,
    @SerializedName("currentItemCnt") val currentItemCnt: Int,
    @SerializedName("isNextPageExists") val isNextPageExists : Boolean,
    @SerializedName("aboutToCloseList") val aboutToCloseList: List<DailyMatching>
)
