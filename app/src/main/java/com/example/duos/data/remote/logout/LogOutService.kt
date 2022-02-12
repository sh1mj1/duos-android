package com.example.duos.data.remote.logout

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object LogOutService {
    val retrofit = NetworkModule.getRetrofit()
    fun postLogOut(logoutListView: LogoutListView, userIdx: Int) {
        val logOutService = retrofit.create(LogoutRetrofitInterface::class.java)

        logOutService.onPostLogOut(userIdx).enqueue(object : Callback<LogOutResponse> {
            override fun onResponse(call: Call<LogOutResponse>, response: Response<LogOutResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> logoutListView.onLogOutSuccess()
                    else -> logoutListView.onLogOutFailure(resp.code, resp.message)

                }
            }

            override fun onFailure(call: Call<LogOutResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                logoutListView.onLogOutFailure(400, " 네트워크 오류 발생")
            }

        })
    }
}