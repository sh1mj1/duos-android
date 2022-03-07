package com.example.duos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.duos.data.remote.dailyMatching.DailyMatchingService
import com.example.duos.ui.main.dailyMatching.DailyMatchingEndView
import com.example.duos.ui.main.dailyMatching.DailyMatchingOptionListener
import com.example.duos.utils.getUserIdx
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog01(val boardIdx : Int) : BottomSheetDialogFragment() {

    private lateinit var mListener : DailyMatchingOptionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mListener = requireContext() as DailyMatchingOptionListener
        return inflater.inflate(R.layout.daily_mathching_bottom_sheet_dialog_01, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        view?.findViewById<TextView>(R.id.daily_matching_option_edit_tv)?.setOnClickListener {
            // 수정하기
        }

        view?.findViewById<TextView>(R.id.daily_matching_option_delete_tv)?.setOnClickListener {
            // 삭제하기
            mListener.onClickDelete()
        }

        view?.findViewById<TextView>(R.id.daily_matching_option_end_tv)?.setOnClickListener {
            // 모집마감
            mListener.onClickEnd()

        }}
}