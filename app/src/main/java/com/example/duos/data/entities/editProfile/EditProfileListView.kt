package com.example.duos.data.entities.editProfile

import com.example.duos.data.remote.editProfile.EditProfilePutResponse


interface EditProfileListView {
    fun onGetEditProfileItemSuccess(getEditProfileResDto: GetEditProfileResDto)
    fun onGetEditItemFailure(code: Int, message:String)

//    fun onPutEditProfileItemSuccess(editputProfileResponse: EditProfilePutResponse)
//    fun onPutEditProfileItemFailure(code : Int, message : String)
}

interface EditProfilePutListView {

    fun onPutEditProfileItemSuccess(editPutProfileResponse: EditProfilePutResponse, message:String)
    fun onPutEditProfileItemFailure(code : Int, message : String)
}
