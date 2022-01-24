package com.example.duos.data.remote.partnerSearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PartnerSearchRetrofitInterface {
    @GET("/test/search/partner")
    fun partnerSearchDataList(
        @Query("userIdx") userIdx: Int
    ): Call<PartnerSearchDataResponse>
}