package com.example.duos.data.remote.lastappointment

import com.example.duos.data.entities.lastappointment.LastAppointmentResDto
import com.google.gson.annotations.SerializedName

data class LastAppointmentResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<LastAppointmentResDto>

)
/*
@SerializedName("isSuccess") val isSuccess: Boolean,
@SerializedName("code") val code: Int,
@SerializedName("message") val message: String,
@SerializedName("result") val result: List<StarredFriend>?
 */
