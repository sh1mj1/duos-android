package com.example.duos.data.remote.chat.chat.appointment

import com.example.duos.data.remote.chat.chat.CreateChatRoomResultData
import com.google.gson.annotations.SerializedName

data class AppointmentExistResultData(
    @SerializedName("isExisted") val isExisted : Boolean,
    @SerializedName("appointmentIdx") val appointmentIdx : Int // 약속이 존재하지 않으면 -1 반환,
                                                                     // 존재하면 약속 인덱스 반환
)

data class AppointmentExistResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: AppointmentExistResultData
)
