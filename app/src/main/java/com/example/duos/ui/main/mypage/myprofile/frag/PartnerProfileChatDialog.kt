package com.example.duos.ui.main.mypage.myprofile.frag

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Dimension
import com.example.duos.R
import com.example.duos.data.remote.chat.chat.CreateChatRoomResultData
import com.example.duos.databinding.PartnerFilterCustomDialogBinding
import com.example.duos.ui.main.partnerSearch.PartnerFilterDialog

class PartnerProfileChatDialog(context : Context) : Dialog(context) {
    var partnerChatCount : Int = 0

    var leftButtonCallback: PartnerProfileChatDialogCallbackLeft? = null    // 채팅 취소
    var rightButtonCallback: PartnerProfileChatDialogCallbackRight? = null  // 채팅 ㄱㄱ

    var mBinding : PartnerFilterCustomDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(true) // Dialog 바깥 역역 터치 -> Dialog 사라짐.
        mBinding = PartnerFilterCustomDialogBinding.inflate(layoutInflater)
        setContentView(mBinding!!.root)
        window?.setGravity(Gravity.CENTER)
        val width = (context.resources.displayMetrics.widthPixels * 0.91).toInt()   //        기기 가로 너비의 91% 크기로 적용하겠다
        window?.setLayout(width, ViewGroup.LayoutParams.WRAP_CONTENT)
        initView()
    }

    private fun initView() {
        mBinding!!.partnerFilterCountTv.text = partnerChatCount.toString()
        mBinding!!.partnerFilterCountNoticeTextTv.text = "채팅은 하루 5명까지 가능합니다."
        mBinding!!.partnerFilterCountTextTv.text = "남은 채팅 가능 횟수 : "
        mBinding!!.partnerFilterCustomDialogLeftBtn.text = "취소"
        mBinding!!.partnerFilterCustomDialogRightBtn.text = "채팅"

        mBinding!!.partnerFilterCustomDialogLeftBtn.setOnClickListener(View.OnClickListener {
            leftButtonCallback!!.onClick(this)
        })
        mBinding!!.partnerFilterCustomDialogRightBtn.setOnClickListener(View.OnClickListener {
            rightButtonCallback!!.onClick(this)
        })
    }

    class Builder(val context: Context) {
        private var dialog = PartnerProfileChatDialog(context)

        @SuppressLint("ResourceAsColor")
        fun setFailView() : PartnerProfileChatDialog {
            dialog.mBinding!!.partnerFilterCountNoticeTextTv.text = "채팅 가능한 횟수가 모두 소진되었습니다. \n 채팅은 하루 5명까지 가능합니다."
            // 글자 색 바꾸기
            dialog.mBinding!!.partnerFilterCountNoticeTextTv.setTextSize(Dimension.SP, 15F)
            dialog.mBinding!!.underTextVisibleLl.visibility = View.INVISIBLE
            dialog.mBinding!!.partnerFilterCustomDialogRightBtn.visibility = View.GONE
            dialog.mBinding!!.partnerFilterCustomDialogLeftBtn.text = "확인"
            dialog.mBinding!!.partnerFilterCustomDialogLeftBtn.setTextColor(R.color.primary)
            return dialog
        }
        fun setCount(count : Int): Builder {
            dialog.partnerChatCount = count
            return this
        }

        fun setLeftButton(callback: PartnerProfileChatDialogCallbackLeft): Builder {
            dialog.leftButtonCallback = callback
            return this
        }

        fun setRightButton(callback: PartnerProfileChatDialogCallbackRight): Builder {

            dialog.rightButtonCallback = callback
            return this
        }

        fun show(): PartnerProfileChatDialog {
            dialog.show()
            return dialog
        }

    }

    interface PartnerProfileChatDialogCallbackLeft {
        fun onClick(dialog:PartnerProfileChatDialog)

    }
    interface PartnerProfileChatDialogCallbackRight{
        fun onClick(dialog: PartnerProfileChatDialog)
        fun onCreateChatRoomSuccess(createChatRoomResultData: CreateChatRoomResultData)
        fun onCreateChatRoomFailure(code: Int, message: String)
    }



}
