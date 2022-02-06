package com.example.duos.ui.main.mypage.setup

import android.util.Log
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.example.duos.CustomDialog
import com.example.duos.R
import com.example.duos.databinding.FragmentSetupBinding
import com.example.duos.ui.BaseFragment


class SetupFragment : BaseFragment<FragmentSetupBinding>(FragmentSetupBinding::inflate) {

    override fun initAfterBinding() {

        // 로그아웃 버튼 클릭 -> 다이얼로그
        setLogOutDialog()

        // 알림 설정 스위치
        initPushNotice()

    }

    // 로그아웃 버튼 클릭 -> 다이얼로그
    private fun setLogOutDialog() {
        val resignBuilder = CustomDialog.Builder(requireContext())// 만약 액티비티에서 사용한다면 requireContext() 가 아닌 context를 사용하면 됨.
            .setCommentMessage("회원탈퇴시 기존 데이터는 복구할 수 없습니다.\n 회원 탈퇴 하시겠습니까?") // Dialog 텍스트 설정하기
            .setRightButton("탈퇴", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {                   // 오른쪽 버튼 클릭시 이벤트 설정하기
                    //TODO: ("Not yet implemented") API 호출해서 회원 탈퇴시키고, 그에 따른 이벤트처리(앱 초기화면 이동 등)
                    Log.d("CustomDialog in SetupFrag", message.toString())  // 테스트 로그
                    dialog.dismiss()
                }
            })
            .setLeftButton("취소", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {                   // 왼쪽 버튼 클릭시 이벤트 설정하기
                    // 바로 dismiss 되야야 함
                    Log.d("CustomDialog in SetupFrag", message.toString())  // 테스트 로그
                    dialog.dismiss()
                }
            })

        binding.btnDeleteAccountCl.setOnClickListener {
            resignBuilder.show()
        }
    }
    // 알림 설정 스위치

    private fun initPushNotice() {
        binding.notificationSettingSw.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
            if (isChecked) {
                binding.noticeOnoffTv.text = "켜짐"
                binding.noticeOnoffTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.primary
                    )
                )
                //Todo : 알림을 받도록
            } else {
                binding.noticeOnoffTv.text = "꺼짐"
                binding.noticeOnoffTv.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.grey
                    )
                )
                //Todo : 알림을 받지 않도록
            }
        }
    }

}


//class SetupFragment : Fragment() {
//
//    private var _binding: FragmentSetupBinding? = null
//    private val binding get() = _binding
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
//
//        val resignBuilder = CustomDialog.Builder(requireContext())// 만약 액티비티에서 사용한다면 requireContext() 가 아닌 context를 사용하면 됨.
//            .setCommentMessage("회원탈퇴시 기존 데이터는 복구할 수 없습니다.\n 회원 탈퇴 하시겠습니까?") // Dialog 텍스트 설정하기
//            .setRightButton("탈퇴", object : CustomDialog.CustomDialogCallback {
//                override fun onClick(dialog: CustomDialog, message: String) {                   // 오른쪽 버튼 클릭시 이벤트 설정하기
//                    //TODO: ("Not yet implemented") API 호출해서 회원 탈퇴시키고, 그에 따른 이벤트처리(앱 초기화면 이동 등)
//                    Log.d("CustomDialog in SetupFrag", message.toString())  // 테스트 로그
//                    dialog.dismiss()
//                }
//            })
//            .setLeftButton("취소",  object : CustomDialog.CustomDialogCallback {
//                override fun onClick(dialog: CustomDialog, message: String) {                   // 왼쪽 버튼 클릭시 이벤트 설정하기
//                    // 바로 dismiss 되야야 함
//                    Log.d("CustomDialog in SetupFrag", message.toString())  // 테스트 로그
//                    dialog.dismiss()
//                }
//            })
//
//        _binding = FragmentSetupBinding.inflate(inflater, container, false)
//
//        // 탈퇴하기 버튼 클릭시 위에서 정의한 다이얼로그 띄우기
//        _binding!!.btnDeleteAccountCl.setOnClickListener {
//            resignBuilder.show()
//        }
////        알림 설정 스위치
//        _binding!!.notificationSettingSw.setOnCheckedChangeListener { compoundButton: CompoundButton, isChecked: Boolean ->
//            if (isChecked) {
//                _binding!!.noticeOnoffTv.text = "켜짐"
//                _binding!!.noticeOnoffTv.setTextColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.primary
//                    )
//                )
//                //Todo : 알림을 받도록
//            } else {
//                _binding!!.noticeOnoffTv.text = "꺼짐"
//                _binding!!.noticeOnoffTv.setTextColor(
//                    ContextCompat.getColor(
//                        requireContext(),
//                        R.color.grey
//                    )
//                )
//                //Todo : 알림을 받지 않도록
//            }
//        }
//
//
//        return binding!!.root
//    }
//
//
//}