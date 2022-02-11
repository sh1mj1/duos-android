package com.example.duos.data.remote.reviews

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.review.PostReviewReqDto
import com.example.duos.data.entities.review.ReviewListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ReviewService {
    val TAG = "ReviewService"
    val retrofit = NetworkModule.getRetrofit()
    fun postReview(
        reviewListView: ReviewListView, writerIdx: Int, revieweeIdx: Int, rating: Float, reviewContent: String, createdAt: String,
        appointmentIdx: Int, userIdx: Int) {
        val reviewService = retrofit.create(ReviewRetrofitInterface::class.java)
        Log.d(TAG, "CREATE_RETROFIT")
        val postReviewReqDto = PostReviewReqDto(writerIdx, revieweeIdx, rating,reviewContent, createdAt,appointmentIdx  )
        reviewService.postReview(postReviewReqDto, userIdx).enqueue(object : Callback<ReviewResponse> {
            override fun onResponse(call: Call<ReviewResponse>, response: Response<ReviewResponse>) {
                Log.d(TAG, "ON_RESPONSE")
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        resp.let {
                            reviewListView.onPostReviewSuccess(it)
                            Log.d(TAG, "postReviewReqDto : $postReviewReqDto.toString()")
                            Log.d(TAG, it.result.toString())
                        }
                    }

                    else -> reviewListView.onPostReviewFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<ReviewResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                reviewListView.onPostReviewFailure(400, "네트워크 오류 발생")
            }


        })
    }
}