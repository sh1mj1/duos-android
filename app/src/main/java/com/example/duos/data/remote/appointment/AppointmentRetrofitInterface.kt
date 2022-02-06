package com.example.duos.data.remote.appointment

import com.example.duos.ApplicationClass.Companion.APPOINTMENT_PAI
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface AppointmentRetrofitInterface {

    @GET(APPOINTMENT_PAI)
    fun getAppointmentList(@Query("userIdx") userIdx: Int): Call<AppointmentResponse>

}