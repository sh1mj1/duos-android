package com.example.duos.ui.main.friendList

import com.example.duos.data.entities.StarredFriend

interface AddStarredFriendView {
    fun onAddStarredFriendSuccess()
    fun onAddStarredFriendFailure(code: Int, message: String)
}