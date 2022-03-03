package com.example.duos.ui.main.dailyMatching

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class DailyMatchingViewpagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllDailyMatchingFragment()
            1 -> PopularAllDailyMatchingFragment()
            else -> ImminentAllDailyMatchingFragment()
        }
    }
}