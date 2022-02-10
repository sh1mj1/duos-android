package com.example.duos.data.remote.myFriendList

import com.google.gson.annotations.SerializedName

data class DeleteStarredFriendResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
