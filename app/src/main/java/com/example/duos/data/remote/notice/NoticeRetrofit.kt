package com.example.duos.data.remote.notice

import com.example.duos.ApplicationClass.Companion.NOTICE_API
import com.example.duos.ApplicationClass.Companion.NOTICE_DELETE_API
import retrofit2.Call
import retrofit2.http.*

interface NoticeGetRetrofitInterface {
    @GET(NOTICE_API)
    fun onGetNotice(): Call<NoticeGetResponse>
}

interface NoticePutRetrofitInterface {
    @DELETE(NOTICE_DELETE_API)
    fun onDeleteNotice(@Path("noticeIdx") noticeIdx : Int): Call<NoticeDeleteResponse>
}

interface NoticePostRetrofitInterface {
    @POST(NOTICE_API)
    fun onPostNotice(): Call<NoticePostResponse>
}

interface NoticePatchRetrofitInterface {
    @PATCH(NOTICE_API)
    fun onPatchNotice(): Call<NoticePatchResponse>
}