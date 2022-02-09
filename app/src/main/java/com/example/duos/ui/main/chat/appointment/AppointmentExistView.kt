package com.example.duos.ui.main.chat.appointment

import com.example.duos.data.entities.ChatListItem

interface AppointmentExistView {
    fun onAppointmentExistSuccess(appointmentIdx : Int)
    fun onAppointmentExistFailure(code: Int, message: String)
}