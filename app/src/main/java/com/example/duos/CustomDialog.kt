package com.example.duos

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.example.duos.databinding.CustomDialogBinding


class CustomDialog(context: Context) : Dialog(context) {
    var messageTextView: String? = null
    var leftButtonMsg: String? = null
    var rightButtonMsg: String? = null

    var messageBackGround: Int = R.drawable.ic_rectangler_introduction_on
    var leftBtnBackGround: Int = R.drawable.ic_rectangler_introduction_on
    var rightBtnBackGround: Int = R.drawable.ic_rectangler_introduction_on

    var leftButtonCallback: CustomDialogCallback? = null
    var rightButtonCallback: CustomDialogCallback? = null

    var mBinding: CustomDialogBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setCanceledOnTouchOutside(true) // Dialog 바깥 역역 터치 -> Dialog 사라짐.
        mBinding = CustomDialogBinding.inflate(layoutInflater)
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
        mBinding!!.messageTextView.setText(messageTextView)
        mBinding!!.rightButton.text = rightButtonMsg
        mBinding!!.leftButton.text = leftButtonMsg

        mBinding!!.messageTextView.setBackgroundResource(messageBackGround)
        mBinding!!.rightButton.setBackgroundResource(rightBtnBackGround)
        mBinding!!.leftButton.setBackgroundResource(leftBtnBackGround)

        mBinding!!.leftButton.setOnClickListener(View.OnClickListener {
            leftButtonCallback!!.onClick(this, leftButtonMsg.toString())
        })
        mBinding!!.rightButton.setOnClickListener(View.OnClickListener {
            rightButtonCallback!!.onClick(this, rightButtonMsg.toString())
        })

    }

    class Builder(val context: Context) {
        private var dialog = CustomDialog(context)

        fun setCommentMessage(message: String, background: Int = R.drawable.ic_rectangler_introduction_on): Builder {
            dialog.messageTextView = message
            dialog.messageBackGround = background
            return this
        }

        fun setRightButton(
            msg: String, callback: CustomDialogCallback, background: Int = R.drawable.ic_rectangler_introduction_on): Builder {
            dialog.rightButtonMsg = msg
            dialog.rightBtnBackGround = background
            dialog.rightButtonCallback = callback
            return this
        }

        fun setLeftButton(msg: String, callback: CustomDialogCallback, background: Int = R.drawable.ic_rectangler_introduction_on): Builder {
            dialog.leftButtonMsg = msg
            dialog.leftBtnBackGround = background
            dialog.leftButtonCallback = callback
            return this
        }

        fun show(): CustomDialog {
            dialog.show()
            return dialog
        }
    }

    interface CustomDialogCallback {
        fun onClick(dialog: CustomDialog, message: String)

    }
}
