package com.example.duos.ui.main.partnerSearch

import com.example.duos.data.entities.PartnerSearchData

interface PartnerSearchFilterCountView {
    fun onPartnerSearchFilterCountSuccess(searchCount : Int)
    fun onPartnerSearchFilterCountFailure(code: Int, message: String)
}