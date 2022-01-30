package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class MyPageListItem(
    @SerializedName("userIdx") var userIdx: Int? = null,
    @SerializedName("profileImgUrl") var profileImgUrl: String? = "",
    @SerializedName("nickname") var nickname: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("experience") var experience: String = "",
    @SerializedName("gamesCount") var gamesCount: Int? = null

)





