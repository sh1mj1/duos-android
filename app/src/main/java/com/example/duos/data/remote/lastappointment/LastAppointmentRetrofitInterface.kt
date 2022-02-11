package com.example.duos.data.remote.lastappointment

import com.example.duos.ApplicationClass.Companion.APPOINTMENT_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LastAppointmentRetrofitInterface {

    @GET(APPOINTMENT_API)
    fun getAppointmentList(@Query("userIdx") userIdx: Int): Call<LastAppointmentResponse>


}