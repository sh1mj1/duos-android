package com.example.duos.data.remote.signUp

import com.google.gson.annotations.SerializedName

data class SignUpRequestResultData(@SerializedName("isCertified") val isCertified : Boolean,
                                   @SerializedName("resultMessage") val resultMessage : String,
                                   @SerializedName("userIdx") val userIdx : Int,
                                   @SerializedName("phoneNumber") val phoneNumber : String,
                                   @SerializedName("nickname") val nickname : String,
                                   @SerializedName("birthDate") val birthDate : String,
                                   @SerializedName("gender") val gender : Int,
                                   @SerializedName("locationIdx") val locationIdx : Int,
                                   @SerializedName("experienceIdx") val experienceIdx : Int,
                                   @SerializedName("profilePhotoUrl") val profilePhotoUrl : String,
                                   @SerializedName("introduction") val introduction: String,
                                   @SerializedName("fcmToken") val fcmToken: String,
                                   @SerializedName("jwtAccessToken") val jwtAccessToken : String,
                                   @SerializedName("jwtRefreshToken") val jwtRefreshToken : String
)

data class SignUpRequestResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SignUpRequestResultData
)