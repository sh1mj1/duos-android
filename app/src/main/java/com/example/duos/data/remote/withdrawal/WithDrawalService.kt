package com.example.duos.data.remote.withdrawal

import android.util.Log
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

object WithDrawalService {
    val TAG = "WithDrawalService"
    val retrofit = NetworkModule.getRetrofit()

    fun withDrawal(withdrawalListView: WithdrawalListView, userIdx: Int) {
        val withdrawalService = retrofit.create(WithdrawalRetrofitInterface::class.java)

        withdrawalService.onWithdrawal(userIdx).enqueue(object : Callback<WithdrawalResponse> {
            override fun onResponse(
                call: Call<WithdrawalResponse>,
                response: Response<WithdrawalResponse>
            ) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        resp.let {
                            withdrawalListView.onWithdrawalSuccess(it)
                            Log.d(TAG, resp.toString())
                            Log.d(TAG, resp.result.toString())
                        }
                    }
                    else -> withdrawalListView.onWithdrawalFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<WithdrawalResponse>, t: Throwable) {
                withdrawalListView.onWithdrawalFailure(400, "네트워크 오류 발생")
            }

        })
    }

}