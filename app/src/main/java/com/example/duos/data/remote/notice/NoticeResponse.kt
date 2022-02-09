package com.example.duos.data.remote.notice

import com.google.gson.annotations.SerializedName

data class NoticeGetResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<NoticeGetResult>

)

data class NoticeGetResult(
    @SerializedName("noticeIdx") val noticeIdx: Int? = null,
    @SerializedName("title") val title: String = "",
    @SerializedName("content") val content: String = "",
    @SerializedName("createdAt") val createdAt: String = ""
)

data class NoticeDeleteResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,

)


data class NoticePostResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,

)


data class NoticePatchResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,

)