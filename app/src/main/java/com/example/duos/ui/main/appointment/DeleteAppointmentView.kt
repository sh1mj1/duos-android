package com.example.duos.ui.main.appointment

interface DeleteAppointmentView {
    fun onDeleteAppointmentSuccess()
    fun onDeleteAppointmentFailure(code: Int, message: String)
}