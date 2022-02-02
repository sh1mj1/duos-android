package com.example.duos.ui.main.mypage.myprofile.frag

import com.example.duos.data.entities.MyProfileResult


interface ProfileListView {

    fun onGetMyProfileInfoSuccess(myProfile: MyProfileResult)
    fun onGetMyProfileInfoFailure(code: Int, message: String)

//    fun onGetChatListSuccess(chatList: List<ChatListItem>)
//    fun onGetChatListFailure(code: Int, message: String)


}
