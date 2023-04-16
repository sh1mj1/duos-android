package com.example.duos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.duos.ui.main.dailyMatching.DailyMatchingOptionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog03  : BottomSheetDialogFragment() {

    private lateinit var mListener : DailyMatchingOptionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mListener = requireContext() as DailyMatchingOptionListener
        return inflater.inflate(R.layout.daily_matching_bottom_sheet_dialog_03, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<TextView>(R.id.daily_matching_option_03_edit_tv)?.setOnClickListener {
            // 수정하기
            mListener.onClickEdit()
            this.dismiss()
        }

        view?.findViewById<TextView>(R.id.daily_matching_option_03_delete_tv)?.setOnClickListener {
            // 삭제하기
            mListener.onClickDelete()
            this.dismiss()
        }
        view?.findViewById<TextView>(R.id.option_cancle_btn)?.setOnClickListener {
            this.dismiss()
        }
    }

}