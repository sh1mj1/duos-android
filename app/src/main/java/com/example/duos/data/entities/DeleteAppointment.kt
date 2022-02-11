package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class DeleteAppointment(
    @SerializedName("chatroomIdx") val chatroomIdx : String,
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("partnerIdx") val partnerIdx : Int,
    @SerializedName("appointmentIdx") val appointmentIdx : Int
)
