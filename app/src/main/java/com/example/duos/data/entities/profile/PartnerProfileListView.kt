package com.example.duos.data.entities.profile

import com.example.duos.data.entities.PartnerResDto

interface PartnerProfileListView {

    fun onGetProfileInfoSuccess(partnerResDto: PartnerResDto)
    fun onGetProfileInfoFailure(code: Int, message: String)


}