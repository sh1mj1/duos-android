package com.example.duos.ui.main.chat

import com.example.duos.databinding.FragmentChatBinding
import com.example.duos.ui.BaseFragment

class ChatFragment(): BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    override fun initAfterBinding() {

    }

    companion object {
        fun newInstance(): ChatFragment = ChatFragment()
    }
}