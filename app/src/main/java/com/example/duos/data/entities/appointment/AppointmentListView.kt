package com.example.duos.data.entities.appointment

import com.example.duos.data.remote.appointment.GetAppointmentListResponse

interface AppointmentListView {
    fun onGetAppointmentSuccess(appointmentResponse: GetAppointmentListResponse)
    fun onGetAppointmentFailure(code : Int, message : String)

}
