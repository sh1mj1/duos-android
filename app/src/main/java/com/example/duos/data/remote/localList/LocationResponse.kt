package com.example.duos.data.remote.localList

import com.example.duos.data.entities.Location
import com.example.duos.data.entities.StarredFriend
import com.google.gson.annotations.SerializedName

data class LocationResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: Location
)
