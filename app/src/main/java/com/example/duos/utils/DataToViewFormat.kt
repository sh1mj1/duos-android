package com.example.duos.utils

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.duos.data.entities.ChatType
import com.example.duos.data.entities.chat.ChatMessageItem
import org.threeten.bp.format.DateTimeFormatter
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ISO_DATE
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.temporal.ChronoUnit

class DataToViewFormat {

    fun toRatingStr(ratingFloat: Float): String {
        val ratingStr = Math.round(ratingFloat * 10) / 10
        return ratingStr.toString()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDateStr(date : String):String{
        val dateFromApi = LocalDateTime.parse(date, java.time.format.DateTimeFormatter.ISO_DATE_TIME)
        val dateDifference = ChronoUnit.DAYS.between(dateFromApi, LocalDateTime.now())
        var dateStr : String = ""
        if(dateDifference <= 0){    /* 오늘이면*/
            if(dateFromApi.hour <= 12){
                dateStr = "오늘 오전 ${dateFromApi.hour}시"
            }else{
                dateStr = "오늘 오후 ${dateFromApi.hour}시"
            }

        } else if(dateDifference <= 1){
            if(dateFromApi.hour <= 12){
                dateStr = "어제 오전 ${dateFromApi.hour}시"
            }else{
                dateStr = "어제 오후 ${dateFromApi.hour}시"
            }
        } else{
            dateStr = dateFromApi.toLocalDate().toString()
            //DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm:ss.SSSX" )
        }

        return dateStr
    }
}

// Log.d(TAG, "7일 이내 ${i} 번째 ${ChronoUnit.DAYS.between(LocalDateTime.parse(xx, ISO_DATE_TIME), now())} 일 전 약속?")