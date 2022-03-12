package com.example.duos.ui.main.dailyMatching

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.R
import com.example.duos.databinding.FragmentDailyMatchingBinding
import com.example.duos.ui.BaseFragment
import com.google.android.material.tabs.TabLayoutMediator
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.duos.ui.main.dailyMatching.dailyMatchingSearch.DailyMatchingSearchActivity
import com.example.duos.utils.ViewModel


class DailyMatchingFragment :
    BaseFragment<FragmentDailyMatchingBinding>(FragmentDailyMatchingBinding::inflate) {

    lateinit var viewModel : ViewModel
    var currentPage : Int = 0

    override fun initAfterBinding() {
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        val dailyMatchingViewpagerAdapter = DailyMatchingViewpagerAdapter(this)
        binding.dailyMatchingContentViewpagerVp.adapter = dailyMatchingViewpagerAdapter

        binding.dailyMatchingContentViewpagerVp.offscreenPageLimit = 3

        val child = binding.dailyMatchingContentViewpagerVp.getChildAt(0)
        (child as RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER

        TabLayoutMediator(
            binding.dailyMatchingContentTabTb,
            binding.dailyMatchingContentViewpagerVp
        ) { tab, position ->
            tab.text = arrayListOf(
                getString(R.string.daily_matching_tab_01),
                getString(R.string.daily_matching_tab_02),
                getString(R.string.daily_matching_tab_03)
            )[position]
        }.attach()

        binding.dailyMatchingGotoWriteFrameLayoutFl.setOnClickListener {
            val intent = Intent(activity, DailyMatchingWrite::class.java)
            startActivity(intent)
        }

        // 검색 중임
        binding.dailyMatchingSearchImageIv.setOnClickListener {
            val intent = Intent(activity, DailyMatchingSearchActivity::class.java)
            startActivity(intent)
        }

        binding.dailyMatchingSwiperefreshLayoutSl.setOnRefreshListener {
            Log.d("새로고침","성공")
            when (currentPage){
                0 -> viewModel.dailyMatchingRefreshSwipeAll.value = true
                1 -> viewModel.dailyMatchingRefreshSwipePopular.value = true
                2 -> viewModel.dailyMatchingRefreshSwipeImminent.value = true
            }
            binding.dailyMatchingSwiperefreshLayoutSl.isRefreshing = false
            when (currentPage){
                0 -> viewModel.dailyMatchingRefreshSwipeAll.value = false
                1 -> viewModel.dailyMatchingRefreshSwipePopular.value = false
                2 -> viewModel.dailyMatchingRefreshSwipeImminent.value = false
            }

        }

        binding.dailyMatchingContentViewpagerVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                currentPage = position
                Log.e("ViewPagerFragment", "Page ${position}")
            }
        })


    }
}