//package com.example.duos.data.remote.PartnersPage
//
//import com.example.duos.data.remote.partnerSearch.PartnerSearchDataResponse
//import com.google.gson.JsonElement
//import retrofit2.Call
//import retrofit2.http.GET
//import retrofit2.http.Query
//
//interface PartnerPageRetrofitInterface {
//
//    // https://duos.co.kr/api/partners/profile?userIdx={userIdx}&partnerIdx={partnerIdx}
//
////    @GET("/api/partners/profile?userIdx={1}&partnerIdx={2}")
////    fun goPartnerPage(@Query("query")userIdx : String)
//
//    @GET(/api/partners/profile?userIdx={userIdx}&partnerIdx={partnerIdx})
//    fun goPartnerPage(@Query("query") userIdx:Int, partnerIdx:Int) : Call<JsonElement>
//
//
//
////    @GET("/test/search/partner")
////    fun partnerSearchDataList(
////        @Query("userIdx") userIdx: Int
////    ): Call<PartnerSearchDataResponse>
//}