package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.entities.dailyMatching.DailyMatchingMessageResult
import com.example.duos.data.remote.dailyMatching.DailyWriteResult

interface DailyMatchingMessageView {
    fun onDailyMatchingMessageSuccess(dailyMatchingMessageResult: DailyMatchingMessageResult)
    fun onDailyMatchingMessageFailure(code: Int, message: String)
}