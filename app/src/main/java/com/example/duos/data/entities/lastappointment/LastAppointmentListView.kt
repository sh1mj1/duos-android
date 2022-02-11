package com.example.duos.data.entities.lastappointment

import com.example.duos.data.remote.lastappointment.LastAppointmentResponse

interface LastAppointmentListView {
    fun onGetAppointmentSuccess(lastAppointmentResponse: LastAppointmentResponse)
    fun onGetAppointmentFailure(code : Int, message : String)

}
