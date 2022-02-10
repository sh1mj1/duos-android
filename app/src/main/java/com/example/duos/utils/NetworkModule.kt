package com.example.duos.utils

import android.util.Log
import com.example.duos.ApplicationClass.Companion.BASE_URL
import com.example.duos.config.XAccessTokenInterceptor
import com.example.duos.data.remote.accessToken.AccessTokenService
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit




object NetworkModule {

    fun getRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
            .addInterceptor(AuthInterceptor())
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // 기본 URL 세팅
            .client(client) //Logger 세팅
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit
    }
}

internal class AuthInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        when (response.code) {
            200 or 201 or 202 ->{
                Log.d("AuthInterceptor", "재발급 불필요")
                Log.d("AuthInterceptor", response.code.toString())
            }
            else ->{
                Log.d("AuthInterceptor", "재발급")
                AccessTokenService.getAccessToken()
            }
        }
        return response
    }
}




//
//object NetworkModule {
//    fun getRetrofit(): Retrofit {
//        val client: OkHttpClient = OkHttpClient.Builder()
//            .readTimeout(30000, TimeUnit.MILLISECONDS)
//            .connectTimeout(30000, TimeUnit.MILLISECONDS)
//            .writeTimeout(30000, TimeUnit.MILLISECONDS)
//            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
//            .build()
//
//        val retrofit = Retrofit.Builder()
//            .baseUrl(BASE_URL) // 기본 URL 세팅
//            .client(client) //Logger 세팅
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//
//
//        return retrofit
//    }
//}