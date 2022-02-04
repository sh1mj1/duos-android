package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class MyProfileResult(

    @SerializedName("profileInfo") var profileInfo : MyProfileInfoItem,
    @SerializedName("reviews") val reviews: List<PartnerProfileReviewItem>

)


