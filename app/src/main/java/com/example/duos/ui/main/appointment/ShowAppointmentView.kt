package com.example.duos.ui.main.appointment

import com.example.duos.data.remote.appointment.ShowAppointmentResultData

interface ShowAppointmentView {
    fun onShowAppointmentSuccess(result : ShowAppointmentResultData)
    fun onShowAppointmentFailure(code: Int, message: String)
}