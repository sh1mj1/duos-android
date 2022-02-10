package com.example.duos.data.remote.appointment

import com.google.gson.annotations.SerializedName

data class ShowAppointmentResultData(@SerializedName("appointmentIdx") val appointmentIdx : Int,
                                     @SerializedName("appointmentDate") val appointmentData : String,
                                     @SerializedName("appointmentTime") val appointmentTime : String
)

data class ShowAppointmentResponse (
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result : ShowAppointmentResultData
)