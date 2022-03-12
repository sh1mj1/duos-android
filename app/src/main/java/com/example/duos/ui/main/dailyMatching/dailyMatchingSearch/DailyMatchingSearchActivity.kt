package com.example.duos.ui.main.dailyMatching.dailyMatchingSearch

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.R
import com.example.duos.data.entities.dailyMatching.SearchHistory
import com.example.duos.data.entities.dailyMatching.SearchHistoryDatabase
import com.example.duos.data.remote.dailyMatching.DailyMatchingListService
import com.example.duos.data.remote.dailyMatching.DailyMatchingSearchResultData
import com.example.duos.data.remote.dailyMatching.SearchResultItem
import com.example.duos.databinding.ActivityDailyMatchingSearchBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.adapter.DailyMatchingSearchRVAdapter
import com.example.duos.ui.main.appointment.DailyMatchingSearchHistoryRVAdapter
import com.example.duos.ui.main.dailyMatching.DailyMatchingDetail
import com.example.duos.ui.main.dailyMatching.DailyMatchingSearchView
import com.example.duos.utils.getUserIdx


class DailyMatchingSearchActivity :
    BaseActivity<ActivityDailyMatchingSearchBinding>(ActivityDailyMatchingSearchBinding::inflate),
    DailyMatchingSearchView {
    val userIdx = getUserIdx()
    val TAG = "DailyMatchingSearchActivity"
    private lateinit var searchHistoryAdapter: DailyMatchingSearchHistoryRVAdapter
    private var layoutManager: RecyclerView.LayoutManager? = null
    lateinit var dailyMatchingSearchSearchRVAdapter: DailyMatchingSearchRVAdapter
    private var dailyMatchingSearchListDatas = ArrayList<SearchResultItem>()
    private lateinit var progressDialog: AppCompatDialog
    private var bindViewHolderCount: Int = 0


    @SuppressLint("NotifyDataSetChanged")
    override fun initAfterBinding() {

        initView()
//         하단 검색
        val allDailyMatchingSearchRV = binding.allDailyMatchingRecyclerviewRc
        allDailyMatchingSearchRV.setHasFixedSize(true)
        allDailyMatchingSearchRV.itemAnimator = DefaultItemAnimator()
        layoutManager = LinearLayoutManager(this)
        allDailyMatchingSearchRV.layoutManager = layoutManager
        dailyMatchingSearchSearchRVAdapter =
            DailyMatchingSearchRVAdapter(dailyMatchingSearchListDatas)
        allDailyMatchingSearchRV.adapter = dailyMatchingSearchSearchRVAdapter

        binding.dailyMatchingSearchEt.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                // TODO : 검색 후 검색 기록 리사이클러뷰 binding.dailyMatchingSearchRecordRv 맨 왼쪽으로
                search(binding.dailyMatchingSearchEt.text.toString())
                searchProgressON()
                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
        // 검색 기록 전체 삭제
        binding.dailyMatchingDeleteSearchRecordTv.setOnClickListener {
            val db = SearchHistoryDatabase.getInstance(this)
            db!!.searchHistoryRoomDao().clearAll()
            searchHistoryAdapter.notifyDataSetChanged()
            binding.dailyMatchingSearchRecordRv.visibility = View.GONE
            binding.dailyMatchingSearchRecordNullTv.visibility = View.VISIBLE
            binding.dailyMatchingSearchResultCountTv.visibility = View.GONE
        }

        binding.dailyMatchingSearchBackIv.setOnClickListener {
            finish()
        }
    }


    private fun search(keyword: String) {
        // 그리고 보이면 안되는 것들 invisible 로 만들기
        binding.dailyMatchingSearchResultCountTv.visibility = View.VISIBLE // 검색결과 갯수
        binding.dailyMatchingSearchRecordNullTv.visibility = View.GONE  // 최근 검색어가 없습니다.
        // 검색한 단어를 RoomDB 에 넣기
        saveSearchKeyword(keyword)
        showResultOfSearch()
        // API 불러오기 화면 표시하기
        DailyMatchingListService.searchDailyMatching(this, userIdx, keyword)
    }

    // 검색 후 키워드 Room DB에 저장
    private fun saveSearchKeyword(keyword: String) {
        val db = SearchHistoryDatabase.getInstance(this)
        db!!.searchHistoryRoomDao().insert(SearchHistory(null, keyword))
    }

    // 최근 검색어 삭제
    private fun deleteSearchKeyword(keyword: String) {
        val db = SearchHistoryDatabase.getInstance(this)
        db!!.searchHistoryRoomDao().delete(keyword)
        showResultOfSearch()
    }


    private fun showResultOfSearch() {
        val db = SearchHistoryDatabase.getInstance(this)
        val keywords = db!!.searchHistoryRoomDao().getAll().reversed()

        binding.dailyMatchingSearchRecordRv.visibility = View.VISIBLE
        searchHistoryAdapter.submitList(keywords.orEmpty())
        binding.dailyMatchingSearchResultCountTv.visibility = View.VISIBLE
        binding.dailyMatchingResultOfSearchFl.visibility = View.VISIBLE
    }


    private fun initHistoryRV() {
        searchHistoryAdapter = DailyMatchingSearchHistoryRVAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        })

        binding.dailyMatchingSearchRecordRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.dailyMatchingSearchRecordRv.adapter = searchHistoryAdapter
    }

    private fun initView() {
        binding.dailyMatchingSearchResultCountTv.visibility = View.GONE // 검색 결과 수 ()
        binding.dailyMatchingResultOfSearchFl.visibility = View.GONE    // 검색 결과 메인 뷰
        binding.dailyMatchingSearchRecentCl.visibility = View.VISIBLE

        // 처음 검색하는 것이면 (DB 가 비어있으면) 처음에는 최근 검색어가 없습니다.
        val db = SearchHistoryDatabase.getInstance(this)
        val mySearchHistory = db!!.searchHistoryRoomDao().getAll()
        Log.d(TAG, "검색어 DB 가 비어있나 $mySearchHistory , isEmpty? : ${mySearchHistory.isEmpty()}")
        initHistoryRV()
        if (mySearchHistory.isEmpty()) {    // 처음 검색
            binding.dailyMatchingSearchRecordRv.visibility = View.GONE
            binding.dailyMatchingSearchRecordNullTv.visibility = View.VISIBLE
        } else {    // 검색한 적 있음
            binding.dailyMatchingSearchRecordRv.visibility = View.VISIBLE
            binding.dailyMatchingSearchRecordNullTv.visibility = View.GONE
            showResultOfSearch()
        }

    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onGetSearchViewSuccess(dailyMatchingSearchResultData: DailyMatchingSearchResultData) {
        Log.d(TAG, "API 호출 성공")
        binding.allDailyMatchingRecyclerviewRc.visibility = View.VISIBLE
        binding.dailyMatchingSearchResultCountTv.visibility = View.VISIBLE // 검색결과 갯수
        binding.dailyMatchingSearchRecentCl.visibility = View.GONE

        binding.dailyMatchingSearchResultCountTv.text =
            "검색 결과 (${dailyMatchingSearchResultData.resultSize})"
        binding.dailyMatchingSearchRecentCl.visibility = View.GONE // 최근 검색어, 전체삭제
        binding.dailyMatchingResultOfSearchFl.visibility = View.VISIBLE
        dailyMatchingSearchListDatas.clear()
        dailyMatchingSearchListDatas.addAll(dailyMatchingSearchResultData.searchResult)
        Log.d(TAG, "하단 결과 뷰 addAll 결과 $dailyMatchingSearchListDatas")

        dailyMatchingSearchSearchRVAdapter.clickSearchResultListener(object :
            DailyMatchingSearchRVAdapter.SearchResultItemClickListener {
            override fun onItemClick(searchResultItem: SearchResultItem) {
                //  해당 게시글로 이동  // 게시글Idx 넘겨주기
                Log.d(TAG, "아이템 클릭 : boardIdx : ${searchResultItem.boardIdx}")
                val intent =
                    Intent(this@DailyMatchingSearchActivity, DailyMatchingDetail::class.java)
                intent.apply { putExtra("boardIdx", searchResultItem.boardIdx) }
                startActivity(intent)
            }
        })
        Handler().postDelayed({
            searchProgressOFF()
        }, (40 * dailyMatchingSearchSearchRVAdapter.itemCount).toLong())

//        searchProgressOFF()

//        Log.d(TAG, "문제의 부분 : bindViewHolderCount : $bindViewHolderCount")
//        if (bindViewHolderCount <= dailyMatchingSearchSearchRVAdapter.itemCount-5) {
//            dailyMatchingSearchSearchRVAdapter.lastBindListener(object :
//                DailyMatchingSearchRVAdapter.BindLastViewHolderListener {
//                override fun onLastBind() {
//                    Log.d(TAG, "모든 viewBindHolder 완료 -> searchProgressOFF")
//                    searchProgressOFF()
//                    bindViewHolderCount = dailyMatchingSearchSearchRVAdapter.itemCount
//                }
//            })
//        } else {
//            searchProgressOFF()
//
//        }

    }

    override fun onGetSearchViewFailure(code: Int, message: String) {
        searchProgressOFF()
        showToast(message)
        binding.allDailyMatchingRecyclerviewRc.visibility = View.GONE
        binding.dailyMatchingSearchResultCountTv.visibility = View.GONE // 검색결과 갯수
        binding.dailyMatchingSearchRecentCl.visibility = View.VISIBLE

    }


    private fun searchProgressON() {
        Log.d(TAG, "searchProgressOn : ")
        progressDialog = AppCompatDialog(this)
        progressDialog.apply {
            setCanceledOnTouchOutside(false)
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            setContentView(R.layout.progress_loading)
            show()
        }
        val img_loading_framge = progressDialog.findViewById<ImageView>(R.id.iv_frame_loading)
        val frameAnimation = img_loading_framge?.background as AnimationDrawable
        img_loading_framge.post(Runnable { frameAnimation.start() })
    }

    private fun searchProgressOFF() {
        Log.d(TAG, "searchProgressOFF : ")
        if (progressDialog != null && progressDialog.isShowing) {
            progressDialog.dismiss()
        }
    }

}

