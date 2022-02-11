package com.example.duos.data.remote.appointment

import com.example.duos.data.entities.appointment.AppointmentResDto
import com.google.gson.annotations.SerializedName

data class GetAppointmentListResponse(
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