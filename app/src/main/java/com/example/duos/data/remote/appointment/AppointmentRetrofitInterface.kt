package com.example.duos.data.remote.appointment

import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.APPOINTMENT_API
import com.example.duos.ApplicationClass.Companion.TEST_ACCESS_TOKEN
import com.example.duos.ApplicationClass.Companion.X_ACCESS_TOKEN
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface AppointmentRetrofitInterface {

    @GET(APPOINTMENT_API)
    fun getAppointmentList(@Query("userIdx") userIdx: Int): Call<AppointmentResponse>


}