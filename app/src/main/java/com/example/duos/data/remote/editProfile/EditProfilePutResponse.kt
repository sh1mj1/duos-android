package com.example.duos.data.remote.editProfile

import com.google.gson.annotations.SerializedName

data class EditProfilePutResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String
)
