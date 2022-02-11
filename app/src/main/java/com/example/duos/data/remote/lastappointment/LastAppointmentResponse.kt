package com.example.duos.data.remote.lastappointment

import com.example.duos.data.entities.lastappointment.AppointmentResDto
import com.google.gson.annotations.SerializedName


data class LastAppointmentResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<AppointmentResDto>

)
/*
@SerializedName("isSuccess") val isSuccess: Boolean,
@SerializedName("code") val code: Int,
@SerializedName("message") val message: String,
@SerializedName("result") val result: List<StarredFriend>?
 */
