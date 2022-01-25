package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class LocationList(
    @SerializedName("categoryIdx") var locationCategoryIdx: Int? = null, // 카테고리 인덱스
    @SerializedName("locationIdx") var locationIdx: Int? = null, // 지역 인덱스
    @SerializedName("locationName") var locationName: String? = null // 지역 이름
)
