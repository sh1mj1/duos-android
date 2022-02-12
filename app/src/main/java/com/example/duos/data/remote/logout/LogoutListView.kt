package com.example.duos.data.remote.logout

interface LogoutListView {
    fun onLogOutSuccess()
    fun onLogOutFailure(code : Int , message : String)
}