package com.example.duos.data.remote.dailyMatching

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ui.main.dailyMatching.AllDailyMatchingView
import com.example.duos.ui.main.dailyMatching.ImminentDailyMatchingView
import com.example.duos.ui.main.dailyMatching.PopularDailyMatchingView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DailyMatchingService {
    val retrofit = NetworkModule.getRetrofit()
    fun getAllDailyMatching(
        allDailyMatchingView: AllDailyMatchingView,
        dailyMatchingListRequesetBody: DailyMatchingListRequesetBody
    ) {
        val allDailyMatchingListService =
            retrofit.create(DailyMatchingRetrofitInterface::class.java)

        allDailyMatchingListService.getAllDailyMatchingList(dailyMatchingListRequesetBody)
            .enqueue(object :
                Callback<AllDailyMatchingListResponse> {
                override fun onResponse(
                    call: Call<AllDailyMatchingListResponse>,
                    response: Response<AllDailyMatchingListResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1000 ->
                            resp.result?.let {
                                allDailyMatchingView.onGetAllDailyMatchingViewSuccess(
                                    resp.result
                                )
                            }
                        else -> allDailyMatchingView.onGetAllDailyMatchingViewFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<AllDailyMatchingListResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    allDailyMatchingView.onGetAllDailyMatchingViewFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }

    fun getPopularDailyMatching(
        popularDailyMatchingView: PopularDailyMatchingView,
        dailyMatchingListRequesetBody: DailyMatchingListRequesetBody
    ) {
        val popularDailyMatchingListService =
            retrofit.create(DailyMatchingRetrofitInterface::class.java)

        popularDailyMatchingListService.getPopularDailyMatchingList(dailyMatchingListRequesetBody)
            .enqueue(object :
                Callback<PopularDailyMatchingListResponse> {
                override fun onResponse(
                    call: Call<PopularDailyMatchingListResponse>,
                    response: Response<PopularDailyMatchingListResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1000 ->
                            resp.result?.let {
                                popularDailyMatchingView.onGetPopularDailyMatchingViewSuccess(
                                    resp.result
                                )
                            }
                        else -> popularDailyMatchingView.onGetPopularDailyMatchingViewFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<PopularDailyMatchingListResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    popularDailyMatchingView.onGetPopularDailyMatchingViewFailure(
                        400,
                        "네트워크 오류가 발생했습니다."
                    )
                }
            })
    }

    fun getImminentDailyMatching(
        imminentDailyMatchingView: ImminentDailyMatchingView,
        dailyMatchingListRequesetBody: DailyMatchingListRequesetBody
    ) {
        val imminentDailyMatchingListService =
            DailyMatchingService.retrofit.create(DailyMatchingRetrofitInterface::class.java)

        imminentDailyMatchingListService.getImminentDailyMatchingList(dailyMatchingListRequesetBody)
            .enqueue(object :
                Callback<ImminentDailyMatchingListResponse> {
                override fun onResponse(
                    call: Call<ImminentDailyMatchingListResponse>,
                    response: Response<ImminentDailyMatchingListResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1000 ->
                            resp.result?.let {
                                imminentDailyMatchingView.onGetImminentDailyMatchingViewSuccess(
                                    resp.result
                                )
                            }
                        else -> imminentDailyMatchingView.onGetImminentDailyMatchingViewFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<ImminentDailyMatchingListResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    imminentDailyMatchingView.onGetImminentDailyMatchingViewFailure(
                        400,
                        "네트워크 오류가 발생했습니다."
                    )
                }
            })
    }
}

