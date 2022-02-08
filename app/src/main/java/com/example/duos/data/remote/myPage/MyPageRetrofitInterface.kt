package com.example.duos.data.remote.myPage

import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.MY_PAGE_API
import com.example.duos.ApplicationClass.Companion.TEST_ACCESS_TOKEN
import com.example.duos.ApplicationClass.Companion.X_ACCESS_TOKEN
import com.google.gson.JsonElement
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface MyPageRetrofitInterface {
    // https://duos.co.kr/api/mypage?userIdx={userIdx}
    // API 코드 구현


    @GET(MY_PAGE_API)
    fun getUserPage(@Query("userIdx") userIdx: Int): Call<MyPageResponse>

}