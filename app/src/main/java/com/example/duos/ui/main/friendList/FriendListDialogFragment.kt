package com.example.duos.ui.main.friendList

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import com.example.duos.databinding.FragmentFriendListDialogBinding
import com.example.duos.utils.saveFriendListDialogNotShowing

class FriendListDialogFragment : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = false

    }

    private lateinit var binding: FragmentFriendListDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFriendListDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        binding.friendListDialogCheckBoxCb.setOnCheckedChangeListener { compoundButton, b ->
//            if (b){
//                // 체크되었다면
//                binding.friendListDialogCheckAgainTv.setTextColor(resources.getColor(R.color.primary))
//            } else {
//                // 체크안되었다면
//                binding.friendListDialogCheckAgainTv.setTextColor(resources.getColor(R.color.dark_gray_A))
//            }
//        }

        binding.friendListDialogOkTv.setOnClickListener {
            if (binding.friendListDialogCheckBoxCb.isChecked){
                // 다시 보지 않기 설정 -> sharedpreference 에 저장
                saveFriendListDialogNotShowing(true)
            }
            else{
                saveFriendListDialogNotShowing(false)
            }
            this.dismiss()
        }
    }


    override
    fun onResume() {
        super.onResume()
        val windowManager = requireActivity().getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = size.x
        val deviceHeight = size.y
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }
}