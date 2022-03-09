package com.example.duos.ui.main.mypage.myboard

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.data.entities.dailyMatching.DailyMatching
import com.example.duos.data.entities.dailyMatching.MyDailyMatchingListResult
import com.example.duos.data.remote.dailyMatching.DailyMatchingListService
import com.example.duos.data.remote.dailyMatching.DailyMatchingWriteService
import com.example.duos.databinding.FragmentMyBoardBinding
import com.example.duos.databinding.FragmentSetupBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.main.dailyMatching.DailyMatchingDetail
import com.example.duos.ui.main.dailyMatching.DailyMatchingListRVAdapter
import com.example.duos.ui.main.dailyMatching.MyDailyMatchingView
import com.example.duos.utils.getUserIdx

class MyBoardFragment: BaseFragment<FragmentMyBoardBinding>(FragmentMyBoardBinding::inflate), MyDailyMatchingView, DailyMatchingListRVAdapter.OnItemClickListener {

    lateinit var activeBoardListRVAdapter : DailyMatchingListRVAdapter
    lateinit var oldBoardListRVAdapter : DailyMatchingListRVAdapter
    lateinit var mContext: MyBoardActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MyBoardActivity) {
            mContext = context
        }
    }

    override fun onResume() {
        super.onResume()
        DailyMatchingListService.getMyDailyMatching(this, getUserIdx()!!)
    }

    override fun initAfterBinding() {
        binding.myboardActiveBoardRecyclerviewRv.setHasFixedSize(true)
        binding.myboardActiveBoardRecyclerviewRv.itemAnimator = DefaultItemAnimator()
        binding.myboardActiveBoardRecyclerviewRv.layoutManager = LinearLayoutManager(requireContext())
        activeBoardListRVAdapter = DailyMatchingListRVAdapter(this)
        binding.myboardActiveBoardRecyclerviewRv.adapter = activeBoardListRVAdapter

        binding.myboardOldBoardRecyclerviewRv.setHasFixedSize(true)
        binding.myboardOldBoardRecyclerviewRv.itemAnimator = DefaultItemAnimator()
        binding.myboardOldBoardRecyclerviewRv.layoutManager = LinearLayoutManager(requireContext())
        oldBoardListRVAdapter = DailyMatchingListRVAdapter(this)
        binding.myboardOldBoardRecyclerviewRv.adapter = oldBoardListRVAdapter
    }

    override fun onMyDailyMatchingViewSuccess(myDailyMatchingListResult: MyDailyMatchingListResult) {
        Log.d("result",myDailyMatchingListResult.toString())
        val activeBoardList : List<DailyMatching> = myDailyMatchingListResult.activePostList
        val oldBoardList : List<DailyMatching> = myDailyMatchingListResult.oldPostList

        activeBoardListRVAdapter.setPagingMessages(activeBoardList)
        oldBoardListRVAdapter.setPagingMessages(oldBoardList)

        if (oldBoardList.isEmpty()){
            binding.myboardOldLayoutCl.visibility = View.INVISIBLE
        }

    }

    override fun onMyDailyMatchingViewFailure(code: Int, message: String) {

    }

    override fun onItemClicked(boardIdx: Int, recruitmentStatus: String) {
        Log.d("상태", "$boardIdx $recruitmentStatus")
        if (recruitmentStatus.equals("recruiting")) {
            val intent = Intent(activity, DailyMatchingDetail::class.java)
            intent.putExtra("boardIdx", boardIdx)
            startActivity(intent)
        }
    }
}