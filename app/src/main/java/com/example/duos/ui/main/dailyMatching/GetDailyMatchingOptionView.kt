package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.entities.dailyMatching.DailyMatchingDetail
import com.example.duos.data.entities.dailyMatching.DailyMatchingOption

interface GetDailyMatchingOptionView {
    fun onGetDailyMatchingOptionSuccess(dailyMatchingOption: DailyMatchingOption)
    fun onGetDailyMatchingOptionFailure(code: Int, message: String)
}