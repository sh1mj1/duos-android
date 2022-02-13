package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class PartnerResDto(
    @SerializedName("todayChatAvailCnt") var todayChatAvailCnt : Int,
    @SerializedName("hasChatRoomAlready") var hasChatRoomAlready : Boolean,
    @SerializedName("partnerInfoDto") var partnerInfoDto : PartnerInfoDto,
    @SerializedName("reviewResDto") var reviewResDto : List<ReviewResDto>

)
