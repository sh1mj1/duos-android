package com.example.duos.data.remote.signUp

import android.util.Log
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.ApplicationClass.Companion.retrofit
import com.example.duos.data.entities.PhoneAuthNum
import com.example.duos.data.entities.SignUpRequestInfo
import com.example.duos.ui.signup.SignUpCreateAuthNumView
import com.example.duos.ui.signup.SignUpNickNameView
import com.example.duos.ui.signup.SignUpRequestView
import com.example.duos.ui.signup.SignUpVerifyAuthNumView
import com.example.duos.utils.NetworkModule
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Multipart

object SignUpService {
    //val retrofit = NetworkModule.getRetrofit()
    fun signUpCreateAuthNum(signUpCreateAuthNumView: SignUpCreateAuthNumView, phoneNumber: String) {
        val signUpCreateAuthNumService = retrofit.create(SignUpRetrofitInterface::class.java)

        signUpCreateAuthNumService.signUpCreateAuthNum(phoneNumber).enqueue(object :
            Callback<SignUpCreateAuthNumResponse> {
            override fun onResponse(
                call: Call<SignUpCreateAuthNumResponse>,
                response: Response<SignUpCreateAuthNumResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> signUpCreateAuthNumView.onSignUpCreateAuthNumSuccess()
                    else -> signUpCreateAuthNumView.onSignUpCreateAuthNumFailure(
                        resp.code,
                        resp.message
                    )
                }
            }

            override fun onFailure(call: Call<SignUpCreateAuthNumResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())

                signUpCreateAuthNumView.onSignUpCreateAuthNumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun signUpVerifyAuthNum(
        signUpVerifyAuthNumView: SignUpVerifyAuthNumView,
        phoneNumber: String,
        authNumber: String
    ) {
        val signUpVerifyAuthNumService = retrofit.create(SignUpRetrofitInterface::class.java)

        val phoneAuthNum = PhoneAuthNum(phoneNumber, authNumber)

        signUpVerifyAuthNumService.signUpVerifyAuthNum(phoneAuthNum).enqueue(object :
            Callback<SignUpVerifyAuthNumResponse> {
            override fun onResponse(
                call: Call<SignUpVerifyAuthNumResponse>,
                response: Response<SignUpVerifyAuthNumResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> signUpVerifyAuthNumView.onSignUpVerifyAuthNumSuccess()
                    else -> signUpVerifyAuthNumView.onSignUpVerifyAuthNumFailure(
                        resp.code,
                        resp.message
                    )
                }
            }

            override fun onFailure(call: Call<SignUpVerifyAuthNumResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())

                signUpVerifyAuthNumView.onSignUpVerifyAuthNumFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun signUpNickNameDuplicate(signUpNickNameView: SignUpNickNameView, nickName: String) {
        val signUpNickNameService = retrofit.create(SignUpRetrofitInterface::class.java)

        signUpNickNameService.signUpNickNameDupli(nickName).enqueue(object :
            Callback<SignUpNickNameDuplicateResponse> {
            override fun onResponse(
                call: Call<SignUpNickNameDuplicateResponse>,
                response: Response<SignUpNickNameDuplicateResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> {
                        if (!resp.result.isDuplicated)
                            signUpNickNameView.onSignUpNickNameSuccess()
                        else
                            signUpNickNameView.onSignUpNickNameFailure(
                                resp.code,
                                resp.result.resultMessage
                            )
                    }
                    else -> signUpNickNameView.onSignUpNickNameFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SignUpNickNameDuplicateResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())

                signUpNickNameView.onSignUpNickNameFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun signUpReqeust(signUpRequestView: SignUpRequestView, mFile: MultipartBody.Part?, userInfo: RequestBody) {
        val signUpRequestService = retrofit.create(SignUpRetrofitInterface::class.java)

        signUpRequestService.signUpRequest(mFile, userInfo).enqueue(object :
            Callback<SignUpRequestResponse> {
            override fun onResponse(
                call: Call<SignUpRequestResponse>,
                response: Response<SignUpRequestResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when (resp.code) {
                    1000 -> signUpRequestView.onSignUpRequestSuccess()
                    else -> signUpRequestView.onSignUpRequestFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SignUpRequestResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())

                signUpRequestView.onSignUpRequestFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

}