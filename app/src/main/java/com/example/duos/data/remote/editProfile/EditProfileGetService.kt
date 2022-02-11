package com.example.duos.data.remote.editProfile

import android.util.Log
import com.example.duos.data.entities.editProfile.EditProfileListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object EditProfileGetService {
    val TAG = "EditProfileGetService"
    val retrofit = NetworkModule.getRetrofit()

    fun getEditProfile(editProfileListView: EditProfileListView, userIdx: Int) {
        val editProfileService = retrofit.create(EditProfileRetrofitInterface::class.java)

        editProfileService.onGetProfile(userIdx).enqueue(object : Callback<EditProfileResponse> {
            override fun onResponse(call: Call<EditProfileResponse>, response: Response<EditProfileResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 -> {
                        resp.result.let {
                            editProfileListView.onGetEditProfileItemSuccess(it)
                            Log.d(TAG, resp.toString())
                            Log.d(TAG, resp.result.toString())
                            Log.d(TAG, resp.result.existingProfileInfo.toString())
                        }
                    }

                    else -> {
                        Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
                    }
                }
            }

            override fun onFailure(call: Call<EditProfileResponse>, t: Throwable) {
                editProfileListView.onGetEditItemFailure(400, "네트워크 오류 발생")
            }


        })

    }

}