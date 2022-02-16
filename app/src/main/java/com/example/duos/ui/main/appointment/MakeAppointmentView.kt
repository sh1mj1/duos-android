package com.example.duos.ui.main.appointment

import com.example.duos.data.remote.appointment.MakeAppointmentResult

interface MakeAppointmentView {
    fun onMakeAppointmentSuccess(result: MakeAppointmentResult)
    fun onMakeAppointmentFailure(code: Int, message: String)
}