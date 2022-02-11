package com.example.duos.data.entities.everyReviews

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime

data class EveryReviewsItem(

    @SerializedName("writerIdx") var writerIdx : Int? = null,
    @SerializedName("rating") var rating : Float? = null,
    @SerializedName("reviewContent") var reviewContent : String? = "",
    @SerializedName("createdAt") var createdAt : String? = "",      // LocalDateTime?????
    @SerializedName("writerNickname") var writerNickname : String? = "",
    @SerializedName("writerProfilePhotoUrl") var writerProfilePhotoUrl : String? = ""


    )





