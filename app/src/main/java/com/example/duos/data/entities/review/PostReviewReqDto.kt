package com.example.duos.data.entities.review

data class PostReviewReqDto(
    val writerIdx: Int,
    val revieweeIdx: Int,
    val rating: Float,
    val reviewContent: String,
    val createdAt: String,
    val appointmentIdx: Int
)
