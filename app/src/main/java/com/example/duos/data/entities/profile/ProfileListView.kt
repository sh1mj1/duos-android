package com.example.duos.data.entities.profile

import com.example.duos.data.entities.MyProfileResult


interface ProfileListView {
    fun onGetMyProfileInfoSuccess(myProfile: MyProfileResult)
    fun onGetMyProfileInfoFailure(code: Int, message: String)
}
