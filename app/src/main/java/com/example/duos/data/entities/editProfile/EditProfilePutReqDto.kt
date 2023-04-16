package com.example.duos.data.entities.editProfile

import com.google.gson.annotations.SerializedName

data class EditProfilePutReqDto(

    @SerializedName("phoneNumber") var phoneNumber: String ,
    @SerializedName("nickname") var nickname: String ,
    @SerializedName("birth") var birth: String ,
    @SerializedName("gender") var gender: Int,
    @SerializedName("locationIdx") var locationIdx: Int,
    @SerializedName("experienceIdx") var experienceIdx: Int,
    @SerializedName("introduction") var introduction: String

)
//        phoneNumber: String,
//        nickname: String,
//        birth: String,
//        gender: Int,
//        locationIdx: Int,
//        experienceIdx: Int,
//        introduction: String,