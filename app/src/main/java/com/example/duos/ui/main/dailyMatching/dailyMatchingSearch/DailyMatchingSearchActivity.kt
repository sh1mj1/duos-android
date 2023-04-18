package com.example.duos.ui.main.dailyMatching.dailyMatchingSearch

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.AdapterDataObserver
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
import com.google.android.material.textfield.TextInputEditText


class DailyMatchingSearchActivity :
    BaseActivity<ActivityDailyMatchingSearchBinding>(ActivityDailyMatchingSearchBinding::inflate),
    DailyMatchingSearchView {
    val userIdx = getUserIdx()
    val TAG = "DailyMatchingSearchActivity"
    private lateinit var searchHistoryAdapter: DailyMatchingSearchHistoryRVAdapter
    private var dailyMatchingSearchListDatas = ArrayList<SearchResultItem>()
    private var isNewSearch = true

    lateinit var historyRv: RecyclerView

    lateinit var db: SearchHistoryDatabase

    @SuppressLint("NotifyDataSetChanged")
    override fun initAfterBinding() {
        db = SearchHistoryDatabase.getInstance(this)!!
        val searchEt = binding.dailyMatchingSearchEt
        val imm: InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        historyRv = binding.dailyMatchingSearchRecordRv

        initView()

        searchEditTextListener(imm, searchEt)

        // 검색 기록 전체 삭제
        deleteAllHistoryListener()

        binding.dailyMatchingSearchBackIv.setOnClickListener {
            finish()
        }
    }

    private fun searchEditTextListener(
        imm: InputMethodManager,
        searchEt: TextInputEditText
    ) {
        binding.dailyMatchingSearchEt.setOnClickListener {
            imm.showSoftInput(searchEt, 0)
        }

        binding.dailyMatchingSearchEt.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                // TODO : 검색 후 검색 기록 리사이클러뷰 binding.dailyMatchingSearchRecordRv 맨 왼쪽으로
                imm.hideSoftInputFromWindow(searchEt.windowToken, 0)
                progressON()
                search(binding.dailyMatchingSearchEt.text.toString())

                return@setOnKeyListener true
            } else {
                return@setOnKeyListener false
            }
        }
    }

    private fun deleteAllHistoryListener() {
        binding.dailyMatchingDeleteSearchRecordTv.setOnClickListener {
            db.searchHistoryRoomDao().clearAll()
            Log.e(TAG, "전체 삭제 후 getAll ${db.searchHistoryRoomDao().getAll()}")
            historyRv.visibility = View.GONE
            binding.dailyMatchingSearchRecordNullTv.visibility = View.VISIBLE
            binding.dailyMatchingSearchResultCountTv.visibility = View.GONE
            binding.dailyMatchingSearchRecentCl.visibility = View.VISIBLE

        }
    }

    // 검색 후 키워드 Room DB에 저장
    private fun saveSearchKeyword(keyword: String) {
        val searchedKeywordList = db.searchHistoryRoomDao().searchKeyword(keyword)

        Log.e(TAG, "searchedKeywordList: $searchedKeywordList")
        searchedKeywordList.listIterator().forEach {
            if (it.keyword == keyword) {
                isNewSearch = false
                return@forEach
            } else {
                isNewSearch = true
            }
            return@forEach
        }
        if (isNewSearch) {
            db.searchHistoryRoomDao().insert(SearchHistory(null, keyword))
        }

        val keywords = db.searchHistoryRoomDao().getAll()
        Log.e(TAG, "showResultOfSearch - $keywords")
        searchHistoryAdapter.submitList(keywords)
        historyRv.visibility = View.VISIBLE
        binding.dailyMatchingSearchResultCountTv.visibility = View.VISIBLE
        binding.dailyMatchingResultOfSearchFl.visibility = View.VISIBLE
    }

    private fun search(keyword: String) {
        // 검색한 단어를 RoomDB 에 넣기
        saveSearchKeyword(keyword)

        // 그리고 보이면 안되는 것들 invisible 로 만들기
        binding.dailyMatchingSearchResultCountTv.visibility = View.VISIBLE // 검색결과 갯수
        binding.dailyMatchingSearchRecordNullTv.visibility = View.GONE  // 최근 검색어가 없습니다.

        // API 불러오기 화면 표시하기
        DailyMatchingListService.searchDailyMatching(this, userIdx, keyword)
    }


    // 최근 검색어 삭제
    private fun deleteSearchKeyword(keyword: String) {
        db.searchHistoryRoomDao().delete(keyword)
        showResultOfSearch()
    }

    private fun showResultOfSearch() {
        val keywords = db.searchHistoryRoomDao().getAll()
        Log.e(TAG, "showResultOfSearch - $keywords")
        searchHistoryAdapter.submitList(keywords)
        historyRv.visibility = View.VISIBLE
        binding.dailyMatchingSearchResultCountTv.visibility = View.VISIBLE
        binding.dailyMatchingResultOfSearchFl.visibility = View.VISIBLE
    }


    // 선택한 아이템으로 검색
    private fun searchHistoryItem(keyword: String) {
        progressON()
        Log.d(TAG, "searchHistoryItem clicked - 선택한 아이템으로 검색")
        binding.dailyMatchingSearchEt.setText(keyword)
        search(keyword)

    }


    private fun initHistoryRV() {
        searchHistoryAdapter = DailyMatchingSearchHistoryRVAdapter(
            historyDeleteClickListener = { deleteSearchKeyword(it) },
            historyItemClickListener = { searchHistoryItem(it) }
        )

        searchHistoryAdapter.registerAdapterDataObserver(object : AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                historyRv.layoutManager!!.smoothScrollToPosition(historyRv, null, searchHistoryAdapter.itemCount)
            }
        })
        historyRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true)

        historyRv.adapter = searchHistoryAdapter

    }

    private fun initView() {

        binding.dailyMatchingSearchResultCountTv.visibility = View.GONE // 검색 결과 수 ()
        binding.dailyMatchingResultOfSearchFl.visibility = View.GONE    // 검색 결과 메인 뷰
        binding.dailyMatchingSearchRecentCl.visibility = View.VISIBLE

        // 처음 검색하는 것이면 (DB 가 비어있으면) 처음에는 최근 검색어가 없습니다.
        val mySearchHistory = db.searchHistoryRoomDao().getAll()
        Log.d(TAG, "검색어 DB 가 비어있나 $mySearchHistory , isEmpty? : ${mySearchHistory.isEmpty()}")

        initHistoryRV()

        if (mySearchHistory.isEmpty()) {    // 처음 검색
            historyRv.visibility = View.GONE
            binding.dailyMatchingSearchRecordNullTv.visibility = View.VISIBLE
        } else {    // 검색한 적 있음
            historyRv.visibility = View.VISIBLE
            binding.dailyMatchingSearchRecordNullTv.visibility = View.GONE
            showResultOfSearch()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onGetSearchViewSuccess(dailyMatchingSearchResultData: DailyMatchingSearchResultData) {
        Log.d(TAG, "API 호출 성공")

        val allDailyMatchingSearchRV = DailyMatchingSearchRVAdapter(dailyMatchingSearchListDatas)
        val recyclerView = binding.allDailyMatchingRecyclerviewRc
        recyclerView.adapter = allDailyMatchingSearchRV
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        binding.dailyMatchingSearchResultCountTv.text = "검색 결과 (${dailyMatchingSearchResultData.resultSize})"
        binding.dailyMatchingResultOfSearchFl.visibility = View.VISIBLE

        dailyMatchingSearchListDatas.clear()
        dailyMatchingSearchListDatas.addAll(dailyMatchingSearchResultData.searchResult)
        Log.d(TAG, "하단 결과 뷰 addAll 결과 $dailyMatchingSearchListDatas")

        binding.allDailyMatchingRecyclerviewRc.visibility = View.VISIBLE
        binding.dailyMatchingSearchResultCountTv.visibility = View.VISIBLE // 검색결과 갯수
        binding.dailyMatchingSearchRecentCl.visibility = View.GONE


        progressOFF()

        allDailyMatchingSearchRV.clickSearchResultListener(object :
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

    }

    override fun onGetSearchViewFailure(code: Int, message: String) {
        showToast(message)
        binding.allDailyMatchingRecyclerviewRc.visibility = View.VISIBLE
        binding.dailyMatchingSearchResultCountTv.visibility = View.GONE // 검색결과 갯수
        binding.dailyMatchingSearchRecentCl.visibility = View.VISIBLE
        binding.dailyMatchingResultOfSearchFl.visibility = View.GONE

        progressOFF()

    }

}

