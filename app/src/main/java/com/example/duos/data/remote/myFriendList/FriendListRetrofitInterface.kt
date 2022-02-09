package com.example.duos.data.remote.myFriendList

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FriendListRetrofitInterface {
    @GET("/api/partners/star")
    fun starredFriendList(
        @Query("userIdx") userIdx : Int
    ): Call<StarredFriendResponse>

    @GET("/api/partners/search/count")
    fun recommendedFriendList(
        @Query("userIdx") userIdx: Int
    ): Call<RecommendedFriendResponse>
}