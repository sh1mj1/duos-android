package com.example.duos.ui.main.mypage.setup

import android.content.Intent
import android.util.Log
import android.widget.CompoundButton
import androidx.core.content.ContextCompat
import com.example.duos.CustomDialog
import com.example.duos.R
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.logout.LogOutService
import com.example.duos.data.remote.logout.LogoutListView
import com.example.duos.data.remote.withdrawal.WithDrawalService
import com.example.duos.data.remote.withdrawal.WithdrawalListView
import com.example.duos.data.remote.withdrawal.WithdrawalResponse
import com.example.duos.databinding.FragmentSetupBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.splash.SplashActivity
import com.example.duos.utils.getUserIdx
import com.example.duos.utils.withdrawalAllData
import android.content.SharedPreferences





class SetupFragment : BaseFragment<FragmentSetupBinding>(FragmentSetupBinding::inflate), WithdrawalListView, LogoutListView {
    val TAG = "SetupFragment"
    val myUserIdx = getUserIdx()!!


    override fun initAfterBinding() {

        // 로그아웃 버튼 클릭 -> 다이얼로그
        setLogOutDialog()

        // 회원탈퇴 버튼 클릭 -> 다이얼로그
        setWithdrawalDialog()

        // 알림 설정 스위치
        initPushNotice()

    }

    // 로그아웃 버튼 클릭 -> 다이얼로그
    private fun setLogOutDialog() {
        val resignBuilder = CustomDialog.Builder(requireContext())// 액티비티에서는 rquireContext() -> context
            .setCommentMessage("로그아웃하시겠습니까?") // Dialog 텍스트 설정하기
            .setRightButton("로그아웃", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {                   // 오른쪽 버튼 클릭시 이벤트 설정하기
                    LogOutService.postLogOut(this@SetupFragment, myUserIdx)
                    dialog.dismiss()
                    // DB의 데이터 삭제
//                    val db = UserDatabase.getInstance(requireContext()) // 룸에 내 idx에 맞는 데이터 있으면 불러오기.
//                    Log.d(TAG, db!!.userDao().getUser(myUserIdx).toString())
//                    db.userDao().clearAll()
//
//                    // sharedPreference의 데이터 삭제
//                    withdrawalAllData()
//                    var testWithdrawal = ""
//                    if(getUserIdx() == 0){
//                        testWithdrawal =  "성공함"
//                    }else {
//                        testWithdrawal = "실패함"
//                    }
//                    Log.d(TAG, testWithdrawal)
//
//                    // 초기화면 SplashActivity로 가기
//                    val intent = Intent(activity, SplashActivity::class.java)
//                    startActivity(intent)
                }
            })
            .setLeftButton("취소", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {                   // 왼쪽 버튼 클릭시 이벤트 설정하기
                    // 바로 dismiss 되야야 함
                    Log.d("CustomDialog in SetupFrag", message)  // 테스트 로그
                    dialog.dismiss()
                }
            })

        binding.btnLogoutTv.setOnClickListener {
            resignBuilder.show()
        }
    }

    // 회원 탈퇴 버튼 클릭 -> 다이얼로그
    private fun setWithdrawalDialog() {
        val resignBuilder = CustomDialog.Builder(requireContext())// 만약 액티비티에서 사용한다면 requireContext() 가 아닌 context를 사용하면 됨.
            .setCommentMessage("회원탈퇴시 기존 데이터는 복구할 수 없습니다.\n 회원 탈퇴 하시겠습니까?") // Dialog 텍스트 설정하기
            .setRightButton("탈퇴", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {                   // 오른쪽 버튼 클릭시 이벤트 설정하기
                    // DB의 데이터 삭제
                    val db = UserDatabase.getInstance(requireContext()) // 룸에 내 idx에 맞는 데이터 있으면 불러오기.
                    Log.d(TAG, db!!.userDao().getUser(myUserIdx).toString())
                    db.userDao().clearAll()
                    
                    // sharedPreference의 데이터 삭제
                    withdrawalAllData()
                    var testWithdrawal = ""
                    if(getUserIdx() == 0){
                        testWithdrawal =  "성공함"
                    }else {
                        testWithdrawal = "실패함"
                    }
                    Log.d(TAG, testWithdrawal)
                    
                    // 초기화면 SplashActivity로 가기
                    val intent = Intent(activity, SplashActivity::class.java)
                    startActivity(intent)

                    // API 호출 -> 서버에 있는 내 데이터 삭제
                    WithDrawalService.withDrawal(this@SetupFragment, myUserIdx)
                    Log.d("CustomDialog in SetupFrag", message)  // 테스트 로그
                    dialog.dismiss()
                }
            })
            .setLeftButton("취소", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {                   // 왼쪽 버튼 클릭시 이벤트 설정하기
                    // 바로 dismiss 되야야 함
                    Log.d("CustomDialog in SetupFrag", message)  // 테스트 로그
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

    override fun onWithdrawalSuccess(withdrawalResponse: WithdrawalResponse) {

        // TODO : Room에 있는 값들과 SharedPreference 모두 삭제하기
        val db = UserDatabase.getInstance(requireContext().applicationContext) // 룸에 내 idx에 맞는 데이터 있으면 불러오기.
        Log.d(TAG, db!!.userDao().getUser(myUserIdx).toString())
        db.userDao().clearAll()

        withdrawalAllData()

        var testWithdrawal = ""
        if(getUserIdx() == 0){
            testWithdrawal =  "성공함"
        }else {
            testWithdrawal = "실패함"
        }
        Log.d(TAG, testWithdrawal)
        //TODO : 초기화면 (SplashActivity로 가기)
        val intent = Intent(activity, SplashActivity::class.java)
        startActivity(intent)

    }

    override fun onWithdrawalFailure(code: Int, message: String) {
        Log.d(TAG, "code: $code , message : $message ")
    }

    override fun onLogOutSuccess() {

        val db = UserDatabase.getInstance(requireContext().applicationContext) // 룸에 내 idx에 맞는 데이터 있으면 불러오기.
        Log.d(TAG, db!!.userDao().getUser(myUserIdx).toString())
        db.userDao().clearAll()

        // sharedPreference의 데이터 삭제
        withdrawalAllData()
        var testWithdrawal = ""
        if(getUserIdx() == 0){
            testWithdrawal =  "성공함"
        }else {
            testWithdrawal = "실패함"
        }
        Log.d(TAG, testWithdrawal)

        // 초기화면 SplashActivity로 가기
        val intent = Intent(activity, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onLogOutFailure(code: Int, message: String) {
        showToast("code : $code, message : $message")
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