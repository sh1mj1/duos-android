package com.example.duos.data.entities.editProfile


interface EditProfileListView {
    fun onGetEditProfileItemSuccess(getEditProfileResDto: GetEditProfileResDto)
    fun onGetEditItemFailure(code: Int, message:String)
}
