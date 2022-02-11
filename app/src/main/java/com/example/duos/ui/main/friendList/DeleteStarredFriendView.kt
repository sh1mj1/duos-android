package com.example.duos.ui.main.friendList

interface DeleteStarredFriendView {
    fun onDeleteStarredFriendSuccess()
    fun onDeleteStarredFriendFailure(code: Int, message: String)
}