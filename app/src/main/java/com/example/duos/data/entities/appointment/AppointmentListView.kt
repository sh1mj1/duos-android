package com.example.duos.data.entities.appointment

import com.example.duos.data.remote.appointment.AppointmentResponse

interface AppointmentListView {
    fun onGetAppointmentSuccess(appointmentResponse: AppointmentResponse)
    fun onGetAppointmentFailure(code : Int, message : String)

}
