package com.example.duos.data.remote.dailyMatching

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DailyMatchingRetrofitInterface {
    @POST("/api/board/list")
    fun getAllDailyMatchingList(@Body dailyMatchingListRequesetBody: DailyMatchingListRequesetBody): Call<AllDailyMatchingListResponse>

    @POST("/api/board/list/popular")
    fun getPopularDailyMatchingList(@Body dailyMatchingListRequesetBody: DailyMatchingListRequesetBody): Call<PopularDailyMatchingListResponse>

    @POST("/api/board/list/imminent")
    fun getImminentDailyMatchingList(@Body dailyMatchingListRequesetBody: DailyMatchingListRequesetBody): Call<ImminentDailyMatchingListResponse>

}