package com.example.duos.data.remote.chat.chat

import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime


data class SendMessageResultData(
    @SerializedName("chatMessageIdx") val chatMessageIdx: String,
    @SerializedName("sendAt") val sentAt: LocalDateTime     // LocalDateTime 라이브러리 일단 사용.. 안되면 import 다시
)
data class SendMessageResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: SendMessageResultData
)
