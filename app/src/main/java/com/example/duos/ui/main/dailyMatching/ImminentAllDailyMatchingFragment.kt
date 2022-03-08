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
import com.example.duos.data.remote.dailyMatching.ImminentDailyMatchingListResult
import com.example.duos.databinding.FragmentImminentDailyMatchingBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity
import com.example.duos.utils.getUserIdx

class ImminentAllDailyMatchingFragment : BaseFragment<FragmentImminentDailyMatchingBinding>
    (FragmentImminentDailyMatchingBinding::inflate), ImminentDailyMatchingView,
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

        binding.imminentDailyMatchingRecyclerviewRc.setHasFixedSize(true)
        binding.imminentDailyMatchingRecyclerviewRc.itemAnimator = DefaultItemAnimator()
        layoutManager = LinearLayoutManager(requireContext())
        binding.imminentDailyMatchingRecyclerviewRc.layoutManager = layoutManager
        dailyMatchingListRVAdapter = DailyMatchingListRVAdapter(this)
        binding.imminentDailyMatchingRecyclerviewRc.adapter = dailyMatchingListRVAdapter
    }

    override fun onResume() {
        super.onResume()

        pageNum = 0
        loadDailyMatchingList()
        initScrollListener()
    }

    private fun initScrollListener() {
        binding.imminentDailyMatchingRecyclerviewRc.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = binding.imminentDailyMatchingRecyclerviewRc.layoutManager
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


    override fun onGetImminentDailyMatchingViewSuccess(imminentDailyMatchingListResult: ImminentDailyMatchingListResult) {
        var imminentDailyMatchingListDatas: List<DailyMatching?> = imminentDailyMatchingListResult.aboutToCloseList
        isNextPageAvailable = imminentDailyMatchingListResult.isNextPageExists

        if (pageNum == 0) {
            dailyMatchingListRVAdapter.setPagingMessages(imminentDailyMatchingListDatas)
        } else {
            dailyMatchingListRVAdapter.run {
                setLoadingView(false)
                addPagingMessages(imminentDailyMatchingListDatas)
            }
        }
        pageNum++
    }

    override fun onGetImminentDailyMatchingViewFailure(code: Int, message: String) {
        Log.d("실패", code.toString() + " : " + message)
    }

    private fun loadDailyMatchingList() {
        val dailyMatchingRequestBody =
            DailyMatchingListRequesetBody(getUserIdx()!!, pageNum, listNum)
        DailyMatchingListService.getImminentDailyMatching(this, dailyMatchingRequestBody)
    }

    private fun loadMoreDailyMatchingList() {
        dailyMatchingListRVAdapter.setLoadingView(true)
        val dailyMatchingRequestBody =
            DailyMatchingListRequesetBody(getUserIdx()!!, pageNum, listNum)
        Handler(Looper.getMainLooper()).postDelayed({
            DailyMatchingListService.getImminentDailyMatching(this, dailyMatchingRequestBody)
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
