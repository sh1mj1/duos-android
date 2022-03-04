package com.example.duos.ui.main.dailyMatching

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.data.entities.StarredFriend
import com.example.duos.databinding.ActivityDailyMatchingWriteBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.friendList.StarredFriendListRVAdapter

class DailyMatchingWrite : BaseActivity<ActivityDailyMatchingWriteBinding>(ActivityDailyMatchingWriteBinding::inflate) {
    override fun initAfterBinding() {
        binding.dailyMatchingWriteSelectTimeRecyclerviewRv.setHasFixedSize(true)
        binding.dailyMatchingWriteSelectTimeRecyclerviewRv.itemAnimator = null
        binding.dailyMatchingWriteSelectTimeRecyclerviewRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dailyMatchingWriteSelectTimeRecyclerviewRv.adapter = DailyMatchingTimeSelectRVAdapter(0)
    }
}