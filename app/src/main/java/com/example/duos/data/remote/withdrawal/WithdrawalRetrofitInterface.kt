package com.example.duos.data.remote.withdrawal

import com.example.duos.ApplicationClass.Companion.WITHDRAWAL_API
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface WithdrawalRetrofitInterface {

    @POST(WITHDRAWAL_API)
    fun onWithdrawal (@Body userIdx : Int) : Call<WithdrawalResponse>

}