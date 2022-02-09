package com.example.duos.data.remote.duplicate

import android.util.Log
import com.example.duos.data.entities.duplicate.DuplicateNicknameListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.create

object DuplicateNicknameService {
    val TAG = "DuplicateNicknameService"
    val retrofit = NetworkModule.getRetrofit()

    fun getDuplicateNickname(
        duplicateNicknameListView: DuplicateNicknameListView,
        userIdx: Int,
        nickname: String
    ) {
        val duplicateNicknameService = retrofit.create(DuplicateRetrofitInterface::class.java)

        duplicateNicknameService.onGetDuplicateNicknameBoolean(userIdx, nickname)
            .enqueue(object : Callback<DuplicateNicknameResponse> {
                override fun onResponse(
                    call: Call<DuplicateNicknameResponse>,
                    response: Response<DuplicateNicknameResponse>
                ) {
                    val resp = response.body()!!
                    when (resp.code){
                        1110 -> {
                            resp.let{
                                duplicateNicknameListView.onGetDuplicateNicknameSuccess(it)
                                Log.d(TAG, resp.toString())
                            }
                        }
                        else -> {
                            Log.d(TAG, "CODE: ${resp.code}, MESSAGE : ${resp.message}")
                        }
                    }
                }

                override fun onFailure(call: Call<DuplicateNicknameResponse>, t: Throwable) {
                    duplicateNicknameListView.onGetDuplicateNicknameFailure(400, "네트워크 오류 발생")
                }

            })
    }


}