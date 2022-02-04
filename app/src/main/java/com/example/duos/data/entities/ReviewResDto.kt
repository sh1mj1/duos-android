package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

// 파트너 프로필 페이지에 있는 후기 내용들
data class ReviewResDto (

    @SerializedName("writerIdx") val writerIdx : Int? = null,
    @SerializedName("rating") val rating : Float? = null,
    @SerializedName("reviewContent") val reviewContent : String = "",
    @SerializedName("createdAt") val createdAt : String = "",
    @SerializedName("writerNickname") val writerNickname : String = "",
    @SerializedName("writerProfilePhotoUrl") val writerProfilePhotoUrl : String? = ""


)

