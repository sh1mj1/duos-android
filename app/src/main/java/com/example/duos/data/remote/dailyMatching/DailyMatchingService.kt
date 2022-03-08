package com.example.duos.data.remote.dailyMatching

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.dailyMatching.DailyMatchingMessageParticipantIdx
import com.example.duos.ui.main.dailyMatching.DailyMatchingDeleteView
import com.example.duos.ui.main.dailyMatching.DailyMatchingEndView
import com.example.duos.ui.main.dailyMatching.DailyMatchingMessageView
import com.example.duos.ui.main.dailyMatching.GetDailyMatchingOptionView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DailyMatchingService {
    val retrofit = NetworkModule.getRetrofit()
    fun getDailyMatchingOption(
        optionView: GetDailyMatchingOptionView,
        boardIdx: Int, usrIdx: Int
    ) {
        val dailyMatchingOptionService =
            retrofit.create(DailyMatchingRetrofitInterface::class.java)

        dailyMatchingOptionService.dailyMatchingOption(boardIdx, usrIdx)
            .enqueue(object :
                Callback<DailyMatchingOptionResponse> {
                override fun onResponse(
                    call: Call<DailyMatchingOptionResponse>,
                    response: Response<DailyMatchingOptionResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1000 ->
                            resp.result?.let {
                                optionView.onGetDailyMatchingOptionSuccess(
                                    resp.result
                                )
                            }
                        else -> optionView.onGetDailyMatchingOptionFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<DailyMatchingOptionResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    optionView.onGetDailyMatchingOptionFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }

    fun dailyMatchingEnd(
        endView: DailyMatchingEndView,
        boardIdx: Int, usrIdx: Int
    ) {
        val dailyMatchingEndService =
            retrofit.create(DailyMatchingRetrofitInterface::class.java)

        dailyMatchingEndService.dailyMatchingEnd(boardIdx, usrIdx)
            .enqueue(object :
                Callback<DailyMatchingEndResponse> {
                override fun onResponse(
                    call: Call<DailyMatchingEndResponse>,
                    response: Response<DailyMatchingEndResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1125 -> endView.onDailyMatchingEndSuccess()
                        else -> endView.onDailyMatchingEndFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<DailyMatchingEndResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    endView.onDailyMatchingEndFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }

    fun dailyMatchingDelete(
        deleteView: DailyMatchingDeleteView,
        boardIdx: Int, usrIdx: Int
    ) {
        val dailyMatchingDeleteService =
            retrofit.create(DailyMatchingRetrofitInterface::class.java)

        dailyMatchingDeleteService.dailyMatchingDelete(boardIdx, usrIdx)
            .enqueue(object :
                Callback<DailyMatchingDeleteResponse> {
                override fun onResponse(
                    call: Call<DailyMatchingDeleteResponse>,
                    response: Response<DailyMatchingDeleteResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1124 -> deleteView.onDailyMatchingDeleteSuccess()
                        else -> deleteView.onDailyMatchingDeleteFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<DailyMatchingDeleteResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    deleteView.onDailyMatchingDeleteFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }

    fun dailyMatchingMessage(
        messageView: DailyMatchingMessageView, boardIdx: Int,
        dailyMatchingMessageParticipantIdx : DailyMatchingMessageParticipantIdx
    ) {
        val dailyMatchingMessageService =
            retrofit.create(DailyMatchingRetrofitInterface::class.java)

        dailyMatchingMessageService.dailyMatchingMessage(boardIdx, dailyMatchingMessageParticipantIdx)
            .enqueue(object :
                Callback<DailyMatchingMessageResponse> {
                override fun onResponse(
                    call: Call<DailyMatchingMessageResponse>,
                    response: Response<DailyMatchingMessageResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1000 -> messageView.onDailyMatchingMessageSuccess(resp.result)
                        else -> messageView.onDailyMatchingMessageFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<DailyMatchingMessageResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    messageView.onDailyMatchingMessageFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }
}