package com.example.duos.data.remote.dailyMatching

import com.example.duos.data.remote.appointment.ShowAppointmentResponse
import com.example.duos.data.remote.signUp.SignUpRequestResponse
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
    ): Call<ShowAppointmentResponse>

}