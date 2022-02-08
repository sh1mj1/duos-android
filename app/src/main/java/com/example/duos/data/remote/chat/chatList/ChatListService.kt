package com.example.duos.data.remote.chat.chatList

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.ApplicationClass.Companion.retrofit
import com.example.duos.ui.main.chat.ChatListView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ChatListService {
    //val retrofit = NetworkModule.getRetrofit()
    fun chatList(chatListView: ChatListView, userIdx: Int){
        val chatListService = retrofit.create(ChatListRetrofitInterface::class.java)

        chatListService.chatList(userIdx).enqueue(object :
            Callback<ChatListResponse> {
            override fun onResponse(call: Call<ChatListResponse>, response: Response<ChatListResponse>) {
                val resp = response.body()!!
                when (resp.code) {
                    1000 ->
                        resp.result?.let { chatListView.onGetChatListSuccess(it) }
                    else -> chatListView.onGetChatListFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<ChatListResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                chatListView.onGetChatListFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}