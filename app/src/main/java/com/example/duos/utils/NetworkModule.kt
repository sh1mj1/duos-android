package com.example.duos.utils

import android.util.Log
import android.util.Log.d
import android.widget.Toast
import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.BASE_URL
import com.example.duos.config.XAccessTokenInterceptor
import com.example.duos.data.remote.accessToken.AccessTokenService
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.google.gson.JsonObject
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.json.JSONObject
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger
import kotlin.coroutines.coroutineContext


object NetworkModule {

    fun getRetrofit(): Retrofit {
        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .writeTimeout(30000, TimeUnit.MILLISECONDS)
//            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .addInterceptor(AuthInterceptor())
            .build()

        val gson = GsonBuilder()
            .registerTypeAdapter(
                LocalDateTime::class.java,
                JsonDeserializer { json, _, _ ->
                    LocalDateTime.parse(json.asString, DateTimeFormatter.ISO_DATE_TIME)
                }).create()


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL) // 기본 URL 세팅
            .client(client) //Logger 세팅
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()

        return retrofit
    }
}


internal class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        var builder = request.newBuilder()

        val token: String? = getAccessToken() //save token of this request for future
        setAuthHeader(builder, token) //write current token to request

        request = builder.build(); //overwrite old request
        var response = chain.proceed(request)
        var responseJson = response.extractResponseJson()

        if (responseJson.has("code")) {
            when (responseJson["code"]) {
                2007 -> {
                    synchronized(this){
                        Log.d("AuthInterceptor", "재발급")
                        if (getRefreshToken()){
                            setAuthHeader(builder, getAccessToken()); //set auth token to updated
                            request = builder.build();
                            return chain.proceed(request); //repeat request with new token
                        }
                        else{
                            // 로그아웃 api 호출
                        }
                    }
                }
            }
        }
        return chain.proceed(request)
    }


    private fun setAuthHeader(builder: Request.Builder, token: String?) {
        if (token != null) //Add Auth token to each request if authorized
            builder.header(ApplicationClass.X_ACCESS_TOKEN, token)
    }

    fun getRefreshToken() : Boolean{
        val code = AccessTokenService.getAccessToken()
        if (code != null ){
            if (code == 1000)
                return true
        }
        return false
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