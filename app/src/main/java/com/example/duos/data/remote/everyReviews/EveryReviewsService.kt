package com.example.duos.data.remote.everyReviews

import android.util.Log
import com.example.duos.ui.main.mypage.myprofile.EveryReviewsItemView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EveryReviewsService {
    private const val TAG: String = "EveryReviewsService"
    val retrofit = NetworkModule.getRetrofit()

    fun getEveryReviews(everyReviewsItemView: EveryReviewsItemView, userIdx: Int) {
        val everyReviewsService = retrofit.create(EveryReviewsRetrofitInterface::class.java)
        everyReviewsService.getEveryReviews(userIdx).enqueue(object : Callback<EveryReviewsResponse> {
            override fun onResponse(call: Call<EveryReviewsResponse>, response: Response<EveryReviewsResponse>) {
                Log.d(TAG, "onResponse")
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        Log.d(TAG, "onGetEveryReviewsItemSuccess")
                        resp.let {
                            everyReviewsItemView.onGetEveryReviewsItemSuccess(it)
                            Log.d(TAG, it.result.toString())

                        }
                    }
                    else -> {
                        Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
                        everyReviewsItemView.onGetEveryReviewsItemFailure(resp.code, resp.message)
                    }
                }
            }

            override fun onFailure(call: Call<EveryReviewsResponse>, t: Throwable) {
                Log.d("${TAG}/API-ERROR", t.message.toString())
                everyReviewsItemView.onGetEveryReviewsItemFailure(400, "네트워크 오류 발생")
            }

        })
    }

}



