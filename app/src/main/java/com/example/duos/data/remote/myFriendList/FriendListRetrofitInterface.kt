package com.example.duos.data.remote.myFriendList

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendListRetrofitInterface {
    @GET("/test/partner/starred")
    fun myFriendList(
        @Query("userIdx") userIdx : Int
    ): Call<StarredFriendResponse>
}