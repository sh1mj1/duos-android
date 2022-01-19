package com.example.duos.ui.main.friendList

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class FriendListViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> StarredFriendListFragment()
            else -> RecommendFriendListFragment()

        }
    }
}