package com.example.duos.data.remote.editProfile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.editProfile.EditProfilePutListView
import com.example.duos.data.entities.editProfile.EditProfilePutReqDto
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EditProfilePutService {
    val TAG = "EditProfilePutService"
    val retrofit = NetworkModule.getRetrofit()

    fun putEditProfile(
        editProfilePutListView: EditProfilePutListView,
        phoneNumber: String,
        nickname: String,
        birth: String,
        gender: Int,
        locationIdx: Int,
        experienceIdx: Int,
        introduction: String,
        userIdx: Int
    ) {
        val editProfilePutService = retrofit.create(EditProfileRetrofitInterface::class.java)

        val editProfilePutReqDto = EditProfilePutReqDto(phoneNumber, nickname, birth, gender, locationIdx, experienceIdx, introduction)

        editProfilePutService.onPutProfileWithoutPic(editProfilePutReqDto, userIdx).enqueue(object : Callback<EditProfilePutResponse> {
            override fun onResponse(call: Call<EditProfilePutResponse>, response: Response<EditProfilePutResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1100 -> {
                        resp.let {
                            editProfilePutListView.onPutEditProfileItemSuccess(it, resp.message)
                            Log.d(TAG, resp.toString())
                        }
                    }
                    else -> {
                        Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
                    }
                }
            }

            override fun onFailure(call: Call<EditProfilePutResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                editProfilePutListView.onPutEditProfileItemFailure(400, "네트워크 오류 발생")
            }

        })
    }

//    fun putEditProfile(editProfilePutListView: EditProfilePutListView,
//                       editProfilePutReqDto: EditProfilePutReqDto ,
//                       userIdx : Int){
//        val editProfilePutService = retrofit.create(EditProfileRetrofitInterface::class.java)
//
//        editProfilePutService.onPutProfileWithoutPic(editProfilePutReqDto, userIdx).enqueue(object : Callback<EditProfilePutResponse>{
//            override fun onResponse(call: Call<EditProfilePutResponse>, response: Response<EditProfilePutResponse>) {
//                val resp = response.body()
//                when(resp?.code){
//                    1000 -> {
//                        resp.let{
//                            editProfilePutListView.onPutEditProfileItemSuccess(it)
//                            Log.d(TAG, resp.toString())
//                        }
//                    }
//                    else -> {
//                        Log.d(EditProfileGetService.TAG, "CODE: ${resp?.code}, MESSAGE : ${resp?.message}")
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<EditProfilePutResponse>, t: Throwable) {
//                editProfilePutListView.onPutEditProfileItemFailure(400, "네트워크 오류 발생")
//            }
//
//        })


}