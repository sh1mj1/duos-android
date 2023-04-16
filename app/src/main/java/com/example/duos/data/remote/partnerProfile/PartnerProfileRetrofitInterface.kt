package com.example.duos.data.remote.partnerProfile

import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.PARTNER_PROFILE_API
import com.example.duos.data.entities.block_report.BlockRequest
import com.example.duos.data.entities.block_report.ReportRequest
import com.example.duos.data.remote.block_report.BlockResponse
import com.example.duos.data.remote.block_report.ReportResponse
import com.example.duos.data.remote.myProfile.MyProfileResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

// https://duos.co.kr/api/partners/profile?userIdx={userIdx}&partnerIdx={partnerIdx}
interface PartnerProfileRetrofitInterface {

    @GET(PARTNER_PROFILE_API)
    fun getPartnerProfile(
        @Query("userIdx") userIdx: Int,
        @Query("partnerIdx") partnerIdx: Int
    ): Call<PartnerProfileResponse>

    @POST("/api/block")
    fun partnerBlock(
        @Body blockRequest: BlockRequest
    ) : Call<BlockResponse>

    @POST("/api/report")
    fun partnerReport(
        @Body reportRequest: ReportRequest
    ) : Call<ReportResponse>

}
