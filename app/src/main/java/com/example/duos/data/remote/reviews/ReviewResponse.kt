package com.example.duos.data.remote.reviews

import com.example.duos.data.entities.review.PostReviewResDto
import com.google.gson.annotations.SerializedName

data class ReviewResponse(

    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PostReviewResDto

)
