package com.example.duos.data.remote.everyReviews

import com.example.duos.ApplicationClass.Companion.EVERY_REVIEW_API
import com.example.duos.data.remote.myPage.MyPageResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EveryReviewsRetrofitInterface {
    // https://duos.co.kr/api/partners/profile?userIdx={userIdx}&partnerIdx={partnerIdx}
    // API 코드 구현
    @GET(EVERY_REVIEW_API)
    fun getEveryReviews(@Query("userIdx") userIdx: Int): Call<EveryReviewsResponse>
    // 이때 user는 해당 프로필의 주인 (내 프로필이 될 수도 있고 파트너의 프로피이 될 수도 있다.)
}