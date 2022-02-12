package com.example.duos.data.remote.chat.chat

import android.util.Log
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.chat.CreateChatRoomData
import com.example.duos.data.entities.chat.SyncChatMessageRequestBody
import com.example.duos.data.entities.chat.sendMessageData
import com.example.duos.ui.main.chat.ChatMessageView
import com.example.duos.ui.main.chat.CreateChatRoomView
import com.example.duos.ui.main.chat.SendMessageView
import com.example.duos.utils.NetworkModule
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

object ChatService {
    val retrofit = NetworkModule.getRetrofit()
    fun createChatRoom(createChatRoomView: CreateChatRoomView, thisUserIdx: Int, targetUserIdx: Int){
        val createChatRoomService = retrofit.create(ChatRetrofitInterface::class.java)
        val createChatRoomRequestInfo = CreateChatRoomData(thisUserIdx, targetUserIdx)

        createChatRoomView.onCreateChatRoomLoading()

        createChatRoomService.createChatRoom(createChatRoomRequestInfo).enqueue(object :
            Callback<CreateChatRoomResponse>
        {
            override fun onResponse(
                call: Call<CreateChatRoomResponse>,
                response: Response<CreateChatRoomResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1000 -> createChatRoomView.onCreateChatRoomSuccess()
                    else -> createChatRoomView.onCreateChatRoomFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<CreateChatRoomResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                createChatRoomView.onCreateChatRoomFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun sendMessage(sendMessageView: SendMessageView, receiverIdx: Int, senderIdx: Int, message: String, type: String, chatRoomIdx: String){
        val sendMessageService = retrofit.create(ChatRetrofitInterface::class.java)
        val message = sendMessageData(chatRoomIdx, type, senderIdx, receiverIdx, message)

        sendMessageService.sendMessage(message).enqueue(object: Callback<SendMessageResponse>{
            override fun onResponse(
                call: Call<SendMessageResponse>,
                response: Response<SendMessageResponse>
            ) {
                val resp = response.body()!!
                Log.d("resp", resp.toString())

                when(resp.code){
                    1000 -> sendMessageView.onSendMessageSuccess()
                    else -> sendMessageView.onSendMessageFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SendMessageResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())

                sendMessageView.onSendMessageFailure(400, "네트워크 오류가 발생했습니다.")
            }

        })
    }

    fun syncChatMessage(chatMessageView: ChatMessageView, chatMessageIdx: String, chatRoomIdx: String){
        val syncChatMessageService = retrofit.create(ChatRetrofitInterface::class.java)
        val syncChatMessageRequestBody = SyncChatMessageRequestBody(chatMessageIdx, chatRoomIdx)

        syncChatMessageService.syncChatMessage(syncChatMessageRequestBody).enqueue(object :
            Callback<SyncChatMessageResponse> {
            override fun onResponse(call: Call<SyncChatMessageResponse>, response: Response<SyncChatMessageResponse>) {
                val resp = response.body()!!
                Log.d("resp",resp.toString())
                when (resp.code) {
                    1000 ->
                        resp.result?.let { chatMessageView.onSyncChatMessageSuccess(it) }
                    else -> chatMessageView.onSyncChatMessageFailure(resp.code, resp.message)
                }
            }

            override fun onFailure(call: Call<SyncChatMessageResponse>, t: Throwable) {
                Log.d("${ApplicationClass.TAG}/API-ERROR", t.message.toString())
                chatMessageView.onSyncChatMessageFailure(400, "네트워크 오류가 발생했습니다.")
            }
        })
    }
}