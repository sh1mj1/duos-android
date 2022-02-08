package com.example.duos.data.remote.chat.chat.appointment

import com.example.duos.data.entities.MakeAppointment
import com.example.duos.data.remote.chat.chatList.ChatListResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface AppointmentRetrofitInterface{
    @GET("/api/appointment/existence")
    fun appointmentExist(
        @Query("userIdx") userIdx : Int,
        @Query("partnerIdx") partnerIdx : Int
    ): Call<AppointmentExistResponse>

    @POST("/api/appointment")
    fun makeAppointment(
        @Body makeAppointment: MakeAppointment
    ) : Call<MakeAppointmentResponse>
}