package com.example.duos.data.remote.myFriendList

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.ui.main.friendList.AddStarredFriendView
import com.example.duos.ui.main.friendList.DeleteStarredFriendView
import com.example.duos.ui.main.friendList.RecommendedFriendListView
import com.example.duos.ui.main.friendList.StarredFriendListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FriendListService {
    val retrofit = NetworkModule.getRetrofit()
    fun getStarredFriendList(starredFriendListView: StarredFriendListView, userIdx : Int) {
        val friendListService = retrofit.create(FriendListRetrofitInterface::class.java)

        friendListService.starredFriendList(userIdx).enqueue(object : Callback<StarredFriendResponse> {
            override fun onResponse(call: Call<StarredFriendResponse>, response: Response<StarredFriendResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 ->
                        resp.result?.let { starredFriendListView.onGetStarredFriendListSuccess(it) }
                    else -> starredFriendListView.onGetStarredFriendListFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<StarredFriendResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())
                starredFriendListView.onGetStarredFriendListFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun getRecommendedFriendList(recommendedFriendListView: RecommendedFriendListView, userIdx: Int){
        val friendListService = retrofit.create(FriendListRetrofitInterface::class.java)

        friendListService.recommendedFriendList(userIdx).enqueue(object : Callback<RecommendedFriendResponse> {
            override fun onResponse(call: Call<RecommendedFriendResponse>, response: Response<RecommendedFriendResponse>) {
                val resp = response.body()!!

                when (resp.code) {
                    1000 ->
                        resp.result?.let { recommendedFriendListView.onGetRecommendedFriendListSuccess(it) }
                    else -> recommendedFriendListView.onGetRecommendedFriendListFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<RecommendedFriendResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())
                recommendedFriendListView.onGetRecommendedFriendListFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }

    fun addStarredFriend(addStarredFriendView: AddStarredFriendView, userIdx: Int, partnerIdx: Int){
        val addStarredFriendservice = retrofit.create(FriendListRetrofitInterface::class.java)

        addStarredFriendservice.addStarredFriend(userIdx, partnerIdx).enqueue(object : Callback<AddStarredFriendResponse>{
            override fun onResponse(
                call: Call<AddStarredFriendResponse>,
                response: Response<AddStarredFriendResponse>
            ) {

                val resp = response.body()!!
                when (resp.code) {
                    1000 -> addStarredFriendView.onAddStarredFriendSuccess()
                    else -> addStarredFriendView.onAddStarredFriendFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<AddStarredFriendResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())
                addStarredFriendView.onAddStarredFriendFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun deleteStarredFriend(deleteStarredFriendView: DeleteStarredFriendView, userIdx: Int, partnerIdx: Int){
        val deleteStarredFriendservice = retrofit.create(FriendListRetrofitInterface::class.java)

        deleteStarredFriendservice.deleteStarredFriend(userIdx, partnerIdx).enqueue(object : Callback<DeleteStarredFriendResponse>{
            override fun onResponse(
                call: Call<DeleteStarredFriendResponse>,
                response: Response<DeleteStarredFriendResponse>
            ) {

                val resp = response.body()!!
                when (resp.code) {
                    1000 -> deleteStarredFriendView.onDeleteStarredFriendSuccess()
                    else -> deleteStarredFriendView.onDeleteStarredFriendFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<DeleteStarredFriendResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())
                deleteStarredFriendView.onDeleteStarredFriendFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }
}