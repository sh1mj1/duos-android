package com.example.duos.ui.signup.localSearch

import com.example.duos.data.entities.LocalCategory

interface LargeLocalSearchView {
    fun LargeLocalToRoomDB(localCategoryList: List<LocalCategory>)
    fun LargeLocalSearchOnView()
}