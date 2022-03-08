package com.example.duos.ui.main.dailyMatching

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.data.entities.dailyMatching.DailyMatching
import com.example.duos.data.remote.dailyMatching.DailyMatchingListRequesetBody
import com.example.duos.data.remote.dailyMatching.AllDailyMatchingListResult
import com.example.duos.data.remote.dailyMatching.DailyMatchingListService
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity
import com.example.duos.utils.getUserIdx
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.databinding.FragmentAllDailyMatchingFragmentBinding


class AllDailyMatchingFragment : BaseFragment<FragmentAllDailyMatchingFragmentBinding>
(FragmentAllDailyMatchingFragmentBinding::inflate), AllDailyMatchingView,
    DailyMatchingRVAdapter.OnItemClickListener {

    var pageNum : Int = 0
    val listNum : Int = 30
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var mContext: MainActivity
    private var isNextPageAvailable = false // 다음 페이지 유무
    lateinit var dailyMatchingRVAdapter : DailyMatchingRVAdapter


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mContext = context
        }
    }

    override fun initAfterBinding() {
        binding.allDailyMatchingRecyclerviewRc.setHasFixedSize(true)
        binding.allDailyMatchingRecyclerviewRc.itemAnimator = DefaultItemAnimator()
        layoutManager = LinearLayoutManager(requireContext())
        binding.allDailyMatchingRecyclerviewRc.layoutManager = layoutManager
        dailyMatchingRVAdapter = DailyMatchingRVAdapter(this)
        binding.allDailyMatchingRecyclerviewRc.adapter = dailyMatchingRVAdapter
    }

    override fun onResume() {
        super.onResume()
        pageNum = 0
        loadDailyMatchingList()
        initScrollListener()
    }

    private fun initScrollListener() {
       binding.allDailyMatchingRecyclerviewRc.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                layoutManager = binding.allDailyMatchingRecyclerviewRc.layoutManager
                if(hasNextPage()){
                    val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition() // 화면에 보이는 마지막 아이템의 position
                    val itemTotalCount = recyclerView.adapter!!.itemCount - 1 // 어댑터에 등록된 아이템의 총 개수 -1

                    // 스크롤이 끝에 도달했는지 확인
                    if (lastVisibleItemPosition == itemTotalCount && isNextPageAvailable)  {
                        Log.d("끝도달","ㅊㅋ")
                        loadMoreDailyMatchingList()
                        setHasNextPage(false)
                    }
                }
            }
       })

    }

    private fun loadDailyMatchingList(){
        val dailyMatchingRequestBody = DailyMatchingListRequesetBody(getUserIdx()!!, pageNum, listNum)
        DailyMatchingListService.getAllDailyMatching(this, dailyMatchingRequestBody)
    }

    private fun loadMoreDailyMatchingList(){
        dailyMatchingRVAdapter.setLoadingView(true)
        val dailyMatchingRequestBody = DailyMatchingListRequesetBody(getUserIdx()!!, pageNum, listNum)
        Handler(Looper.getMainLooper()).postDelayed({
            DailyMatchingListService.getAllDailyMatching(this, dailyMatchingRequestBody)
        }, 1000)
    }

    override fun onGetAllDailyMatchingViewSuccess(allDailyMatchingListResult: AllDailyMatchingListResult) {
        var allDailyMatchingListDatas : List<DailyMatching?> = allDailyMatchingListResult.dailyMatching
        isNextPageAvailable = allDailyMatchingListResult.isNextPageExists

        if (pageNum == 0){
            dailyMatchingRVAdapter.setPagingMessages(allDailyMatchingListDatas)
        }
        else {
            dailyMatchingRVAdapter.run {
                setLoadingView(false)
                addPagingMessages(allDailyMatchingListDatas)
            }
        }
        pageNum++
    }

    override fun onGetAllDailyMatchingViewFailure(code: Int, message: String) {
        Log.d("실패", code.toString() + " : " + message)
    }

    private fun hasNextPage(): Boolean {
        return isNextPageAvailable
    }

    private fun setHasNextPage(hasNextPage: Boolean) {
        isNextPageAvailable = hasNextPage
    }

    override fun onItemClicked(boardIdx: Int) {
        Log.d("idx",boardIdx.toString())
    }
}