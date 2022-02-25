package com.example.duos.data.entities.editProfile

import com.google.gson.annotations.SerializedName

data class EditProfilePutReqDto(

    @SerializedName("phoneNumber")val phoneNumber: String,
    @SerializedName("nickname")val nickname: String,
    @SerializedName("birth")val birth: String,
    @SerializedName("gender")val gender: Int,
    @SerializedName("locationIdx")val locationIdx: Int,
    @SerializedName("experienceIdx")val experienceIdx: Int,
    @SerializedName("introduction")val introduction: String

)
//        phoneNumber: String,
//        nickname: String,
//        birth: String,
//        gender: Int,
//        locationIdx: Int,
//        experienceIdx: Int,
//        introduction: String,