package com.example.duos.ui.main.partnerSearch

import com.example.duos.databinding.FragmentPartnerSearchBinding
import com.example.duos.ui.BaseFragment

class PartnerSearchFragment(): BaseFragment<FragmentPartnerSearchBinding>(FragmentPartnerSearchBinding::inflate) {

    override fun initAfterBinding() {

    }

    companion object {
        fun newInstance(): PartnerSearchFragment = PartnerSearchFragment()
    }
}