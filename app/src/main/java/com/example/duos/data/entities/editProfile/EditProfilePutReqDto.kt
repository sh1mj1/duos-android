package com.example.duos.data.entities.editProfile

data class EditProfilePutReqDto(

    val phoneNumber: String,
    val nickname: String,
    val birth: String,
    val gender: Int,
    val locationIdx: Int,
    val experienceIdx: Int ,
    val introduction: String

)
//        phoneNumber: String,
//        nickname: String,
//        birth: String,
//        gender: Int,
//        locationIdx: Int,
//        experienceIdx: Int,
//        introduction: String,