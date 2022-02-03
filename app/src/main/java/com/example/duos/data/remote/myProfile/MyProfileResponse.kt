package com.example.duos.data.remote.myProfile

import com.example.duos.data.entities.MyProfileResult
import com.google.gson.annotations.SerializedName

data class MyProfileResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: MyProfileResult
//    @SerializedName("reviews") val reviews: List<MyProfileReviewItem>
)
