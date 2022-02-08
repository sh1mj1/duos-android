package com.example.duos.data.remote.partnerProfile

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ui.main.mypage.myprofile.PartnerProfileListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

object PartnerProfileService {
    val retrofit = NetworkModule.getRetrofit()
    val TAG = "PartnerProfileService"
    fun partnerProfileInfo(partnerProfileListView: PartnerProfileListView, userIdx: Int, partnerIdx: Int){
        val partnerProfileService = retrofit.create(PartnerProfileRetrofitInterface::class.java)
        Log.d("PartnerProfileService", "CREATE_RETROFIT")
        partnerProfileService.getPartnerProfile(userIdx, partnerIdx).enqueue(object : Callback<PartnerProfileResponse>{
            override fun onResponse(call: Call<PartnerProfileResponse>, response: Response<PartnerProfileResponse>) {
                Log.d("PartnerProfileService", "ON_RESPONSE")
                val resp = response.body()!!
                when (resp.code){
                    1000 -> {
                        resp.result.let{
                            partnerProfileListView.onGetProfileInfoSuccess(it)
                            Log.d(TAG, resp.toString())
                            Log.d(TAG, "파트너 프로필 $resp.result.partnerInfoDto.toString()")
                            Log.d(TAG, "파트너의 후기 $resp.result.reviewResDto.toString()")
                        }
                    }
                    else -> partnerProfileListView.onGetProfileInfoFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<PartnerProfileResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                partnerProfileListView.onGetProfileInfoFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}
