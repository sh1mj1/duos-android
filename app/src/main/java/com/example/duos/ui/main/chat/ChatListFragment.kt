package com.example.duos.ui.main.chat

import com.example.duos.databinding.FragmentChatBinding
import com.example.duos.ui.BaseFragment

class ChatListFragment(): BaseFragment<FragmentChatBinding>(FragmentChatBinding::inflate) {

    override fun initAfterBinding() {

    }

    companion object {
        fun newInstance(): ChatListFragment = ChatListFragment()
    }
}