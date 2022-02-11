//package com.example.duos.data.remote.notice
//
//import android.util.Log
//import com.example.duos.data.entities.notice.NoticeDeleteReqDto
//import com.example.duos.data.entities.notice.NoticeListView
//import com.example.duos.utils.NetworkModule
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//object NoticeDeleteService {
//    val TAG = "NoticeDeleteService"
//    val retrofit = NetworkModule.getRetrofit()
//
//    fun onDeleteNotice(noticeListView: NoticeListView, noticeIdx: Int) {
//        val noticeDeleteService = retrofit.create(NoticeDeleteRetrofitInterface::class.java)
//
//        noticeDeleteService.onDeleteNotice(noticeIdx).enqueue(object :
//            Callback<NoticeDeleteResponse> {
//            override fun onResponse(
//                call: Call<NoticeDeleteResponse>,
//                response: Response<NoticeDeleteResponse>
//            ) {
//                val resp = response.body()!!
//                when (resp.code) {
//                    7200 -> {
//                        resp.let {
//                            noticeListView.onDeleteNoticeSuccess(it)
//                            Log.d(TAG, resp.toString())
//                        }
//                    }
//                    else -> Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
//
//                }
//            }
//
//            override fun onFailure(call: Call<NoticeDeleteResponse>, t: Throwable) {
//                noticeListView.onDeleteNoticFailure(400, "네트워크 오류 발생")
//            }
//
//        })
//
//    }
//}