package com.example.duos.ui.main.mypage.myprofile

import com.example.duos.data.remote.everyReviews.EveryReviewsResponse


interface EveryReviewsItemView {
    fun onGetEveryReviewsItemSuccess(everyReviewsResponse: EveryReviewsResponse)
    fun onGetEveryReviewsItemFailure(code: Int, message:String)
}