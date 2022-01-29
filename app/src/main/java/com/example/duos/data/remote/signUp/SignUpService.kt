package com.example.duos.data.remote.signUp

import android.text.Editable
import android.util.Log
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.ApplicationClass.Companion.retrofit
import com.example.duos.data.entities.PhoneAuthNum
import com.example.duos.ui.signup.SignUpCreateAuthNumView
import com.example.duos.ui.signup.SignUpVerifyAuthNumView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object SignUpService {
    fun signUpCreateAuthNum(signUpCreateAuthNumView: SignUpCreateAuthNumView, phoneNumber: String){
        val signUpCreateAuthNumService = retrofit.create(SignUpRetrofitInterface::class.java)

        signUpCreateAuthNumService.signUpCreateAuthNum(phoneNumber).enqueue(object :
        Callback<SignUpCreateAuthNumResponse>
        {
            override fun onResponse(call: Call<SignUpCreateAuthNumResponse>, response: Response<SignUpCreateAuthNumResponse>) {

                val resp = response.body()!!
                Log.d("resp",resp.toString())

                when(resp.code){
                    1000 -> signUpCreateAuthNumView.onSignUpCreateAuthNumSuccess()
                    else -> signUpCreateAuthNumView.onSignUpCreateAuthNumFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SignUpCreateAuthNumResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())

                signUpCreateAuthNumView.onSignUpCreateAuthNumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun signUpVerifyAuthNum(signUpVerifyAuthNumView: SignUpVerifyAuthNumView, phoneNumber: String, authNumber : String){
        val signUpVerifyAuthNumService = retrofit.create(SignUpRetrofitInterface::class.java)

        val phoneAuthNum = PhoneAuthNum(phoneNumber, authNumber)

        signUpVerifyAuthNumService.signUpVerifyAuthNum(phoneAuthNum).enqueue(object :
            Callback<SignUpVerifyAuthNumResponse>
        {
            override fun onResponse(call: Call<SignUpVerifyAuthNumResponse>, response: Response<SignUpVerifyAuthNumResponse>) {

                val resp = response.body()!!
                Log.d("resp",resp.toString())

                when(resp.code){
                    1000 -> signUpVerifyAuthNumView.onSignUpVerifyAuthNumSuccess()
                    else -> signUpVerifyAuthNumView.onSignUpVerifyAuthNumFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SignUpVerifyAuthNumResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())

                signUpVerifyAuthNumView.onSignUpVerifyAuthNumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }


}