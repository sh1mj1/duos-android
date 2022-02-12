package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class PartnerSearchData(@SerializedName("userProfilePhotoUrl") var userProfileImageUrl: String,
                             @SerializedName("userNickname") var userNickname: String,
                             @SerializedName("RecommendPartnerDto") var recommendedPartnerList : List<RecommendedPartner>)
