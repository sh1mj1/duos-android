package com.example.duos.data.entities.editProfile

import com.example.duos.data.remote.editProfile.EditProfilePutPicResponse
import com.example.duos.data.remote.editProfile.EditProfilePutResponse


interface EditProfileListView {
    fun onGetEditProfileItemSuccess(getEditProfileResDto: GetEditProfileResDto)
    fun onGetEditItemFailure(code: Int, message: String)
}

interface EditProfilePutListView {
    fun onPutEditNonPicProfileItemSuccess(editPutProfileResponse: EditProfilePutResponse, message: String)
    fun onPutEditNonPicProfileItemFailure(code: Int, message: String)
}

interface EditProfilePicPutListView {
    fun onPutPicEditProfileItemSuccess(editProfilePutPicResponse: EditProfilePutPicResponse)
    fun onPutPicEditProfileItemFailure(code: Int, message: String)
}
