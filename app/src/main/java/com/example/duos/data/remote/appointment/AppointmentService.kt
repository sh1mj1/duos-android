package com.example.duos.data.remote.appointment

import android.util.Log
import android.widget.Toast
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.data.entities.appointment.AppointmentListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

object AppointmentService {
    val retrofit = NetworkModule.getRetrofit()

    fun getAppointmentList(appointmentListView: AppointmentListView, userIdx: Int) {
        val appointmentService = retrofit.create(AppointmentRetrofitInterface::class.java)
        appointmentService.getAppointmentList(userIdx).enqueue(object : Callback<AppointmentResponse> {
            override fun onResponse(call: Call<AppointmentResponse>, response: Response<AppointmentResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> resp.let { appointmentListView.onGetAppointmentSuccess(it) }

                    else -> appointmentListView.onGetAppointmentFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<AppointmentResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())
                appointmentListView.onGetAppointmentFailure(400, "네트워크 오류 발생")


            }
        })
    }

    //TODO :     fun Post?

}