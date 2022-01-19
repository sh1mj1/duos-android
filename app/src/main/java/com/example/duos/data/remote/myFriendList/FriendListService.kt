package com.example.duos.data.remote.myFriendList

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.ui.main.friendList.StarredFriendListView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object FriendListService {
    fun myFriendList(myFriendListView: StarredFriendListView, userIdx : Int) {
        val friendListService = ApplicationClass.retrofit.create(FriendListRetrofitInterface::class.java)


        friendListService.myFriendList(userIdx).enqueue(object : Callback<StarredFriendResponse> {
            override fun onResponse(call: Call<StarredFriendResponse>, response: Response<StarredFriendResponse>) {
                val resp = response.body()!!

                when (resp.code) {
                    1000 ->
                        resp.result?.let { myFriendListView.onGetMyFriendListSuccess(it) }
                    else -> myFriendListView.onGetMyFriendListFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<StarredFriendResponse>, t: Throwable) {
                Log.d("$TAG/API-ERROR", t.message.toString())
                myFriendListView.onGetMyFriendListFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}