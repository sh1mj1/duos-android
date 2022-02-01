package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class SignUpRequestInfo(
    @SerializedName("phoneNumber") var phonenumber : String,
    @SerializedName("nickname") var nickName : String,
    @SerializedName("birthDate") var birthDate : String,
    @SerializedName("gender") var gender : Int,
    @SerializedName("locationIdx") var locationIdx : Int,
    @SerializedName("experienceIdx") var experienceIdx : Int,
    @SerializedName("introduction") var introduction : String
)
