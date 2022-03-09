package com.example.duos.ui.main.dailyMatching

import com.example.duos.data.entities.dailyMatching.DailyMatchingDetail
import com.example.duos.data.entities.dailyMatching.DailyMatchingMessageResult
import com.example.duos.data.entities.dailyMatching.DailyMatchingOption
import com.example.duos.data.entities.dailyMatching.MyDailyMatchingListResult
import com.example.duos.data.remote.dailyMatching.AllDailyMatchingListResult
import com.example.duos.data.remote.dailyMatching.DailyMatchingSearchResultData
import com.example.duos.data.remote.dailyMatching.DailyWriteResult
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
    fun onGetSearchViewLoading()
    fun onGetSearchViewFailure(code : Int, message : String)
}

interface DailyMatchingEditView {
    fun onDailyMatchingEditSuccess(dailyWriteResult: DailyWriteResult)
    fun onDailyMatchingEditFailure(code: Int, message: String)
}

interface DailyMatchingDeleteView {
    fun onDailyMatchingDeleteSuccess()
    fun onDailyMatchingDeleteFailure(code: Int, message: String)
}

interface DailyMatchingEndView {
    fun onDailyMatchingEndSuccess()
    fun onDailyMatchingEndFailure(code: Int, message: String)
}

interface DailyMatchingMessageView {
    fun onDailyMatchingMessageSuccess(dailyMatchingMessageResult: DailyMatchingMessageResult)
    fun onDailyMatchingMessageFailure(code: Int, message: String)
}

interface MyDailyMatchingView {
    fun onMyDailyMatchingViewSuccess(myDailyMatchingListResult: MyDailyMatchingListResult)
    fun onMyDailyMatchingViewFailure(code: Int, message: String)
}

interface DailyMatchingWriteView {
    fun onDailyMatchingWriteSuccess(dailyWriteResult: DailyWriteResult)
    fun onDailyMatchingWriteFailure(code: Int, message: String)
}

interface GetDailyMatchingDetailView {
    fun onGetDailyMatchingDetailSuccess(dailyMatchingDetail: DailyMatchingDetail)
    fun onGetDailyMatchingDetailFailure(code: Int, message: String)
}

interface GetDailyMatchingOptionView {
    fun onGetDailyMatchingOptionSuccess(dailyMatchingOption: DailyMatchingOption)
    fun onGetDailyMatchingOptionFailure(code: Int, message: String)
}