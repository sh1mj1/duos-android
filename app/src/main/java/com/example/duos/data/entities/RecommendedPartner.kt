package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class RecommendedPartner(
    @SerializedName("profileImageUrl") var profileImg: String = "",
    @SerializedName("location") var location: String = "",
    @SerializedName("experience") var ballCapacity: String = "",
    @SerializedName("nickname") var id: String = "",
    @SerializedName("age") var age: Int? = 0,
    @SerializedName("rating") var starRating: Float = 0.0f,
    @SerializedName("reviewCnt") var reviewCount: Int = 0
)
