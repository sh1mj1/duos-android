package com.example.duos

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDialog
import com.example.duos.config.XAccessTokenInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

// Retrofit을 호출하기 위한 Creator( API를 바로 호출할 수 있도록 설정해주는 클래스)
class ApplicationClass : Application() {
    companion object{
        const val X_ACCESS_TOKEN: String = "jwtAccessToken"         // JWT Access Token Key
        const val X_REFRESH_TOKEN: String = "jwtRefreshToken"       // JWT Refresh Token Key
        const val USER_IDX: String = "userIdx"       // JWT Refresh Token Key
        const val TAG: String = "TEMPLATE-APP"                      // Log, SharedPreference
        const val APP_DATABASE = "$TAG-DB"
        const val TEST_ACCESS_TOKEN : String = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJ1c2VySWR4IjoxNDUsImlhdCI6MTY0MzQ3MTU2MiwiZXhwIjoxNjQzNDczMzYyfQ.n5Zc8GtSvS5JHv1AFnZaCAF_GHmswdpEIgWZRmICKC9gGpcJv-6aupTZGlWkZT9isG72IfZMn_PTP06YuOt_ag"


        const val DEV_URL: String = "https://duos.co.kr/"       // 테스트 서버 주소
        const val PROD_URL: String = "https://api.template.com/"    // 실서버 주소
        const val BASE_URL: String = DEV_URL

        lateinit var mSharedPreferences: SharedPreferences
        lateinit var retrofit: Retrofit

        lateinit var baseApplication: ApplicationClass
        lateinit var progressDialog: AppCompatDialog

        fun getInstance(): ApplicationClass {
            return baseApplication
        }

        const val MY_PAGE_API = "api/mypage"
        const val MY_PROFILE_API = "api/mypage/myprofile"
        const val EVERY_REVIEW_API="api/reviews"
        const val PARTNER_PROFILE_API = "api/partners/profile"
        const val APPOINTMENT_API = "api/appointments"
        const val REVIEW_API = "api/reviews"
        const val EDIT_GET_API = "api/mypage/myprofile/previous"
        const val EDIT_PUT_NON_PIC_API = "api/mypage/myprofile/info"
        const val DUPLICATE_NICKNAME_API = "api/nickname/duplication"
        const val NOTICE_API = "api/notices"
        const val NOTICE_DELETE_API = "api/notices/{noticeIdx}"
    }

    override fun onCreate() {
        super.onCreate()

        baseApplication = this

        val client: OkHttpClient = OkHttpClient.Builder()
            .readTimeout(30000, TimeUnit.MILLISECONDS)
            .connectTimeout(30000, TimeUnit.MILLISECONDS)
            .addNetworkInterceptor(XAccessTokenInterceptor()) // JWT 자동 헤더 전송
            .build()

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        mSharedPreferences = applicationContext.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }

    fun context(): Context = applicationContext

    fun progressON(activity: Activity?, message: String?) {
        if (activity == null || activity.isFinishing) {
            return
        }

        progressDialog = AppCompatDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.progress_loading)
        progressDialog.show()

//        if (progressDialog != null && progressDialog.isShowing()) {
//            //progressSET(message)
//        } else {
//            progressDialog = AppCompatDialog(activity)
//            progressDialog.setCancelable(false)
//            progressDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//            progressDialog.setContentView(R.layout.progress_loading)
//            progressDialog.show()
//        }

        val img_loading_frame = progressDialog.findViewById<ImageView>(R.id.iv_frame_loading)
        val frameAnimation = img_loading_frame?.background as AnimationDrawable
        img_loading_frame.post { frameAnimation.start() }

    }


    fun progressOFF() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss()
        }
    }
}