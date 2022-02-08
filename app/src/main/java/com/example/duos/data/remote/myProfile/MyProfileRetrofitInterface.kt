package com.example.duos.data.remote.myProfile

import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.MY_PROFILE_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MyProfileRetrofitInterface {

    //https://duos.co.kr/api/mypage/myprofile?userIdx={userIdx}

    @Headers(ApplicationClass.X_ACCESS_TOKEN)
    @GET(MY_PROFILE_API)
    fun myProfile(@Query("userIdx") userIdx: Int): Call<MyProfileResponse>

}