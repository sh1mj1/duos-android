package com.example.duos.ui.main.mypage.notion

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import com.example.duos.R
import com.example.duos.data.entities.notice.NoticeListView
import com.example.duos.data.remote.notice.*
import com.example.duos.databinding.FragmentNotionBinding
import com.example.duos.ui.BaseFragment


class NotionFragment : BaseFragment<FragmentNotionBinding>(FragmentNotionBinding::inflate),
    NoticeListView {
    val TAG = "NotionFragment"
    private var notionshown = false
    private val noticeGetResult = ArrayList<NoticeGetResult>()
//    lateinit var noticeDeleteReqDta: NoticeDeleteReqDto
//    lateinit var noticePostReqDto: NoticePostReqDto
//    lateinit var noticePatchReqDta: NoticePatchReqDto

    override fun initAfterBinding() {
// 서비스 업데이트 공지사항 펼치기/펼치지 않기

        progressON()
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

        (context as NotionActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            requireActivity().finish()
        }


    }

    override fun onGetNoticeSuccess(noticeGetResponse: NoticeGetResponse) {
        progressOFF()
        noticeGetResult.clear()
        noticeGetResult.addAll(noticeGetResponse.result)
        val notionIdx =
            noticeGetResult.size - 1         // TODO : notionIdx에 앱에 띄우고 싶은 Idx를 넣으면 된다. 일단 가장 최근

        binding.btnNoticeTv.text = noticeGetResult[notionIdx].title
        binding.noticeOnCreateTv.text = noticeGetResult[notionIdx].createdAt
        binding.notionInfoTv.text = noticeGetResult[notionIdx].content

    }

    override fun onGetNoticeFailure(code: Int, message: String) {
        Log.d(TAG, "onGetNoticeFailure")
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
    }



}

