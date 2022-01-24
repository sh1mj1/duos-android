package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class PartnerSearchData(@SerializedName("userProfileImageUrl") var userProfileImageUrl: String,
                             @SerializedName("userNickname") var userNickname: String,
                             @SerializedName("recommendPartnerList") var recommendedPartnerList : List<RecommendedPartner>)
