package com.example.duos.ui.main.appointment

interface EditAppointmentView {
    fun onEditAppointmentSuccess()
    fun onEditAppointmentFailure(code: Int, message: String)
}