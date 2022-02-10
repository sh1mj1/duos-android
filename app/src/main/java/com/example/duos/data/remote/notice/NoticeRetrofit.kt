package com.example.duos.data.remote.notice

import com.example.duos.ApplicationClass.Companion.NOTICE_GET_API
import retrofit2.Call
import retrofit2.http.*

interface NoticeGetRetrofitInterface {
    @GET(NOTICE_GET_API)
    fun onGetNotice(): Call<NoticeGetResponse>
}

//interface NoticeDeleteRetrofitInterface {
//    @DELETE("api/notice/{noticeIdx}")
//    fun onDeleteNotice(@Path("noticeIdx") noticeIdx: Int): Call<NoticeDeleteResponse>
//}
//
//interface NoticePostRetrofitInterface {
//    @POST(NOTICE_API)
//    fun onPostNotice(
//        @Body noticePostReqDto: NoticePostReqDto
//    ): Call<NoticePostResponse>
//}
//
//interface NoticePatchRetrofitInterface {
//    @PATCH(NOTICE_API)
//    fun onPatchNotice(
//        @Body noticePatchReqDto: NoticePatchReqDto
//    ): Call<NoticePatchResponse>
//}