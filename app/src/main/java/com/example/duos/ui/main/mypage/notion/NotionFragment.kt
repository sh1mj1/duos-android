package com.example.duos.ui.main.mypage.notion

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.duos.data.entities.notice.NoticeGetListView
import com.example.duos.data.remote.notice.NoticeGetResponse
import com.example.duos.data.remote.notice.NoticeGetResult
import com.example.duos.data.remote.notice.NoticeGetService
import com.example.duos.databinding.FragmentNotionBinding
import com.example.duos.ui.BaseFragment


class NotionFragment : BaseFragment<FragmentNotionBinding>(FragmentNotionBinding::inflate), NoticeGetListView {
    val TAG = "NotionFragment"
    private var notionshown = false
    private val noticeGetResult = ArrayList<NoticeGetResult>()

    override fun initAfterBinding() {
// 서비스 업데이트 공지사항 펼치기/펼치지 않기
        NoticeGetService.onGetNotice(this)


        binding.btnShowServiceUpdateCl.setOnClickListener {
            if (!notionshown) {
                notionshown = !notionshown
                binding.notionInfoTv.visibility = VISIBLE
                binding.btnShowServiceUpdateIv.visibility = GONE
                binding.btnShownServiceUpdateIv.visibility = VISIBLE
            } else {
                notionshown = !notionshown
                binding.notionInfoTv.visibility = GONE
                binding.btnShowServiceUpdateIv.visibility = VISIBLE
                binding.btnShownServiceUpdateIv.visibility = GONE
            }
        }
    }

    override fun onGetNoticeSuccess(noticeGetResponse: NoticeGetResponse) {
        noticeGetResult.clear()
        noticeGetResult.addAll(noticeGetResponse.result)
        val notionIdx = noticeGetResult.size-1         // TODO : notionIdx에 앱에 띄우고 싶은 Idx를 넣으면 된다. 일단 가장 최근

        binding.btnNoticeTv.text = noticeGetResult[notionIdx].title
        binding.noticeOnCreateTv.text = noticeGetResult[notionIdx].createdAt
        binding.notionInfoTv.text = noticeGetResult[notionIdx].content

    }

    override fun onGetNoticeFailure(code: Int, message: String) {
        Log.d(TAG, "onGetNoticeFailure")
    }


}

