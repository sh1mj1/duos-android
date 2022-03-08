package com.example.duos.data.entities.dailyMatching

import com.google.gson.annotations.SerializedName
import java.io.Serializable
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


data class DailyMatchingDetail (
    @SerializedName("title") val title : String,
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("profileUrl") val profileUrl : String,
    @SerializedName("rating") val rating : Float,
    @SerializedName("review_Num") val review_Num : Int,
    @SerializedName("nickname") val nickname : String,
    @SerializedName("content") val content : String,
    @SerializedName("matchDate") val matchDate : String,
    @SerializedName("dayofWeek") val dayOfWeek : String,
    @SerializedName("matchPlace") val matchPlace : String,
    @SerializedName("startTime") val startTime : String,
    @SerializedName("endTime") val endTime : String,
    @SerializedName("duration") val duration : Int,
    @SerializedName("viewCount") val viewCount : Int,
    @SerializedName("messageCount") val messageCount : Int,
    @SerializedName("urls") val urls : List<String>,
    @SerializedName("regBefore") val regBefore : String,
    @SerializedName("stringForMatchDateGap") val stringForMatchDateGap : String
) : Serializable

data class DailyMatchingOption(
    @SerializedName("options") val options : List<String>
)

data class DailyMatchingMessageParticipantIdx(
    @SerializedName("thisUserIdx") val thisUserIdx : Int,
    @SerializedName("targetUserIdx") val targetUserIdx : Int
)

data class DailyMatchingMessageResult(
    @SerializedName("remains") val remains : Int,
    @SerializedName("createdNewChatRoom") val createdNewChatRoom : Boolean,
    @SerializedName("targetChatRoomIdx") val targetChatRoomIdx : String,
    @SerializedName("participantList") val participantList : List<Int>
)