package com.example.duos.utils

import android.util.Log
import com.example.duos.ApplicationClass.Companion.BASE_URL
import com.example.duos.config.XAccessTokenInterceptor
import com.example.duos.data.entities.ResponseWrapper
import com.example.duos.data.remote.accessToken.AccessTokenService
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit




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
        val rawJson = response.body?.string() ?: "{}"

        /**
         * 4) Wrap body with gson
         */
        val gson = Gson()
        val type = object : TypeToken<ResponseWrapper<*>>() {}.type
        val res = try {
            val r = gson.fromJson<ResponseWrapper<*>>(rawJson, type) ?: throw JsonSyntaxException("Parse Fail")

            if(!r.isSuccess)
                ResponseWrapper<Any>(false, r.code, "Server Logic Fail : ${r.message}", null) //1
            else
                ResponseWrapper<Any>(true, r.code, "Success : ${r.message}", null)

        } catch (e: JsonSyntaxException) {
            ResponseWrapper<Any>(false, e.hashCode(), "json parsing fail : $e", null) //2
        } catch (t: Throwable) {
            ResponseWrapper<Any>(false, t.hashCode(), "unknown error : $t", null)   //3
        }

//        /**
//         * 5) get data json from data
//         */
//        val dataJson = gson.toJson(res.data)

        when (res.code) {
            1000 ->{
                Log.d("AuthInterceptor", "재발급 불필요")
            }
            2007 ->{
                Log.d("AuthInterceptor", "재발급")
                AccessTokenService.getAccessToken()
            }
            else->{
                Log.d("상태", res.code.toString())
                Log.d("메시지",res.message.toString())

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