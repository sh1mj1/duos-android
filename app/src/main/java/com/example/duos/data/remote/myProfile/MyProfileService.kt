package com.example.duos.data.remote.myProfile

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ui.main.mypage.myprofile.frag.ProfileListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object MyProfileService {
    val retrofit = NetworkModule.getRetrofit()
    val TAG = "MyProfileService"
    fun myProfileInfo(profileListView: ProfileListView, userIdx: Int) {
        val myProfileService = retrofit.create(MyProfileRetrofitInterface::class.java)

        myProfileService.myProfile(userIdx).enqueue(object : Callback<MyProfileResponse> {
            override fun onResponse(call: Call<MyProfileResponse>, response: Response<MyProfileResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        resp.result.let { profileListView.onGetMyProfileInfoSuccess(it) }
                        Log.d(TAG, resp.toString())
                        Log.d(TAG, resp.result.toString())

                        Log.d(TAG, "내 프로필 정보 $resp.result.profileInfo.toString()")
                        Log.d(TAG, "내게 쓴 후기 $resp.result.reviews.toString()")
                    }
                    else -> profileListView.onGetMyProfileInfoFailure(resp.code, resp.message)
                }

            }

            override fun onFailure(call: Call<MyProfileResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                profileListView.onGetMyProfileInfoFailure(400, "네트워크 오류가 발생했습니다.")

            }
        })
    }

}