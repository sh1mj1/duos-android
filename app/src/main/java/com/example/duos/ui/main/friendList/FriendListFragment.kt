package com.example.duos.ui.main.friendList

import com.example.duos.databinding.FragmentFriendListBinding
import com.example.duos.ui.BaseFragment

class FriendListFragment(): BaseFragment<FragmentFriendListBinding>(FragmentFriendListBinding::inflate) {

    override fun initAfterBinding() {

    }

    companion object {
        fun newInstance(): FriendListFragment = FriendListFragment()
    }
}