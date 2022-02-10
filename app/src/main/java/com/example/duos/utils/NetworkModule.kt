package com.example.duos.utils

import android.util.Log
import android.util.Log.d
import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.BASE_URL
import com.example.duos.config.XAccessTokenInterceptor
import com.example.duos.data.remote.accessToken.AccessTokenService
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger


object NetworkModule {

    fun getRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .addInterceptor(AuthInterceptor())
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // 기본 URL 세팅
            .addConverterFactory(GsonConverterFactory.create())
            .client(client) //Logger 세팅
            .build()

        return retrofit
    }
}

internal class AuthInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {

        val request = chain.request()
        val response = chain.proceed(request)
        val responseJson = response.extractResponseJson()

        if (responseJson.has("code")){
            when (responseJson["code"]){
                2007 ->{
                    Log.d("AuthInterceptor", "재발급")
                    AccessTokenService.getAccessToken()
                }
            }
        }
        return response.newBuilder()
            .body(responseJson.toString().toResponseBody())
            .build()
    }

    fun Response.extractResponseJson(): JSONObject {
        val jsonString = this.body?.string() ?: "{}"
        return JSONObject(jsonString)
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