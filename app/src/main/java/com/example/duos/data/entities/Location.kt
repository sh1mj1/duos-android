package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class Location(
    @SerializedName("locationCategoryList") var locationCategoryList: List<LocationCategoryList>,
    @SerializedName("locationList") var locationList: List<LocationList>
)
