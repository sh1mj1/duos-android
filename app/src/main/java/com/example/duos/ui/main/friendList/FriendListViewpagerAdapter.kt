package com.example.duos.ui.main.friendList

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.duos.ui.main.MainActivity

class FriendListViewpagerAdapter(fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> MyFriendListFragment()
            else -> LastRecommendFriendListFragment()

        }
    }
}