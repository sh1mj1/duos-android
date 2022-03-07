package com.example.duos.data.remote.dailyMatching

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ui.main.dailyMatching.DailyMatchingWriteView
import com.example.duos.utils.NetworkModule
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object DailyMatchingWriteService {
    val retrofit = NetworkModule.getRetrofit()

    fun dailyMatchingWriteWithoutImg(dailyMatchingWriteView: DailyMatchingWriteView,  writeInfo: RequestBody) {
        val dailyWriteService =  retrofit.create(DailyMatchingRetrofitInterface::class.java)

        dailyWriteService.dailyMatchingWriteWithoutImg(writeInfo).enqueue(object :
            Callback<DailyWriteResponse> {
            override fun onResponse(
                call: Call<DailyWriteResponse>,
                response: Response<DailyWriteResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> dailyMatchingWriteView.onDailyMatchingWriteSuccess(resp.result)
                    else -> dailyMatchingWriteView.onDailyMatchingWriteFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<DailyWriteResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                dailyMatchingWriteView.onDailyMatchingWriteFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun dailyMatchingWriteWithImg(dailyMatchingWriteView: DailyMatchingWriteView, mFiles : List<MultipartBody.Part>, writeInfo: RequestBody) {
        val dailyWriteService =  retrofit.create(DailyMatchingRetrofitInterface::class.java)

        dailyWriteService.dailyMatchingWriteWithImg(mFiles, writeInfo).enqueue(object :
            Callback<DailyWriteResponse> {
            override fun onResponse(
                call: Call<DailyWriteResponse>,
                response: Response<DailyWriteResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> dailyMatchingWriteView.onDailyMatchingWriteSuccess(resp.result)
                    else -> dailyMatchingWriteView.onDailyMatchingWriteFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<DailyWriteResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                dailyMatchingWriteView.onDailyMatchingWriteFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}