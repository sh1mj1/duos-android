package com.example.duos.data.entities.editProfile

import com.google.gson.annotations.SerializedName

data class ExistingProfileInfo(
    @SerializedName("userIdx") var userIdx: Int? = null,
    @SerializedName("profileImgUrl") var profileImgUrl: String? = "",
    @SerializedName("nickname") var nickname: String = "",
    @SerializedName("birth") var birth: String = "",
    @SerializedName("gender") var gender: Int? = null ,
    @SerializedName("introduction") var introduction: String = "",
    @SerializedName("experienceIdx") var experienceIdx : Int = 1            // 초기값은 1으로??
)


/*
            "userIdx": 186,
            "profileImgUrl": "https://duosimage.s3.ap-northeast-2.amazonaws.com/static/images/ed4bf61d-edc7-4aa2-90f6-eb7d2a82fc5020220206083050.mFile",
            "nickname": "sh1mj1",
            "birth": "1998년 08월 23일",
            "gender": 1,
            "introduction": "sh1mj1",
            "experienceIdx": 4
        },

    @SerializedName("userIdx") var userIdx: Int? = null,
    @SerializedName("profileImgUrl") var profileImgUrl: String? = "",
    @SerializedName("nickname") var nickname: String = "",
    @SerializedName("phoneNumber") var phoneNumber: String = "",
    @SerializedName("experience") var experience: String = "",
    @SerializedName("gamesCount") var gamesCount: Int? = null
 */