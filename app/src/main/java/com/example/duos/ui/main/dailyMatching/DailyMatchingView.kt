package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.remote.dailyMatching.AllDailyMatchingListResult
import com.example.duos.data.remote.dailyMatching.DailyMatchingSearchResultData
import com.example.duos.data.remote.dailyMatching.ImminentDailyMatchingListResult
import com.example.duos.data.remote.dailyMatching.PopularDailyMatchingListResult

interface AllDailyMatchingView {
    fun onGetAllDailyMatchingViewSuccess(allDailyMatchingListResult: AllDailyMatchingListResult)
    fun  onGetAllDailyMatchingViewFailure(code: Int, message: String)
}

interface PopularDailyMatchingView {
    fun onGetPopularDailyMatchingViewSuccess(popularDailyMatchingListResult: PopularDailyMatchingListResult)
    fun  onGetPopularDailyMatchingViewFailure(code: Int, message: String)
}

interface ImminentDailyMatchingView {
    fun onGetImminentDailyMatchingViewSuccess(imminentDailyMatchingListResult: ImminentDailyMatchingListResult)
    fun  onGetImminentDailyMatchingViewFailure(code: Int, message: String)
}

interface DailyMatchingSearchView{
    fun onGetSearchViewSuccess(dailyMatchingSearchResultData : DailyMatchingSearchResultData)
    fun onGetSearchViewFailure(code : Int, message : String)
}