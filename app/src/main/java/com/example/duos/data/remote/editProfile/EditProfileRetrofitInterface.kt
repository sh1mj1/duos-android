package com.example.duos.data.remote.editProfile

import com.example.duos.ApplicationClass.Companion.EDIT_GET_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EditProfileRetrofitInterface {

    @GET(EDIT_GET_API)
    fun onGetProfile(@Query("userIdx")userIdx : Int) : Call<EditProfileResponse>
}