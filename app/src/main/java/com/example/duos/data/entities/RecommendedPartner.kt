package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class RecommendedPartner(
    @SerializedName("profilePhotoUrl") var profileImg: String = "",
    @SerializedName("experience") var ballCapacity: String = "",
    @SerializedName("nickname") var id: String = "",
    @SerializedName("age") var age: String = "",
    @SerializedName("rating") var starRating: Float = 0.0f,
    @SerializedName("reviewCount") var reviewCount: Int = 0,
    @SerializedName("partnerIdx") var partnerIdx: Int = 0,
    @SerializedName("locationName") var locationName: String = "",
    @SerializedName("locationCategoryName") var locationCategory: String = ""
)
