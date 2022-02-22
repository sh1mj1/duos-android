package com.example.duos.ui.main.appointment



interface AppointmentExistView {
    fun onAppointmentExistSuccess(isExisted : Boolean, appointmentIdx : Int)
    fun onAppointmentExistFailure(code: Int, message: String)
}