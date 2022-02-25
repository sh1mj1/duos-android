package com.example.duos.data.remote.appointment

import com.google.gson.annotations.SerializedName

data class MakeAppointmentResult(
    val appointmentIdx: Int
)

data class MakeAppointmentResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: MakeAppointmentResult
)