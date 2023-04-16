package com.example.duos.ui.main.mypage.myboard

import android.content.Context
import android.content.Intent
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.dailyMatching.DailyMatching
import com.example.duos.data.entities.dailyMatching.MyDailyMatchingListResult
import com.example.duos.data.remote.dailyMatching.DailyMatchingListService
import com.example.duos.databinding.FragmentMyBoardBinding
import com.example.duos.ui.BaseFragment
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
        progressON()
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

        (context as MyBoardActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            requireActivity().finish()
        }
    }

    override fun onMyDailyMatchingViewSuccess(myDailyMatchingListResult: MyDailyMatchingListResult) {
        Log.d("result",myDailyMatchingListResult.toString())
        val activeBoardList : List<DailyMatching> = myDailyMatchingListResult.activePostList
        val oldBoardList : List<DailyMatching> = myDailyMatchingListResult.oldPostList

        activeBoardListRVAdapter.setPagingMessages(activeBoardList)
        oldBoardListRVAdapter.setPagingMessages(oldBoardList)

        if (activeBoardList.isEmpty()){
                binding.myboardOldLayoutCl.visibility = View.VISIBLE
            (binding.myboardOldLayoutCl.layoutParams as LinearLayoutCompat.LayoutParams).apply {
                // individually set text view any side margin
                topMargin = 0.dpToPixels(requireContext())
            }
        }

        if (oldBoardList.isEmpty()){
            binding.myboardOldLayoutCl.visibility = View.INVISIBLE
        } else {
            binding.myboardOldLayoutCl.visibility = View.VISIBLE
        }

        progressOFF()
    }

    // extension function to convert dp to equivalent pixels
    fun Int.dpToPixels(context: Context):Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
    ).toInt()

    override fun onMyDailyMatchingViewFailure(code: Int, message: String) {
        progressOFF()
    }

    override fun onItemClicked(boardIdx: Int, recruitmentStatus: String) {
        val intent = Intent(activity, DailyMatchingDetail::class.java)
        intent.putExtra("boardIdx", boardIdx)
        intent.putExtra("recruitmentStatus", recruitmentStatus)
        startActivity(intent)
    }
}