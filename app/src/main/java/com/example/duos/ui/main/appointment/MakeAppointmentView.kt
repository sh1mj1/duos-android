package com.example.duos.ui.main.appointment

interface MakeAppointmentView {
    fun onMakeAppointmentSuccess()
    fun onMakeAppointmentFailure(code: Int, message: String)
}