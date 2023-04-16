package com.example.duos.data.remote.partnerProfile

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.profile.PartnerProfileListView
import com.example.duos.data.remote.dailyMatching.DailyMatchingRetrofitInterface
import com.example.duos.data.remote.dailyMatching.DailyMatchingService
import com.example.duos.ui.main.dailyMatching.DailyMatchingReportView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

//    fun partnerProfileBlock(
//        blockView: PartnerProfileBlockView,
//        partnerProfileBlockRequest : PartnerProfileBlockRequest
//    ) {
//        val dailyMatchingBlockService =
//            PartnerProfileService.retrofit.create(PartnerProfileRetrofitInterface::class.java)
//
//        dailyMatchingBlockService.dailyMatchingBlock(dailyMatchingBlockRequest)
//            .enqueue(object :
//                Callback<DailyMatchingBlockResponse> {
//                override fun onResponse(
//                    call: Call<DailyMatchingBlockResponse>,
//                    response: Response<DailyMatchingBlockResponse>
//                ) {
//                    val resp = response.body()!!
//                    Log.d("resp", resp.toString())
//                    when (resp.code) {
//                        1130 -> blockView.onDailyMatchingBlockSuccess()
//                        else -> blockView.onDailyMatchingBlockFailure(
//                            resp.code,
//                            resp.message
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<DailyMatchingBlockResponse>, t: Throwable) {
//                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
//                    blockView.onDailyMatchingBlockFailure(400, "네트워크 오류가 발생했습니다.")
//                }
//            })
//    }
//
//    fun dailyMatchingReport(
//        reportView: DailyMatchingReportView,
//        dailyMatchingReportRequest: DailyMatchingReportRequest
//    ) {
//        val dailyMatchingMessageService =
//            DailyMatchingService.retrofit.create(DailyMatchingRetrofitInterface::class.java)
//
//        dailyMatchingMessageService.dailyMatchingReport(dailyMatchingReportRequest)
//            .enqueue(object :
//                Callback<DailyMatchingReportResponse> {
//                override fun onResponse(
//                    call: Call<DailyMatchingReportResponse>,
//                    response: Response<DailyMatchingReportResponse>
//                ) {
//                    val resp = response.body()!!
//                    Log.d("resp", resp.toString())
//                    when (resp.code) {
//                        1141 -> reportView.onDailyMatchingReportSuccess()
//                        else -> reportView.onDailyMatchingReportFailure(
//                            resp.code,
//                            resp.message
//                        )
//                    }
//                }
//
//                override fun onFailure(call: Call<DailyMatchingReportResponse>, t: Throwable) {
//                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
//                    reportView.onDailyMatchingReportFailure(400, "네트워크 오류가 발생했습니다.")
//                }
//            })
//    }
}
