package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.entities.dailyMatching.DailyMatchingDetail
import com.example.duos.data.remote.dailyMatching.DailyWriteResult

interface GetDailyMatchingDetailView {
    fun onGetDailyMatchingDetailSuccess(dailyMatchingDetail: DailyMatchingDetail)
    fun onGetDailyMatchingDetailFailure(code: Int, message: String)
}