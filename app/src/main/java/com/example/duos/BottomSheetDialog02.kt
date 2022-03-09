package com.example.duos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.duos.ui.main.dailyMatching.DailyMatchingOptionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog02 : BottomSheetDialogFragment() {

    private lateinit var mListener : DailyMatchingOptionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mListener = requireContext() as DailyMatchingOptionListener
        return inflater.inflate(R.layout.daily_matching_bottom_sheet_dialog_02, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<TextView>(R.id.daily_matching_option_message_tv)?.setOnClickListener {
            // 메세지 보내기
            mListener.onClickChat()
            this.dismiss()
        }
        view?.findViewById<TextView>(R.id.daily_matching_option_02_bottom_btn)?.setOnClickListener {
            this.dismiss()
        }
    }

}