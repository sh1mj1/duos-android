package com.example.duos.data.entities.notice

import com.example.duos.data.remote.notice.*

interface NoticeListView {
    fun onGetNoticeSuccess(noticeGetResponse: NoticeGetResponse)
    fun onGetNoticeFailure(code: Int, message: String)
//
//    fun onDeleteNoticeSuccess(noticeDeleteResponse: NoticeDeleteResponse)
//    fun onDeleteNoticFailure(code: Int, message: String)
//
//    fun onPostNoticeSuccess(noticePostResponse: NoticePostResponse)
//    fun onPostNoticeFailure(code: Int, message: String)
//
//    fun onPatchNoticeSuccess(noticePatchResponse: NoticePatchResponse)
//    fun onPatchNoticeFailure(code: Int, message: String)

}