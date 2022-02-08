package com.example.duos.data.entities.review

import com.example.duos.data.remote.reviews.ReviewResponse

interface ReviewListView {

    fun onPostReviewSuccess(reviewResponse: ReviewResponse)
    fun onPostReviewFailure(code:Int, message : String)

}