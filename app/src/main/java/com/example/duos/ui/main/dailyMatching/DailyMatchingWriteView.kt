package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.remote.dailyMatching.DailyWriteResponse
import com.example.duos.data.remote.dailyMatching.DailyWriteResult
import com.example.duos.data.remote.dailyMatching.PopularDailyMatchingListResult

interface DailyMatchingWriteView {
    fun onDailyMatchingWriteSuccess(dailyWriteResult: DailyWriteResult)
    fun onDailyMatchingWriteFailure(code: Int, message: String)
}