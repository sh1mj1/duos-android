package com.example.duos.ui.main.partnerSearch

import com.example.duos.data.entities.RecommendedPartner
import com.example.duos.data.remote.partnerSearch.PartnerSearchData

interface PartnerSearchView {
    // 사용자 정보는 로컬에서 가져와도 될 듯 해서 일단 뺌
//    fun onGetUserSimpleDataSuccess(userSimple: UserSimple)
//    fun onGetUserSimpleDataFailure(code: Int, message: String)
    fun onGetPartnerSearchDataSuccess(partnerSearchData: List<PartnerSearchData>)
    fun onGetPartnerSearchDataFailure(code: Int, message: String)
}