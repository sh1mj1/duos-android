package com.example.duos.ui.main.dailyMatching

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.duos.ui.main.friendList.RecommendFriendListFragment
import com.example.duos.ui.main.friendList.StarredFriendListFragment

class DailyMatchingViewpagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllDailyMatchingFragment()
            1 -> PopularDailyMatchingFragment()
            else -> ImminentDailyMatchingFragment()
        }
    }
}