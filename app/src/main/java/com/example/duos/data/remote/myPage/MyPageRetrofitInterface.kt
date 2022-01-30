package com.example.duos.data.remote.myPage

import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MyPageRetrofitInterface {
    // https://duos.co.kr/api/mypage?userIdx={userIdx}

    @GET("api/mypage")
    fun getUserPage(@Query("userIdx") userIdx: Int): Call<MyPageResponse>


}