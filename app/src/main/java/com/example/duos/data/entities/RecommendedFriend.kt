package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class RecommendedFriend(
    @SerializedName("nickname") var RecommendedFriendNickname: String = "",
    @SerializedName("age") var RecommendedFriendAge: Int? = null,
    @SerializedName("gender") var RecommendedFriendGender: String = "",
    @SerializedName("profileImageUrl") var RecommendedFriendImgUrl: String? = "",
    @SerializedName("isStarred") var RecommendedFriendIsStarred : Boolean = false // 찜한 여부
)
