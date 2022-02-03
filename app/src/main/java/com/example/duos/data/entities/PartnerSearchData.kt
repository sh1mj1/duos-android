package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class PartnerSearchData(@SerializedName("userProfilePhotoUrl") var userProfileImageUrl: String,
                             @SerializedName("userNickname") var userNickname: String,
                             @SerializedName("recommendPartnerDto") var recommendedPartnerList : List<RecommendedPartner>)
