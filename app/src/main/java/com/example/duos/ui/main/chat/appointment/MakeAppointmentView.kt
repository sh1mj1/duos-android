package com.example.duos.ui.main.chat.appointment

interface MakeAppointmentView {
    fun onMakeAppointmentSuccess()
    fun onMakeAppointmentFailure(code: Int, message: String)
}