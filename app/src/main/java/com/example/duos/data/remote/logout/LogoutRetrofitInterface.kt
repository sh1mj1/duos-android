package com.example.duos.data.remote.logout

import com.example.duos.ApplicationClass.Companion.LOGOUT_API
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LogoutRetrofitInterface {

    @POST(LOGOUT_API)
    fun onPostLogOut(@Body userIdx : Int) : Call<LogOutResponse>
}
/*
@POST(WITHDRAWAL_API)
    fun onWithdrawal (@Body userIdx : Int) : Call<WithdrawalResponse>
 */