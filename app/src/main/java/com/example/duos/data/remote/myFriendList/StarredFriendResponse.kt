package com.example.duos.data.remote.myFriendList

import com.example.duos.data.entities.StarredFriend
import com.google.gson.annotations.SerializedName

data class StarredFriendResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<StarredFriend>?
)
