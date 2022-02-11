package com.example.duos.data.remote.accessToken

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.example.duos.ApplicationClass
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.splash.SplashActivity
import com.example.duos.utils.getRefreshToken
import com.example.duos.utils.getUserIdx
import com.example.duos.utils.saveJwt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object AccessTokenService {
    fun getAccessToken() : Int? {
        val retrofit = ApplicationClass.retrofit
        var code : Int? = null
        val getAccessTokenService = retrofit.create(AccessTokenRetrofitInterface::class.java)

        getAccessTokenService.getAccessToken(com.example.duos.utils.getAccessToken()!!, getRefreshToken()!!, getUserIdx()!!).enqueue(object :
            Callback<AccessTokenResponse> {
            override fun onResponse(
                call: Call<AccessTokenResponse>,
                response: Response<AccessTokenResponse>
            ) {

                val resp = response.body()!!
                Log.d("resp", resp.toString())
                code = resp.code

                when (resp.code) {
                    1000 -> {
                        saveJwt(resp.result.jwtAccessToken)
                    }
                    else -> {
                        val intent = Intent(ApplicationClass.getInstance().context(), SplashActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                        ApplicationClass.getInstance().context().startActivity(intent)
                    }
                }
            }

            override fun onFailure(call: Call<AccessTokenResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
            }
        })
        return code
    }
}