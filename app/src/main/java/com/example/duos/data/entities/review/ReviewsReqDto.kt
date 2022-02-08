package com.example.duos.data.entities.review

import com.google.gson.annotations.SerializedName

data class ReviewsReqDto(

    @SerializedName("writerIdx") val writerIdx: Int,
    @SerializedName("revieweeIdx") val revieweeIdx: Int,
    @SerializedName("rating") val rating: Float,
    @SerializedName("reviewContent") val reviewContent: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("appointmentIdx") val appointmentIdx: Int

)

/*
ReviewReqDto
ㄴwriterIdx	Integer
ㄴrevieweeIdx	Integer
ㄴrating	Float
ㄴreviewContent	String
ㄴcreatedAt	LocalDateTime
ㄴappointmentIdx	int
 */