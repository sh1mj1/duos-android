package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class StarredFriend (
    @SerializedName("nickname") var myFriendNickname: String = "",
    @SerializedName("age") var myFriendAge: Int? = null,
    @SerializedName("gender") var myFriendGender: Int? = null,
    @SerializedName("profileImageUrl") var myFriendImgUrl: String? = ""
)