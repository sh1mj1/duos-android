package com.example.duos.data.remote.partnerProfile

import com.example.duos.data.entities.PartnerResDto
import com.google.gson.annotations.SerializedName

data class  PartnerProfileResponse(

    @SerializedName("isSuccess") val isSuccess: Boolean,
    @SerializedName("code") val code: Int,
    @SerializedName("message") val message: String,
    @SerializedName("result") val result: PartnerResDto
)
