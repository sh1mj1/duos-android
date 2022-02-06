package com.example.duos.data.remote.chat.chat.appointment

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.remote.chat.chat.CreateChatRoomResponse
import com.example.duos.ui.main.chat.appointment.AppointmentExistView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AppointmentService {
    fun appointmentExist(appointmentExistView : AppointmentExistView, userIdx : Int, partnerIdx : Int){
        val appointmentExistService = ApplicationClass.retrofit.create(AppointmentRetrofitInterface::class.java)

        appointmentExistService.appointmentExist(userIdx, partnerIdx).enqueue(object :
            Callback<AppointmentExistResponse>
        {
            override fun onResponse(
                call: Call<AppointmentExistResponse>,
                response: Response<AppointmentExistResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1000 -> appointmentExistView.onAppointmentExistSuccess(resp.result.appointmentIdx)
                    else -> appointmentExistView.onAppointmentExistFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<AppointmentExistResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                appointmentExistView.onAppointmentExistFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}