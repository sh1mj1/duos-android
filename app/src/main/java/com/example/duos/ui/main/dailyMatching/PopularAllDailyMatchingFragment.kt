package com.example.duos.ui.main.dailyMatching

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.dailyMatching.DailyMatching
import com.example.duos.data.remote.dailyMatching.DailyMatchingListRequesetBody
import com.example.duos.data.remote.dailyMatching.DailyMatchingListService
import com.example.duos.data.remote.dailyMatching.PopularDailyMatchingListResult
import com.example.duos.databinding.FragmentPopularDailyMatchingBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity
import com.example.duos.utils.getUserIdx

class PopularAllDailyMatchingFragment : BaseFragment<FragmentPopularDailyMatchingBinding>
    (FragmentPopularDailyMatchingBinding::inflate), PopularDailyMatchingView,
    DailyMatchingListRVAdapter.OnItemClickListener {

    var pageNum: Int = 0
    val listNum: Int = 30
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var mContext: MainActivity
    private var isNextPageAvailable = false // 다음 페이지 유무
    lateinit var dailyMatchingListRVAdapter: DailyMatchingListRVAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun initAfterBinding() {
        binding.popularDailyMatchingRecyclerviewRc.setHasFixedSize(true)
        binding.popularDailyMatchingRecyclerviewRc.itemAnimator = DefaultItemAnimator()
        layoutManager = LinearLayoutManager(requireContext())
        binding.popularDailyMatchingRecyclerviewRc.layoutManager = layoutManager
        dailyMatchingListRVAdapter = DailyMatchingListRVAdapter(this)
        binding.popularDailyMatchingRecyclerviewRc.adapter = dailyMatchingListRVAdapter
    }

    override fun onResume() {
        super.onResume()
        pageNum = 0
        loadDailyMatchingList()
        initScrollListener()
    }

    private fun initScrollListener() {
        binding.popularDailyMatchingRecyclerviewRc.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = binding.popularDailyMatchingRecyclerviewRc.layoutManager
                if (hasNextPage()) {
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                    val itemTotalCount =
                        recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

                    // 스크롤이 끝에 도달했는지 확인
                    if (lastVisibleItemPosition == itemTotalCount && isNextPageAvailable) {
                        Log.d("끝도달", "ㅊㅋ")
                        loadMoreDailyMatchingList()
                        setHasNextPage(false)
                    }
                }
            }
        })

    }

    override fun onGetPopularDailyMatchingViewSuccess(popularDailyMatchingListResult: PopularDailyMatchingListResult) {
        var allDailyMatchingListDatas: List<DailyMatching?> = popularDailyMatchingListResult.popularList
        isNextPageAvailable = popularDailyMatchingListResult.isNextPageExists

        if (pageNum == 0) {
            dailyMatchingListRVAdapter.setPagingMessages(allDailyMatchingListDatas)
        } else {
            dailyMatchingListRVAdapter.run {
                setLoadingView(false)
                addPagingMessages(allDailyMatchingListDatas)
            }
        }
        pageNum++
    }

    override fun onGetPopularDailyMatchingViewFailure(code: Int, message: String) {
        Log.d("실패", code.toString() + " : " + message)
    }

    private fun loadDailyMatchingList() {
        val dailyMatchingRequestBody =
            DailyMatchingListRequesetBody(getUserIdx()!!, pageNum, listNum)
        DailyMatchingListService.getPopularDailyMatching(this, dailyMatchingRequestBody)
    }

    private fun loadMoreDailyMatchingList() {
        dailyMatchingListRVAdapter.setLoadingView(true)
        val dailyMatchingRequestBody =
            DailyMatchingListRequesetBody(getUserIdx()!!, pageNum, listNum)
        Handler(Looper.getMainLooper()).postDelayed({
            DailyMatchingListService.getPopularDailyMatching(this, dailyMatchingRequestBody)
        }, 1000)
    }

    private fun hasNextPage(): Boolean {
        return isNextPageAvailable
    }

    private fun setHasNextPage(hasNextPage: Boolean) {
        isNextPageAvailable = hasNextPage
    }


    override fun onItemClicked(boardIdx: Int, recruitmentStatus : String) {
        Log.d("상태", "$boardIdx $recruitmentStatus")
        if (recruitmentStatus.equals("recruiting")) {
            val intent = Intent(activity, DailyMatchingDetail::class.java)
            intent.putExtra("boardIdx", boardIdx)
            startActivity(intent)
        }
    }

}
