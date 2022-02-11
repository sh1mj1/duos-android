package com.example.duos.data.remote.withdrawal

interface WithdrawalListView {

    fun onWithdrawalSuccess(withdrawalResponse: WithdrawalResponse)
    fun onWithdrawalFailure(code : Int, message : String)


}
