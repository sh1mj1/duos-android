package com.example.duos.ui.main.partnerSearch

interface PartnerBlockView {
    fun onPartnerBlockSuccess()
    fun onPartnerBlockFailure(code: Int, message: String)
}

interface PartnerReportView {
    fun onPartnerReportSuccess()
    fun onPartnerReportFailure(code: Int, message: String)
}