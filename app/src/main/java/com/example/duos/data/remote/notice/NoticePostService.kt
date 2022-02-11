//package com.example.duos.data.remote.notice
//
//import android.util.Log
//import com.example.duos.data.entities.notice.NoticeListView
//import com.example.duos.data.entities.notice.NoticePostReqDto
//import com.example.duos.utils.NetworkModule
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//object NoticePostService {
//    val TAG = "NoticePostService"
//    val retrofit = NetworkModule.getRetrofit()
//
//    fun onPostNotice(noticeListView: NoticeListView, title: String, body: String) {
//        val noticePostService = retrofit.create(NoticePostRetrofitInterface::class.java)
//        val noticePostReqDto = NoticePostReqDto(title, body)
//
//        noticePostService.onPostNotice(noticePostReqDto)
//            .enqueue(object : Callback<NoticePostResponse> {
//                override fun onResponse(
//                    call: Call<NoticePostResponse>,
//                    response: Response<NoticePostResponse>
//                ) {
//                    val resp = response.body()!!
//                    when (resp.code) {
//                        7000 -> {
//                            resp.let {
//                                noticeListView.onPostNoticeSuccess(it)
//                                Log.d(TAG, resp.toString())
//                            }
//                        }
//                        else -> {
//                            Log.d(TAG, "CODE : ${resp.code} , MESSAGE : ${resp.message}")
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<NoticePostResponse>, t: Throwable) {
//                    noticeListView.onPostNoticeFailure(400, "네트워크 오류 발생")
//                }
//
//            })
//    }
//}