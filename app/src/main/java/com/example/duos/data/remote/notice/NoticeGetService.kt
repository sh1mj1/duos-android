package com.example.duos.data.remote.notice

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.notice.NoticeListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object NoticeGetService {
    val TAG = "NoticeGetService"
    val retrofit = NetworkModule.getRetrofit()

    fun onGetNotice(noticeListView: NoticeListView) {
        val noticeGetService = retrofit.create(NoticeGetRetrofitInterface::class.java)

        noticeGetService.onGetNotice().enqueue(object : Callback<NoticeGetResponse> {
            override fun onResponse(
                call: Call<NoticeGetResponse>,
                response: Response<NoticeGetResponse>
            ) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        resp.result.let {
                            noticeListView.onGetNoticeSuccess(resp)
                            Log.d(TAG, resp.toString())
                            Log.d(TAG, resp.result.toString())
                        }
                    }
                    else -> Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
                }
            }

            override fun onFailure(call: Call<NoticeGetResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
            }

        })

    }

}