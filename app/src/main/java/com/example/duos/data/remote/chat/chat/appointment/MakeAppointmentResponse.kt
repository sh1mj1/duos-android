package com.example.duos.data.remote.chat.chat.appointment

import com.google.gson.annotations.SerializedName

data class MakeAppointmentResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)
