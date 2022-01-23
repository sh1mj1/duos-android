package com.example.duos.data.remote.partnerSearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RecommendedPartnerListRetrofitInterface {
    @GET("/test/search/partner")
    fun recommendedPartnerList(
        @Query("userIdx") userIdx: Int
    ): Call<RecommendedPartnerResponse>
}