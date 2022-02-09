package com.example.duos.data.remote.duplicate

import com.example.duos.ApplicationClass.Companion.DUPLICATE_NICKNAME_API
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface DuplicateRetrofitInterface {

    @GET(DUPLICATE_NICKNAME_API)
    fun onGetDuplicateNicknameBoolean(
        @Query("userIdx") userIdx: Int,
        @Query("nickname") nickname: String
    ): Call<DuplicateNicknameResponse>
}