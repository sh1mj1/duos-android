package com.example.duos.data.entities.block_report

import com.google.gson.annotations.SerializedName

data class BlockRequest (
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("blockRequestedUserIdx") val blockRequestedUserIdx : Int
)

data class ReportRequest(
    @SerializedName("userIdx") val userIdx : Int,
    @SerializedName("reportedUserIdx") val reportedUserIdx : Int
)