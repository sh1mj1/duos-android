package com.example.duos.data.remote.dailyMatching

import com.example.duos.data.entities.block_report.BlockRequest
import com.example.duos.data.entities.block_report.ReportRequest
import com.example.duos.data.entities.dailyMatching.DailyMatchingMessageParticipantIdx
import com.example.duos.data.remote.block_report.BlockResponse
import com.example.duos.data.remote.block_report.ReportResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface DailyMatchingRetrofitInterface {
    @POST("/api/board/list")
    fun getAllDailyMatchingList(@Body dailyMatchingListRequesetBody: DailyMatchingListRequesetBody): Call<AllDailyMatchingListResponse>

    @POST("/api/board/list/popular")
    fun getPopularDailyMatchingList(@Body dailyMatchingListRequesetBody: DailyMatchingListRequesetBody): Call<PopularDailyMatchingListResponse>

    @POST("/api/board/list/imminent")
    fun getImminentDailyMatchingList(@Body dailyMatchingListRequesetBody: DailyMatchingListRequesetBody): Call<ImminentDailyMatchingListResponse>

    @Multipart
    @POST("/api/board")
    fun dailyMatchingWriteWithoutImg(
        @Part("registerBoardReqDto") writeInfo : RequestBody
    ) : Call<DailyWriteResponse>

    @Multipart
    @POST("/api/board")
    fun dailyMatchingWriteWithImg(
        @Part mFiles : List<MultipartBody.Part>,
        @Part("registerBoardReqDto") writeInfo : RequestBody
    ) : Call<DailyWriteResponse>

    @GET("/api/board/{boardIdx}")
    fun dailyMatchingShowDetail(
        @Path("boardIdx") boardIdx : Int,
        @Query("userIdx") userIdx : Int
    ): Call<DailyMatchingDetailResponse>

    @GET("/api/board/{boardIdx}/option")
    fun dailyMatchingOption(
        @Path("boardIdx") boardIdx : Int,
        @Query("userIdx") userIdx : Int
    ): Call<DailyMatchingOptionResponse>

    @PUT("/api/board/{boardIdx}/finish")
    fun dailyMatchingEnd(
        @Path("boardIdx") boardIdx : Int,
        @Query("userIdx") userIdx : Int
    ): Call<DailyMatchingEndResponse>

    @DELETE("/api/board/{boardIdx}/delete")
    fun dailyMatchingDelete(
        @Path("boardIdx") boardIdx : Int,
        @Query("userIdx") userIdx : Int
    ): Call<DailyMatchingDeleteResponse>

    @Multipart
    @PUT("/api/board")
    fun dailyMatchingEditWithoutImg(
        @Part("updateBoardReqDto") editInfo : RequestBody
    ) : Call<DailyWriteResponse>

    @Multipart
    @PUT("/api/board")
    fun dailyMatchingEditWithImg(
        @Part mFiles : List<MultipartBody.Part>,
        @Part("updateBoardReqDto") editInfo : RequestBody
    ) : Call<DailyWriteResponse>

    @POST("/api/board/{boardIdx}/message")
    fun dailyMatchingMessage(
        @Path("boardIdx") boardIdx : Int,
        @Body participantIdx : DailyMatchingMessageParticipantIdx
    ) : Call<DailyMatchingMessageResponse>

    @POST("/api/board/list/user")
    fun getMyDailyMatchingList(
        @Query("userIdx") userIdx : Int
    ): Call<MyDailyMatchingListResponse>
////////////////////////////////////////////////////////////////////////////////////////////////
    @GET("/api/board/search")
    fun dailyMatchingSearch(
        @Query("userIdx") userIdx : Int,
        @Query("searchParam") searchParam : String
    ) : Call<DailyMatchingSearchResponse>

    @POST("/api/block")
    fun dailyMatchingBlock(
        @Body blockRequest: BlockRequest
    ) : Call<BlockResponse>

    @POST("/api/report")
    fun dailyMatchingReport(
        @Body reportRequest: ReportRequest
    ) : Call<ReportResponse>
}