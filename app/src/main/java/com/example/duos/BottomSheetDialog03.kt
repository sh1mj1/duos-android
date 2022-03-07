package com.example.duos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class BottomSheetDialog03  : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        return inflater.inflate(R.layout.daily_matching_bottom_sheet_dialog_03, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        view?.findViewById<TextView>(R.id.daily_matching_option_03_edit_tv)?.setOnClickListener {
            // 수정하기
        }

        view?.findViewById<TextView>(R.id.daily_matching_option_03_delete_tv)?.setOnClickListener {
            // 삭제하기
        }
    }

}