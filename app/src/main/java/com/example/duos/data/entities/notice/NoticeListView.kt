package com.example.duos.data.entities.notice

import com.example.duos.data.remote.notice.NoticeGetResponse
import com.example.duos.data.remote.notice.NoticeGetResult

interface NoticeGetListView {
    fun onGetNoticeSuccess(noticeGetResponse: NoticeGetResponse)
    fun onGetNoticeFailure(code : Int, message : String)
}