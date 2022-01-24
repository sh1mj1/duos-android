package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class UserSimple(
    @SerializedName("userProfileImageUrl") var userProfileImageUrl: String? = "",
    @SerializedName("userNickname") var userNickname: String = ""
)
