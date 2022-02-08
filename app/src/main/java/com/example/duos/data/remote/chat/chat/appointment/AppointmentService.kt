package com.example.duos.data.remote.chat.chat.appointment

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.MakeAppointment
import com.example.duos.data.remote.chat.chat.CreateChatRoomResponse
import com.example.duos.ui.main.chat.appointment.AppointmentExistView
import com.example.duos.ui.main.chat.appointment.MakeAppointmentView
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

    fun makeAppointment(makeAppointmentView: MakeAppointmentView, makeAppointment: MakeAppointment){
        val makeAppointmentService = ApplicationClass.retrofit.create(AppointmentRetrofitInterface::class.java)
        makeAppointmentService.makeAppointment(makeAppointment).enqueue(object :
        Callback<MakeAppointmentResponse>{
            override fun onResponse(
                call: Call<MakeAppointmentResponse>,
                response: Response<MakeAppointmentResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1000 -> makeAppointmentView.onMakeAppointmentSuccess()
                    else -> makeAppointmentView.onMakeAppointmentFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<MakeAppointmentResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                makeAppointmentView.onMakeAppointmentFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })

    }
}