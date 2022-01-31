package com.example.duos.ui.main.mypage.myprofile.frag

import com.example.duos.data.entities.MyPageItem

interface MyPageItemView {
    fun onGetMyPageItemSuccess(myPageItem: MyPageItem)
    fun onGetMyPageItemFailure(code: Int, message:String)
}