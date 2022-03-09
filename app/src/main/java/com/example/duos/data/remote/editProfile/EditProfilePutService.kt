package com.example.duos.data.remote.editProfile

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.editProfile.EditProfilePicPutListView
import com.example.duos.data.entities.editProfile.EditProfilePutListView
import com.example.duos.data.entities.editProfile.EditProfilePutReqDto
import com.example.duos.utils.NetworkModule
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EditProfilePutService {
    val TAG = "EditProfilePutService"
    val retrofit = NetworkModule.getRetrofit()
    fun putEditNonPicProfile(
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

        val editProfilePutReqDto =
            EditProfilePutReqDto(phoneNumber, nickname, birth, gender, locationIdx, experienceIdx, introduction)

        editProfilePutService.onPutProfileWithoutPic(editProfilePutReqDto, userIdx)
            .enqueue(object : Callback<EditProfilePutResponse> {
                override fun onResponse(call: Call<EditProfilePutResponse>, response: Response<EditProfilePutResponse>) {
                    val resp = response.body()!!
                    Log.d("EditProfileFrag", "${resp.toString()} , ${editProfilePutReqDto}")
                    when (resp.code) {
                        1100 -> {
                            resp.let {
                                editProfilePutListView.onPutEditNonPicProfileItemSuccess(it, resp.message)
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
                    editProfilePutListView.onPutEditNonPicProfileItemFailure(400, "네트워크 오류 발생")
                }

            })
    }

    fun putPicEditProfile(editProfilePicPutListView: EditProfilePicPutListView, mFile: MultipartBody.Part?, userIdx: Int) {
        val putPicEditProfileService = retrofit.create(EditProfileRetrofitInterface::class.java)

        putPicEditProfileService.onPutProfilePic(mFile, userIdx).enqueue(object :Callback<EditProfilePutPicResponse>{
            override fun onResponse(call: Call<EditProfilePutPicResponse>, response: Response<EditProfilePutPicResponse>) {
                val resp = response.body()!!
                Log.d("EditProfilePutPicService", resp.toString())

                when(resp.code){
                    1101 -> editProfilePicPutListView.onPutPicEditProfileItemSuccess(resp)
                    else -> editProfilePicPutListView.onPutPicEditProfileItemFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<EditProfilePutPicResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                editProfilePicPutListView.onPutPicEditProfileItemFailure(400, "네트워크 오류 발생")
            }

        })

    }

}