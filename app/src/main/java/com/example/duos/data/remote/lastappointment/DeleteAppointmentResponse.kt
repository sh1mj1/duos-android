package com.example.duos.data.remote.appointment

import com.google.gson.annotations.SerializedName

data class DeleteAppointmentResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
)
