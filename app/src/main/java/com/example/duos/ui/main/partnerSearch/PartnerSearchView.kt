package com.example.duos.ui.main.partnerSearch

import com.example.duos.data.entities.PartnerSearchData


interface PartnerSearchView {
    fun onGetPartnerSearchDataSuccess(partnerSearchData: PartnerSearchData)
    fun onGetPartnerSearchDataFailure(code: Int, message: String)
}