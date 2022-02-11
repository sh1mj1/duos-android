package com.example.duos.data.remote.appointment

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.DeleteAppointment
import com.example.duos.data.entities.EditAppointment
import com.example.duos.data.entities.MakeAppointment
import com.example.duos.data.entities.appointment.AppointmentListView
import com.example.duos.ui.main.appointment.*
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object AppointmentService {
    val retrofit = NetworkModule.getRetrofit()

    fun getAppointmentList(appointmentListView: AppointmentListView, userIdx: Int) {
        val TAG = "AppointmentService"
        val appointmentService = retrofit.create(AppointmentRetrofitInterface::class.java)
        appointmentService.getAppointmentList(userIdx).enqueue(object : Callback<GetAppointmentListResponse> {
            override fun onResponse(call: Call<GetAppointmentListResponse>, response: Response<GetAppointmentListResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> resp.let {
                        appointmentListView.onGetAppointmentSuccess(it)
                        Log.d(TAG, resp.toString())
                        Log.d(TAG, resp.result.toString())
                    }

                    else -> appointmentListView.onGetAppointmentFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<GetAppointmentListResponse>, t: Throwable) {
                Log.d("${TAG}/API-ERROR", t.message.toString())
                appointmentListView.onGetAppointmentFailure(400, "네트워크 오류 발생")

            }
        })
    }


    fun isAppointmentExist(appointmentExistView : AppointmentExistView, userIdx : Int, partnerIdx : Int){
        val isAppointmentExistService = retrofit.create(AppointmentRetrofitInterface::class.java)

        isAppointmentExistService.appointmentExist(userIdx, partnerIdx).enqueue(object :
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
        val makeAppointmentService = retrofit.create(AppointmentRetrofitInterface::class.java)
        makeAppointmentService.makeAppointment(makeAppointment).enqueue(object :
        Callback<MakeAppointmentResponse>{
            override fun onResponse(
                call: Call<MakeAppointmentResponse>,
                response: Response<MakeAppointmentResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1122 -> makeAppointmentView.onMakeAppointmentSuccess()
                    else -> makeAppointmentView.onMakeAppointmentFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<MakeAppointmentResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                makeAppointmentView.onMakeAppointmentFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun editAppointment(editAppointmentView: EditAppointmentView, appointmentIdx: Int, editAppointment: EditAppointment){
        val editAppointmentService = retrofit.create(AppointmentRetrofitInterface::class.java)
        editAppointmentService.editAppointment(appointmentIdx, editAppointment).enqueue(object :
            Callback<EditAppointmentResponse>{
            override fun onResponse(
                call: Call<EditAppointmentResponse>,
                response: Response<EditAppointmentResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1120 -> editAppointmentView.onEditAppointmentSuccess()
                    else -> editAppointmentView.onEditAppointmentFailure(resp.code, resp.message)

                }
            }

            override fun onFailure(call: Call<EditAppointmentResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                editAppointmentView.onEditAppointmentFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun showAppointment(showAppointmentView: ShowAppointmentView, appointmentIdx: Int, userIdx: Int){
        val showAppointmentService = retrofit.create(AppointmentRetrofitInterface::class.java)
        showAppointmentService.showAppointment(appointmentIdx, userIdx).enqueue(object :
            Callback<ShowAppointmentResponse>{
            override fun onResponse(
                call: Call<ShowAppointmentResponse>,
                response: Response<ShowAppointmentResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1000 -> showAppointmentView.onShowAppointmentSuccess(resp.result)
                    else -> showAppointmentView.onShowAppointmentFailure(resp.code, resp.message)

                }
            }

            override fun onFailure(call: Call<ShowAppointmentResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                showAppointmentView.onShowAppointmentFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun deleteAppointment(deleteAppointmentView: DeleteAppointmentView, appointmentIdx:Int, userIdx: Int, deleteAppointment: DeleteAppointment){
        val deleteAppointmentService = retrofit.create(AppointmentRetrofitInterface::class.java)
        deleteAppointmentService.deleteAppointment(appointmentIdx, userIdx, deleteAppointment).enqueue(object :
            Callback<DeleteAppointmentResponse>{
            override fun onResponse(
                call: Call<DeleteAppointmentResponse>,
                response: Response<DeleteAppointmentResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1121 -> deleteAppointmentView.onDeleteAppointmentSuccess()
                    else -> deleteAppointmentView.onDeleteAppointmentFailure(resp.code, resp.message)

                }
            }

            override fun onFailure(call: Call<DeleteAppointmentResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                deleteAppointmentView.onDeleteAppointmentFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}