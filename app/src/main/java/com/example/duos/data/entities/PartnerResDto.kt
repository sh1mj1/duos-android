package com.example.duos.data.entities

import com.google.gson.annotations.SerializedName

data class PartnerResDto(

    @SerializedName("partnerInfoDto") var partnerInfoDto : PartnerInfoDto,
    @SerializedName("reviewResDto") var reviewResDto : ReviewResDto

)
