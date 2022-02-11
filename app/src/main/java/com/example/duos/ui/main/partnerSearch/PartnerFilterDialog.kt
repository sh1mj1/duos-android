package com.example.duos.ui.main.partnerSearch

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.example.duos.CustomDialog
import com.example.duos.R
import com.example.duos.databinding.CustomDialogBinding
import com.example.duos.databinding.PartnerFilterCustomDialogBinding

class PartnerFilterDialog (context: Context) : Dialog(context) {
    var partnerFilterCount : String? = ""

    var leftButtonCallback: PartnerFilterDialogCallback? = null
    var rightButtonCallback: PartnerFilterDialogCallback? = null

    var mBinding: PartnerFilterCustomDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(true) // Dialog 바깥 역역 터치 -> Dialog 사라짐.
        mBinding = PartnerFilterCustomDialogBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)

//        Dialog 레이아웃 크기 및 위치 설정  setContentView 실행 후에 적용되어야 제대로 반영됨.
        window?.setGravity(Gravity.CENTER)
        val width = (context.resources.displayMetrics.widthPixels * 0.91).toInt()   //        기기 가로 너비의 91% 크기로 적용하겠다
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)

        initView()

    }

    //    Activity 종료시 Dialog를 종료시킨다.
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        this.dismiss()
    }

    //    Builder를 통해 받은 설정값들을 초기화하는 메서드
    private fun initView() {
        mBinding!!.partnerFilterCountTv.text = partnerFilterCount

        mBinding!!.partnerFilterCustomDialogLeftBtn.setOnClickListener(View.OnClickListener {
            leftButtonCallback!!.onClick(this)
        })
        mBinding!!.partnerFilterCustomDialogRightBtn.setOnClickListener(View.OnClickListener {
            rightButtonCallback!!.onClick(this)
        })

    }

    class Builder(val context: Context) {
        private var dialog = PartnerFilterDialog(context)

        fun setCount(count : String): Builder {
            dialog.partnerFilterCount = count
            return this
        }

        fun setRightButton(callback: PartnerFilterDialogCallback): Builder {
            dialog.rightButtonCallback = callback
            return this
        }

        fun setLeftButton(callback: PartnerFilterDialogCallback): Builder {
            dialog.leftButtonCallback = callback
            return this
        }

        fun show(): PartnerFilterDialog {
            dialog.show()
            return dialog
        }
    }

    interface PartnerFilterDialogCallback {
        fun onClick(dialog: PartnerFilterDialog)

    }
}
