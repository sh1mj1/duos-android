//package com.example.duos.data.remote.notice
//
//import android.util.Log
//import com.example.duos.data.entities.notice.NoticeListView
//import com.example.duos.data.entities.notice.NoticePatchReqDto
//import com.example.duos.utils.NetworkModule
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//
//object NoticePatchService {
//    val TAG = "NoticePatchService"
//    val retrofit = NetworkModule.getRetrofit()
//
//    fun onPatchNotice(
//        noticeListView: NoticeListView,
//        noticePatchReqDto: NoticePatchReqDto
//    ) {
//        val noticePatchService = retrofit.create(NoticePatchRetrofitInterface::class.java)
//
//        noticePatchService.onPatchNotice(noticePatchReqDto)
//            .enqueue(object : Callback<NoticePatchResponse> {
//                override fun onResponse(
//                    call: Call<NoticePatchResponse>,
//                    response: Response<NoticePatchResponse>
//                ) {
//                    val resp = response.body()!!
//                    when (resp.code) {
//                        7100 -> {
//                            resp.let {
//                                noticeListView.onPatchNoticeSuccess(it)
//                                Log.d(TAG, resp.toString())
//                            }
//                        }
//                    }
//                }
//
//                override fun onFailure(call: Call<NoticePatchResponse>, t: Throwable) {
//                    noticeListView.onPatchNoticeFailure(400, "네트워크 오류")
//                }
//
//
//            })
//    }
//}