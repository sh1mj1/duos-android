package com.example.duos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.duos.ui.main.partnerSearch.PartnerProfileOptionListener
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class PartnerProfileOptionDialog : BottomSheetDialogFragment() {

    private lateinit var mListener : PartnerProfileOptionListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        mListener = requireContext() as PartnerProfileOptionListener
        return inflater.inflate(R.layout.block_report_dialog, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<TextView>(R.id.option_block_tv)?.setOnClickListener {
            // 차단하기
            mListener.onClickBlock()
            this.dismiss()
        }
        view?.findViewById<TextView>(R.id.option_report_tv)?.setOnClickListener {
            // 신고하기
            mListener.onClickReport()
            this.dismiss()
        }
        view?.findViewById<TextView>(R.id.option_cancle_btn)?.setOnClickListener {
            this.dismiss()
        }
    }

}