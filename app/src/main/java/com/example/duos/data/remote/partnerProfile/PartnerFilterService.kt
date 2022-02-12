package com.example.duos.data.remote.partnerProfile

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ui.main.partnerSearch.PartnerFilterDialog
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object PartnerFilterService {
    val retrofit = NetworkModule.getRetrofit()
    val TAG = "PartnerFilterService"
    fun showPartnersByFilter(
        partnerFilterListView: PartnerFilterDialog.PartnerFilterDialogCallback,
        userIdx: Int,
        gender: Int,
        ageStart: Int,
        ageEnd: Int,
        experienceStart: Int,
        experienceEnd: Int,
        locationIdx: Int,

        ) {
        val partnerFilterService = retrofit.create(PartnerFilterRetrofitInterface::class.java)
        Log.d("TAG", "CREATE_RETROFIT")
        partnerFilterService.showPartnersByFilter(
            userIdx,
            gender,
            ageStart,
            ageEnd,
            experienceStart,
            experienceEnd,
            locationIdx
        ).enqueue(object : Callback<PartnerFilterResponse> {
            override fun onResponse(call: Call<PartnerFilterResponse>, response: Response<PartnerFilterResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        resp.result.let {
                            partnerFilterListView.onGetPartnerFilterSuccess(it)
                            Log.d(TAG, resp.toString())
                            Log.d(TAG, resp.result.toString())
                        }
                    }
                    else -> partnerFilterListView.onGetPartnerFilterFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<PartnerFilterResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                partnerFilterListView.onGetPartnerFilterFailure(400, "네트워크 실패")
            }

        })
    }
}

/*
        @Query("userIdx") userIdx : Int,
        @Query("gender") gender : Int,
        @Query("ageStart") ageStart : Int,
        @Query("ageEnd") ageEnd : Int,
        @Query("experienceStart") experienceStart : Int,
        @Query("experienceEnd") experienceEnd : Int,
        @Query("locationIdx") locationIdx : Int,
 */