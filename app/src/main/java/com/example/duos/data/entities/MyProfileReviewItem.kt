package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

// 내 프로필 페이지에 있는 후기 내용들.
data class MyProfileReviewItem(

    @SerializedName("reviewIdx") val reviewIdx : Int? = null,
    @SerializedName("writerIdx") val writerIdx : Int? = null,
    @SerializedName("writerNickname") val writerNickname : String? = "",
    @SerializedName("writerProfileImgUrl") val writerProfileImgUrl : String = "",
    @SerializedName("rating") val rating : Float? = null,
    @SerializedName("date") val date : String? = "",
    @SerializedName("reviewContent") val reviewContent : String = ""

)
