package com.example.duos.data.remote.partnerSearch

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ui.main.partnerSearch.PartnerSearchView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PartnerSearchService {
    val retrofit = NetworkModule.getRetrofit()
    fun partnerSearchData(partnerSearchView: PartnerSearchView, userIdx: Int){
        val partnerSearchService = retrofit.create(PartnerSearchRetrofitInterface::class.java)

        partnerSearchService.partnerSearchDataList(userIdx).enqueue(object : Callback<PartnerSearchDataResponse>{
            override fun onResponse(
                call: Call<PartnerSearchDataResponse>,
                response: Response<PartnerSearchDataResponse>
            ) {
                val resp = response.body()!!
                when (resp.code){
                    1000 ->
                        resp.result?.let { partnerSearchView.onGetPartnerSearchDataSuccess(it) }
                    else -> partnerSearchView.onGetPartnerSearchDataFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<PartnerSearchDataResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                partnerSearchView.onGetPartnerSearchDataFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}