package com.example.duos.ui.main.mypage.myprofile

import com.example.duos.data.entities.PartnerResDto

interface PartnerProfileListView {

    fun onGetProfileInfoSuccess(partnerResDto: PartnerResDto)
    fun onGetProfileInfoFailure(code: Int, message: String)


}