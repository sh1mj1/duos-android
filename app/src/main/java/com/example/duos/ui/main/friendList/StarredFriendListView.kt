package com.example.duos.ui.main.friendList

import com.example.duos.data.entities.StarredFriend

interface StarredFriendListView {
    fun onGetMyFriendListSuccess(myFriendList: List<StarredFriend>)
    fun  onGetMyFriendListFailure(code: Int, message: String)
}