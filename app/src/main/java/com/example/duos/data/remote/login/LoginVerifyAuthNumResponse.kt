package com.example.duos.data.remote.login

import com.google.gson.annotations.SerializedName

data class LoginVerifyAuthNumResultData(@SerializedName("isCertified") val isPhoneNumAvail: Boolean,
                                        @SerializedName("resultMessage") val resultMessage: String,
                                        @SerializedName("userIdx") val userIdx: Int,
                                        @SerializedName("phoneNumber") val phoneNumber : String,
                                        @SerializedName("nickname") val nickname : String,
                                        @SerializedName("birthDate") val birthDate : String,
                                        @SerializedName("gender") val gender : Int,
                                        @SerializedName("locationIdx") val locationIdx : Int,
                                        @SerializedName("experienceIdx") val experienceIdx : Int,
                                        @SerializedName("profilePhotoUrl") val profilePhotoUrl : String,
                                        @SerializedName("introduction") val introduction : String,
                                        @SerializedName("fcmToken") val fcmToken : String,
                                        @SerializedName("jwtAccessToken") val jwtAccessToken : String,
                                        @SerializedName("jwtRefreshToken") val jwtRefreshToken : String
)

data class LoginVerifyAuthNumResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: LoginVerifyAuthNumResultData
)
