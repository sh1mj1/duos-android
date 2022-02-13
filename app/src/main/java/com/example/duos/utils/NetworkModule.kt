package com.example.duos.utils

import android.content.Intent
import android.util.Log
import android.util.Log.d
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.duos.AccessTokenView
import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.BASE_URL
import com.example.duos.config.XAccessTokenInterceptor
import com.example.duos.data.remote.accessToken.AccessTokenService
import com.example.duos.data.remote.logout.LogOutService
import com.example.duos.data.remote.logout.LogoutListView
import com.example.duos.ui.splash.SplashActivity
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


internal class AuthInterceptor : Interceptor, LogoutListView, AccessTokenView {

    var getAccessToken : Boolean = false

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        var builder = request.newBuilder()

        setAuthHeader(builder, getAccessToken()) //write current token to request

        request = builder.build(); //overwrite old request
        var response = chain.proceed(request)
        var responseJson = response.extractResponseJson()

        if (responseJson.has("code")) {
            when (responseJson["code"]) {
                2007 -> {
                    synchronized(this){
                        Log.d("AuthInterceptor", "재발급")
                        getRefreshToken()
                        Thread.sleep(1000)
                        if (getAccessToken){
                            Log.d("재발급 성공",request.toString())
                            setAuthHeader(builder, getAccessToken()) //set auth token to updated
                            request = builder.build()
                            Log.d("request 다시 요청",request.toString())
                            return chain.proceed(request) //repeat request with new token
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

     fun getRefreshToken() {
         AccessTokenService.getAccessToken(this)
    }

    fun Response.extractResponseJson(): JSONObject {
        val jsonString = this.body?.string() ?: "{}"
        return JSONObject(jsonString)
    }

    override fun onLogOutSuccess() {
        Log.d("로그아웃","성공")
        val intent = Intent(ApplicationClass.getInstance().context(), SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        ApplicationClass.getInstance().context().startActivity(intent)
    }

    override fun onLogOutFailure(code: Int, message: String) {
//        Log.d("로그아웃","실패")
//        val intent = Intent(ApplicationClass.getInstance().context(), SplashActivity::class.java)
//        intent.flags =
//            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//        ApplicationClass.getInstance().context().startActivity(intent)
    }

    @Synchronized override fun onAccessTokenCode(boolean: Boolean) {
        getAccessToken = boolean
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