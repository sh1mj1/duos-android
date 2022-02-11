package com.example.duos.data.remote.editProfile

import com.example.duos.data.entities.editProfile.GetEditProfileResDto
import com.google.gson.annotations.SerializedName

data class EditProfileResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: GetEditProfileResDto

)

