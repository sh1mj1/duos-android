package com.example.duos.ui.main.friendList

import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.databinding.FragmentFriendListBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.utils.FriendListCountViewModel
import com.google.android.material.tabs.TabLayoutMediator

class FriendListFragment(): BaseFragment<FragmentFriendListBinding>(FragmentFriendListBinding::inflate){


    lateinit var sharedViewModel: FriendListCountViewModel

    override fun initAfterBinding() {
        var friendCount : Int // 서버에서 친구 수 받아오기

        val friendListViewpagerAdapter = FriendListViewpagerAdapter(this)
        binding.friendListContentVp.adapter = friendListViewpagerAdapter

        val child = binding.friendListContentVp.getChildAt(0)
        (child as RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        sharedViewModel = ViewModelProvider(requireActivity()).get(FriendListCountViewModel::class.java)
        sharedViewModel.currentCount.observe(this, Observer {
            friendCount = it
            TabLayoutMediator(binding.friendListContentTb, binding.friendListContentVp){
                    tab, position -> tab.text =  arrayListOf("찜한 친구($friendCount)", "지난 추천 친구")[position]
            }.attach()
        })
    }

    companion object {
        fun newInstance(): FriendListFragment = FriendListFragment()
    }

}