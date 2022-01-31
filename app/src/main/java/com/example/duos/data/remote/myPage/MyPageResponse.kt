package com.example.duos.data.remote.myPage

import com.example.duos.data.entities.MyPageItem
import com.google.gson.annotations.SerializedName

data class MyPageResponse(

    // @SerializedName으로 일치시켜 주지않을 경우엔 클래스 변수명이 일치해야함
    // @SerializedName()로 변수명을 입치시켜주면 클래스 변수명이 달라도 알아서 매핑
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,

//    @SerializedName("result") val result : List<MyPageListItem> = listOf()
    @SerializedName("result") val result: MyPageItem

)