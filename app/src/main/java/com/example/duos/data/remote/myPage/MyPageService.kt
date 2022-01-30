package com.example.duos.data.remote.myPage

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.BASE_URL
import com.example.duos.ApplicationClass.Companion.DEV_URL
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


// 싱글턴
object MyPageService {

    private const val TAG: String = "MyPageService"
    fun getMyPageService(myPageService: MyPageService, userIdx: Int) {

        val myPageService = ApplicationClass.retrofit.create(MyPageRetrofitInterface::class.java)

        myPageService.getUserPage(userIdx).enqueue(object : Callback<MyPageResponse> {

            override fun onResponse(call: Call<MyPageResponse>, response: Response<MyPageResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        resp.let {
                            Log.d(TAG, resp.toString())
//                            it.result.forEach { myPageListItem ->
//                                Log.d(TAG, myPageListItem.toString())
                            Log.d(TAG, it.result.userIdx.toString()); Log.d(TAG, it.result.nickname.toString())
                            Log.d(TAG, it.result.phoneNumber.toString()); Log.d(TAG, it.result.experience.toString())
                            Log.d(TAG, it.result.gamesCount.toString())
                        }
                    }
                    else -> {
                        resp.code
                        Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
                    }

                }

            }

            override fun onFailure(call: Call<MyPageResponse>, t: Throwable) {
                Log.d("${TAG}/API-ERROR", t.message.toString())
//                Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
                // 네트워크 오류 발생
            }


        })

    }


}



