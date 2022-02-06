package com.example.duos.data.remote.partnerSearch

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PartnerSearchRetrofitInterface {
    @GET("/api/partners")
    fun partnerSearchDataList(
        @Query("userIdx") userIdx: Int
    ): Call<PartnerSearchDataResponse>

    @GET("/api/partners/search")
    fun partnerSearchFilterDataList(
        @Query("userIdx") userIdx: Int,
        @Query("gender") gender: Int,
        @Query("ageStart") ageStart : Int,
        @Query("ageEnd") ageEnd : Int,
        @Query("experienceStart") experienceStart : Int,
        @Query("experienceEnd") experienceEnd : Int,
        @Query("locationIdx") locationIdx : Int
    )
}