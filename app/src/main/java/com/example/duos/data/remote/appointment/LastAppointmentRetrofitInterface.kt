package com.example.duos.data.remote.appointment

import com.example.duos.ApplicationClass
import com.example.duos.data.entities.EditAppointment
import com.example.duos.data.entities.MakeAppointment
import retrofit2.Call
import retrofit2.http.*

interface AppointmentRetrofitInterface{

    @GET(ApplicationClass.APPOINTMENT_API)
    fun getAppointmentList(@Query("userIdx") userIdx: Int): Call<GetAppointmentListResponse>

    @GET("/api/appointment/existence")
    fun appointmentExist(
        @Query("userIdx") userIdx : Int,
        @Query("partnerIdx") partnerIdx : Int
    ): Call<AppointmentExistResponse>

    @POST("/api/appointment")
    fun makeAppointment(
        @Body makeAppointment: MakeAppointment
    ) : Call<MakeAppointmentResponse>

    @GET("/api/appointment/{appointmentIdx}")
    fun showAppointment(
        @Path("appointmentIdx") appointmentIdx : Int,
        @Query("userIdx") userIdx : Int
    ): Call<ShowAppointmentResponse>

    @PUT("/api/appointment/{appointmentIdx}")
    fun editAppointment(
        @Path("appointmentIdx") appointmentIdx : Int,
        @Body editAppointment: EditAppointment
    ) : Call<EditAppointmentResponse>
}