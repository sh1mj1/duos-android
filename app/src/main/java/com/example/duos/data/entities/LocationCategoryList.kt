package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class LocationCategoryList(
    @SerializedName("categoryIdx") var locationCategoryIdx: Int? = null, // 카테고리 인덱스
    @SerializedName("categoryName") var locationCategoryName: String? = null // 카테고리 이름
)
