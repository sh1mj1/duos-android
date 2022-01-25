package com.example.duos.ui.main.mypage.myprofile.frag

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.duos.R
import com.example.duos.databinding.FragmentChatListBinding
import com.example.duos.databinding.FragmentPlayerBinding
import com.example.duos.ui.BaseFragment

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate) {


    override fun initAfterBinding() {

        val playerNickname = arguments?.getString("nickname")
        val playerIntroduction = arguments?.getString("introduction")
        val playerImg = arguments?.getInt("coverImg")

        binding.playerNicknameTv.text = playerNickname.toString()
        binding.playerIntroductionTv.text = playerIntroduction.toString()
        binding.playerProfileImgIv.setImageResource(playerImg!!)
        
        
        // 리사이클러 뷰 아이템 클릭시 해당 회원 프로필로 이동하기


    }


}

