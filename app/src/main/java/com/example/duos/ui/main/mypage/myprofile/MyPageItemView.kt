package com.example.duos.ui.main.mypage.myprofile

import com.example.duos.data.entities.MyPageInfo

interface MyPageItemView {
    fun onGetMyPageItemSuccess(myPageInfo: MyPageInfo)
    fun onGetMyPageItemFailure(code: Int, message:String)
}