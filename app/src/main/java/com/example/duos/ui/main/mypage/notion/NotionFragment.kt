package com.example.duos.ui.main.mypage.notion

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ImageView
import com.example.duos.R
import com.example.duos.data.entities.notice.NoticeListView
import com.example.duos.data.remote.notice.NoticeGetResponse
import com.example.duos.data.remote.notice.NoticeGetResult
import com.example.duos.data.remote.notice.NoticeGetService
import com.example.duos.databinding.FragmentNotionBinding
import com.example.duos.ui.BaseFragment


class NotionFragment : BaseFragment<FragmentNotionBinding>(FragmentNotionBinding::inflate),
    NoticeListView {
    val TAG = "NotionFragment"
    private var notionShown = false
    private var errorCodeShown = false
    private val noticeGetResult = ArrayList<NoticeGetResult>()
//    lateinit var noticeDeleteReqDta: NoticeDeleteReqDto
//    lateinit var noticePostReqDto: NoticePostReqDto
//    lateinit var noticePatchReqDta: NoticePatchReqDto

    override fun initAfterBinding() {


        progressON()
        NoticeGetService.onGetNotice(this)

        binding.errorCodeOnCreateTv.text = getString(R.string.error_code_on_create)

        // 서비스 업데이트 공지사항 펼치기/펼치지 않기
        initListener()

        (context as NotionActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            requireActivity().finish()
        }


    }

    private fun initListener() {
        binding.btnShowServiceUpdateCl.setOnClickListener {
            if (!notionShown) {
                notionShown = true
                binding.notionInfoTv.visibility = VISIBLE
                binding.btnShowServiceUpdateIv.visibility = GONE
                binding.btnShownServiceUpdateIv.visibility = VISIBLE
            } else {
                notionShown = false
                binding.notionInfoTv.visibility = GONE
                binding.btnShowServiceUpdateIv.visibility = VISIBLE
                binding.btnShownServiceUpdateIv.visibility = GONE
            }
        }

        binding.btnShowErrorCodeCl.setOnClickListener {
            if (!errorCodeShown) {
                errorCodeShown = true
                binding.errorCodeInfoTv.visibility = VISIBLE
                binding.btnShowErrorCodeIv.visibility = GONE
                binding.btnShownErrorCodeIv.visibility = VISIBLE

            } else {
                errorCodeShown = false
                binding.errorCodeInfoTv.visibility = GONE
                binding.btnShowErrorCodeIv.visibility = VISIBLE
                binding.btnShownErrorCodeIv.visibility = GONE
            }
        }
    }

    override fun onGetNoticeSuccess(noticeGetResponse: NoticeGetResponse) {
        progressOFF()
        noticeGetResult.clear()
        noticeGetResult.addAll(noticeGetResponse.result)
        Log.e(TAG, "noticeGetResult: $noticeGetResponse")

        binding.btnNoticeTv.text = getString(R.string.notion_service_update_notion)


//        notionIdx에 앱에 띄우고 싶은 Idx(가장 최근)을 넣으면 된다.
        if (noticeGetResult.size != 0) {
            val notionIdx = noticeGetResult.size - 1
            binding.noticeOnCreateTv.text = noticeGetResult[notionIdx].createdAt
            val notionInfoText =
                noticeGetResult[notionIdx].content + "\n\n" + getString(R.string.notion_service_update_info)
            binding.notionInfoTv.text = notionInfoText
        } else {
            binding.noticeOnCreateTv.text = getString(R.string.notion_service_update_date)
            binding.notionInfoTv.text = getString(R.string.notion_service_update_info)
        }


    }

    override fun onGetNoticeFailure(code: Int, message: String) {
        progressOFF()
        Log.d(TAG, "onGetNoticeFailure")
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
    }


}

