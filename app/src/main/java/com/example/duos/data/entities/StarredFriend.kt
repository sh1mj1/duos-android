package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class StarredFriend (
    @SerializedName("starredFriendIdx") var starredFrienedIdx: Int? = null, //찜한친구 유저인덱스
    @SerializedName("nickname") var starredFriendNickname: String = "",
    @SerializedName("age") var starredFriendAge: Int? = null,
    @SerializedName("gender") var starredFriendGender: Int? = null,
    @SerializedName("profileImageUrl") var starredFriendImgUrl: String? = ""
)