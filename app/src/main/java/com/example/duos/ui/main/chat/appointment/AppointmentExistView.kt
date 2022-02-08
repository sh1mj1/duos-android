package com.example.duos.ui.main.chat.appointment



interface AppointmentExistView {
    fun onAppointmentExistSuccess(appointmentIdx : Int)
    fun onAppointmentExistFailure(code: Int, message: String)
}