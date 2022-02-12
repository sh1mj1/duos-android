package com.example.duos.data.remote.partnerProfile

import com.example.duos.ApplicationClass.Companion.PARTER_FILTER_API
import com.example.duos.data.entities.RecommendedPartner
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PartnerFilterRetrofitInterface {

    ///api/partners/search?userIdx={userIdx}&gender={gender}&ageStart={ageStart}&ageEnd={ageEnd}&experienceStart={experienceStart}&experienceEnd={experienceEnd}&locationIdx={locationIdx}
    @GET(PARTER_FILTER_API)
    fun showPartnersByFilter(
        @Query("userIdx") userIdx : Int,
        @Query("gender") gender : Int,
        @Query("ageStart") ageStart : Int,
        @Query("ageEnd") ageEnd : Int,
        @Query("experienceStart") experienceStart : Int,
        @Query("experienceEnd") experienceEnd : Int,
        @Query("locationIdx") locationIdx : Int,
    ): Call<PartnerFilterResponse>

}




