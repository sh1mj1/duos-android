package com.example.duos.ui.main.friendList

import com.example.duos.data.remote.myFriendList.RecommendHistoryDto

interface RecommendedFriendListView {
    fun onGetRecommendedFriendListSuccess(starredFriendList: List<RecommendHistoryDto>)
    fun onGetRecommendedFriendListFailure(code: Int, message: String)
}