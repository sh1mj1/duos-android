package com.example.duos.ui.main.partnerSearch

import com.example.duos.data.entities.RecommendedPartner

interface PartnerSearchFilterCountView {
    fun onPartnerSearchFilterCountSuccess(searchCount: Int)
    fun onPartnerSearchFilterCountFailure(code: Int, message: String)

//    fun onGetPartnerFilterSuccess(recommendedPartner: List<RecommendedPartner>)
//    fun onGetPartnerFilterFailure(code: Int, message: String)
}