package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.remote.dailyMatching.DailyWriteResult

interface DailyMatchingEditView {
    fun onDailyMatchingEditSuccess(dailyWriteResult: DailyWriteResult)
    fun onDailyMatchingEditFailure(code: Int, message: String)
}