package com.example.duos.data.remote.partnerSearch

import com.example.duos.data.entities.PartnerSearchData
import com.google.gson.annotations.SerializedName

data class PartnerSearchFilterCountResultData(@SerializedName("searchCnt") val searchCnt : Int)

data class PartnerSearchFilterCountResponse(
    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PartnerSearchFilterCountResultData
)
