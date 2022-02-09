package com.example.duos.data.remote.reviews

import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.REVIEW_API
import com.example.duos.ApplicationClass.Companion.X_ACCESS_TOKEN
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface ReviewRetrofitInterface {

    @POST(REVIEW_API)
    fun postReview(@Body reviewData: JSONObject, @Query ("userIdx") userIdx: Int) : Call<ReviewResponse>


}