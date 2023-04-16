package com.example.duos.data.remote.partnerProfile

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.block_report.BlockRequest
import com.example.duos.data.entities.block_report.ReportRequest
import com.example.duos.data.entities.profile.PartnerProfileListView
import com.example.duos.data.remote.block_report.BlockResponse
import com.example.duos.data.remote.block_report.ReportResponse
import com.example.duos.data.remote.dailyMatching.DailyMatchingRetrofitInterface
import com.example.duos.data.remote.dailyMatching.DailyMatchingService
import com.example.duos.ui.main.dailyMatching.DailyMatchingReportView
import com.example.duos.ui.main.partnerSearch.PartnerBlockView
import com.example.duos.ui.main.partnerSearch.PartnerReportView
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

    fun partnerBlock(
        blockView: PartnerBlockView,
        partnerBlockRequest : BlockRequest
    ) {
        val partnerProfileService =
            retrofit.create(PartnerProfileRetrofitInterface::class.java)

        partnerProfileService.partnerBlock(partnerBlockRequest)
            .enqueue(object :
                Callback<BlockResponse> {
                override fun onResponse(
                    call: Call<BlockResponse>,
                    response: Response<BlockResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1130 -> blockView.onPartnerBlockSuccess()
                        else -> blockView.onPartnerBlockFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<BlockResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    blockView.onPartnerBlockFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }

    fun partnerReport(
        reportView: PartnerReportView,
        partnerReportRequest: ReportRequest
    ) {
        val partnerProfileService =
            retrofit.create(PartnerProfileRetrofitInterface::class.java)

        partnerProfileService.partnerReport(partnerReportRequest)
            .enqueue(object :
                Callback<ReportResponse> {
                override fun onResponse(
                    call: Call<ReportResponse>,
                    response: Response<ReportResponse>
                ) {
                    val resp = response.body()!!
                    Log.d("resp", resp.toString())
                    when (resp.code) {
                        1141 -> reportView.onPartnerReportSuccess()
                        else -> reportView.onPartnerReportFailure(
                            resp.code,
                            resp.message
                        )
                    }
                }

                override fun onFailure(call: Call<ReportResponse>, t: Throwable) {
                    Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                    reportView.onPartnerReportFailure(400, "네트워크 오류가 발생했습니다.")
                }
            })
    }
}
