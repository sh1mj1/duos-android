package com.example.duos.data.entities.everyReviews

import com.example.duos.data.remote.everyReviews.EveryReviewsResponse


interface EveryReviewsItemView {
    fun onGetEveryReviewsItemSuccess(everyReviewsResponse: EveryReviewsResponse)
    fun onGetEveryReviewsItemFailure(code: Int, message:String)
}