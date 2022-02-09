package com.example.duos.data.remote.myFriendList

import com.example.duos.data.entities.RecommendedFriend
import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import java.time.LocalDateTime

data class RecommendedFriendListOnDate(@SerializedName("recommendedAt") val recommendedDate: org.threeten.bp.LocalDateTime,
                                       @SerializedName("recommendPartnerDummyDtoList") val recommendedFriendList : List<RecommendedFriend>)

data class RecommendedFriendResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<RecommendedFriendListOnDate>?
)
