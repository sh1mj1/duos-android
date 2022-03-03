package com.example.duos.data.entities.dailyMatching

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.util.*


data class DailyMatching(
    @SerializedName("boardIdx") val boardIdx : Int,
    @SerializedName("title") val title : String,
    @SerializedName("content") val content : String,
    @SerializedName("matchPlace") val matchPlace : String,
    @SerializedName("matchDate") val matchDate : Date,
    @SerializedName("startTime") val startTime : org.threeten.bp.LocalDateTime,
    @SerializedName("endTime") val endTime : org.threeten.bp.LocalDateTime,
    @SerializedName("recruitmentStatus") val recruitmentStatus : String,
    @SerializedName("userProfileImage") val userProfileImage : String,
    @SerializedName("userNickname") val userNickname : String,
    @SerializedName("userAge") val userAge : Int,
    @SerializedName("userGender") val userGender : Int,
    @SerializedName("viewCount") val viewCount : Int,
    @SerializedName("messageCount") val messageCount : Int
)
