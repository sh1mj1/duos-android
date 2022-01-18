package com.example.duos.ui.signup.localSearch

import com.example.duos.data.entities.LargeLocal

interface LargeLocalSearchView {
    fun LargeLocalToRoomDB(largeLocalList: List<LargeLocal>)
    fun LargeLocalSearchOnView()
}