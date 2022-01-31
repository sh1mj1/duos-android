package com.example.duos.data.remote.myPage

import com.example.duos.ApplicationClass.Companion.MY_PAGE_API
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MyPageRetrofitInterface {
    // https://duos.co.kr/api/mypage?userIdx={userIdx}

    // API 코드 구현
    @GET(MY_PAGE_API)
    fun getUserPage(@Query("userIdx") userIdx: Int): Call<MyPageResponse>




}