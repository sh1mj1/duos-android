package com.example.duos.data.remote.dailyMatching

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

data class DailyMatchingWriteRequestBody(
    val writeIdx : Int,
    val title : String,
    val content : String,
    val matchDate : LocalDate,
    val matchPlace : String,
    val startTime : LocalDateTime,
    val endTime : LocalDateTime

)
