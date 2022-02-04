package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class PartnerInfoDto(

    @SerializedName("userIdx") var userIdx: Int? = null,
    @SerializedName("nickname") var nickname: String = "",
    @SerializedName("age") var age: String = "",
    @SerializedName("gender") var gender: Int,
    @SerializedName("rating") var rating: Float? = null,
    @SerializedName("locationName") var locationName: String = "",      //
    @SerializedName("locationCategory") var locationCategory: String = "",
    @SerializedName("experience") var experience: String = "",
    @SerializedName("profilePhotoUrl") var profilePhotoUrl: String = "",
    @SerializedName("introduction") var introduction: String = "",
    @SerializedName("gameCount") var gameCount: Int? = null,
    @SerializedName("reviewCount") var reviewCount: Int? = null,
    @SerializedName("starred") var starred: Boolean = false
)

