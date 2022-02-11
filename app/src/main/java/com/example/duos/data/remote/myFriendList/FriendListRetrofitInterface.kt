package com.example.duos.data.remote.myFriendList

import androidx.room.Delete
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface FriendListRetrofitInterface {
    @GET("/api/partners/star")
    fun starredFriendList(
        @Query("userIdx") userIdx : Int
    ): Call<StarredFriendResponse>

    @GET("/api/partners/recommendation/history")
    fun recommendedFriendList(
        @Query("userIdx") userIdx: Int
    ): Call<RecommendedFriendResponse>

    @POST("/api/partners/star")
    fun addStarredFriend(
        @Query("userIdx") userIdx: Int,
        @Query("partnerIdx") partnerIdx : Int
    ): Call<AddStarredFriendResponse>

    @DELETE("/api/partners/star")
    fun deleteStarredFriend(
        @Query("userIdx") userIdx: Int,
        @Query("partnerIdx") partnerIdx : Int
    ): Call<DeleteStarredFriendResponse>

    @DELETE("/api/partners/recommendation")
    fun deleteRecommendedFriend(

    )
}