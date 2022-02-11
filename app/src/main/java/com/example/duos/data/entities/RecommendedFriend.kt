package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class RecommendedFriend(
    @SerializedName("partnerIdx") var partnerIdx : Int? = null,
    @SerializedName("nickname") var recommendedFriendNickname: String = "",
    @SerializedName("age") var recommendedFriendAge: String? = "",
    @SerializedName("gender") var recommendedFriendGender: Int? = null,
    @SerializedName("profilePhotoUrl") var recommendedFriendImgUrl: String? = "",
    @SerializedName("starred") var recommendedFriendIsStarred : Boolean = false // 찜한 여부
)
