package com.example.duos.data.remote.dailyMatching

import android.util.Log
import androidx.appcompat.app.AppCompatDialog
import com.example.duos.ApplicationClass
import com.example.duos.CustomDialog
import com.example.duos.ui.main.dailyMatching.AllDailyMatchingView
import com.example.duos.ui.main.dailyMatching.DailyMatchingSearchView
import com.example.duos.ui.main.dailyMatching.ImminentDailyMatchingView
import com.example.duos.ui.main.dailyMatching.PopularDailyMatchingView
import com.example.duos.ui.main.dailyMatching.*
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DailyMatchingListService {

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


    fun searchDailyMatching(
        dailyMatchingSearchView: DailyMatchingSearchView,
        userIdx: Int, searchParam: String
    ) {

        val searchDailyMatchingListService =
            DailyMatchingListService.retrofit.create(DailyMatchingRetrofitInterface::class.java)

        searchDailyMatchingListService.dailyMatchingSearch(userIdx, searchParam).enqueue(object :
            Callback<DailyMatchingSearchResponse> {

            override fun onResponse(
                call: Call<DailyMatchingSearchResponse>,
                response: Response<DailyMatchingSearchResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())
                when(resp.code){
                    1000 -> resp.result.let{
                        dailyMatchingSearchView.onGetSearchViewSuccess(it)
                    }
                    else -> dailyMatchingSearchView.onGetSearchViewFailure(resp.code, resp.message)
                }
            }
            override fun onFailure(call: Call<DailyMatchingSearchResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                dailyMatchingSearchView.onGetSearchViewFailure(400, "네트워크 오류 발생")

            }

        })
    }


    fun getImminentDailyMatching(
        imminentDailyMatchingView: ImminentDailyMatchingView,
        dailyMatchingListRequesetBody: DailyMatchingListRequesetBody
    ) {
        val imminentDailyMatchingListService =
            DailyMatchingListService.retrofit.create(DailyMatchingRetrofitInterface::class.java)

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

                override fun onFailure(
                    call: Call<ImminentDailyMatchingListResponse>,
                    t: Throwable
                ) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    imminentDailyMatchingView.onGetImminentDailyMatchingViewFailure(
                        400,
                        "네트워크 오류가 발생했습니다."
                    )
                }
            })
    }



    fun getDailyMatchingDetail(
        getDailyMatchingDetailView: GetDailyMatchingDetailView,
        boardIdx : Int, usrIdx : Int
    ) {
        val getDailyMatchingDetailService = retrofit.create(DailyMatchingRetrofitInterface::class.java)

        getDailyMatchingDetailService.dailyMatchingShowDetail(boardIdx, usrIdx).enqueue(object :
            Callback<DailyMatchingDetailResponse> {
                override fun onResponse(
                    call: Call<DailyMatchingDetailResponse>,
                    response: Response<DailyMatchingDetailResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1000 ->
                            resp.result?.let {
                                getDailyMatchingDetailView.onGetDailyMatchingDetailSuccess(
                                    resp.result
                                )
                            }
                        else -> getDailyMatchingDetailView.onGetDailyMatchingDetailFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<DailyMatchingDetailResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    getDailyMatchingDetailView.onGetDailyMatchingDetailFailure(
                        400,
                        "네트워크 오류가 발생했습니다."
                    )
                }
            })
    }

    fun getMyDailyMatching(
        myDailyMatchingView: MyDailyMatchingView,
        userIdx : Int
    ) {
        val myDailyMatchingListService =
            retrofit.create(DailyMatchingRetrofitInterface::class.java)

        myDailyMatchingListService.getMyDailyMatchingList(userIdx)
            .enqueue(object :
                Callback<MyDailyMatchingListResponse> {
                override fun onResponse(
                    call: Call<MyDailyMatchingListResponse>,
                    response: Response<MyDailyMatchingListResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1000 ->
                            resp.result?.let {
                                myDailyMatchingView.onMyDailyMatchingViewSuccess(
                                    resp.result
                                )
                            }
                        else -> myDailyMatchingView.onMyDailyMatchingViewFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<MyDailyMatchingListResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    myDailyMatchingView.onMyDailyMatchingViewFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }
}

