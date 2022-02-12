package com.example.duos.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "RecommendedPartnerTable")
data class RecommendedPartner(
    @PrimaryKey @SerializedName("partnerIdx") var partnerIdx: Int = 0,
    @SerializedName("nickname") var id: String = "",
    @SerializedName("age") var age: String = "",
    @SerializedName("rating") var starRating: Float = 0.0f,
    @SerializedName("locationName") var locationName: String = "",
    @SerializedName("locationCategoryName") var locationCategory: String = "",
    @SerializedName("experience") var ballCapacity: String = "",
    @SerializedName("profilePhotoUrl") var profileImg: String = "",
    @SerializedName("reviewCount") var reviewCount: Int = 0,
)
