package com.example.duos.ui.main.mypage

import com.example.duos.databinding.FragmentMypageBinding
import com.example.duos.ui.BaseFragment

class MypageFragment(): BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate) {

    override fun initAfterBinding() {

    }

    companion object {
        fun newInstance(): MypageFragment = MypageFragment()
    }
}