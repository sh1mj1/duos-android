package com.example.duos.data.remote.partnerSearch

import com.example.duos.data.entities.RecommendedPartner
import com.google.gson.annotations.SerializedName

data class RecommendedPartnerResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: List<RecommendedPartner>?
)