package com.example.duos.ui.main.dailyMatching

interface DailyMatchingDeleteView {
    fun onDailyMatchingDeleteSuccess()
    fun onDailyMatchingDeleteFailure(code: Int, message: String)
}