package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.entities.dailyMatching.DailyMatchingDetail

interface DailyMatchingEndView {
    fun onDailyMatchingEndSuccess()
    fun onDailyMatchingEndFailure(code: Int, message: String)
}
