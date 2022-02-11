package com.example.duos.ui.main.mypage.notion

import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
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

        NoticeGetService.onGetNotice(this)
//        noticeDeleteReqDta = NoticeDeleteReqDto(10)
//        noticePostReqDto =
//            NoticePostReqDto("신규공지사항 입니다_TEST_0210_1", "신규 공지사항 내용입니다_TEST_0210_1")
//        noticePatchReqDta =
//            NoticePatchReqDto(10, "수정된 공지사항입니다_TEST_0210_1", "수정된 공지사항입니다_TEST_0210_1")
//
//        NoticeDeleteService.onDeleteNotice(this, 11)
//        NoticePostService.onPostNotice(
//            this,
//            noticePostReqDto.title, noticePostReqDto.body
//        )
//        NoticePatchService.onPatchNotice(
//            this,
//            noticePatchReqDta
//        )


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
        val notionIdx =
            noticeGetResult.size - 1         // TODO : notionIdx에 앱에 띄우고 싶은 Idx를 넣으면 된다. 일단 가장 최근

        binding.btnNoticeTv.text = noticeGetResult[notionIdx].title
        binding.noticeOnCreateTv.text = noticeGetResult[notionIdx].createdAt
        binding.notionInfoTv.text = noticeGetResult[notionIdx].content

    }

    override fun onGetNoticeFailure(code: Int, message: String) {
        Log.d(TAG, "onGetNoticeFailure")
    }


//    override fun onDeleteNoticeSuccess(noticeDeleteResponse: NoticeDeleteResponse) {
//        Log.d(TAG, "onDeleteNoticeSuccess : $noticeDeleteReqDta")
//    }
//
//    override fun onDeleteNoticFailure(code: Int, message: String) {
//        Log.d(TAG, "onDeleteNoticFailure")
//    }
//
//
//    override fun onPostNoticeSuccess(noticePostResponse: NoticePostResponse) {
//        Log.d(TAG, "onPostNoticeSuccess : $noticePostReqDto")
//    }
//
//    override fun onPostNoticeFailure(code: Int, message: String) {
//        Log.d(TAG, "onPostNoticeFailure")
//    }
//
//
//    override fun onPatchNoticeSuccess(noticePatchResponse: NoticePatchResponse) {
//        Log.d(TAG, "onPatchNoticeSuccess : $noticePatchReqDta")
//    }
//
//    override fun onPatchNoticeFailure(code: Int, message: String) {
//        Log.d(TAG, "onPatchNoticeFailure")
//    }


}

