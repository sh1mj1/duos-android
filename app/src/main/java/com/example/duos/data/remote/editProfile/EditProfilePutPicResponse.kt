package com.example.duos.data.remote.editProfile

import com.google.gson.annotations.SerializedName

data class EditProfilePutPicResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)


