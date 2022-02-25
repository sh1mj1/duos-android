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
import com.example.duos.utils.deleteSharedPreferenceData
import android.widget.ImageView
import com.example.duos.data.local.ChatDatabase


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

        (context as SetupActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            requireActivity().finish()
        }

    }

    // 로그아웃 버튼 클릭 -> 다이얼로그
    private fun setLogOutDialog() {
        val resignBuilder = CustomDialog.Builder(requireContext())// 액티비티에서는 rquireContext() -> context
            .setCommentMessage("로그아웃하시겠습니까?") // Dialog 텍스트 설정하기
            .setRightButton("로그아웃", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) { // 오른쪽 버튼 클릭시 이벤트 설정하기
                    Log.d("로그아웃버튼", "클릭")
                    LogOutService.postLogOut(this@SetupFragment, myUserIdx)
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

        binding.btnLogoutTv.setOnClickListener {
            Log.d(TAG, "로그아웃 클릭함")
            resignBuilder.show()
        }
    }

    // 회원 탈퇴 버튼 클릭 -> 다이얼로그
    private fun setWithdrawalDialog() {
        val resignBuilder = CustomDialog.Builder(requireContext())// 만약 액티비티에서 사용한다면 requireContext() 가 아닌 context를 사용하면 됨.
            .setCommentMessage("회원탈퇴시 기존 데이터는 복구할 수 없습니다.\n 회원 탈퇴 하시겠습니까?") // Dialog 텍스트 설정하기
            .setRightButton("탈퇴", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {                   // 오른쪽 버튼 클릭시 이벤트 설정하기
                    Log.d(TAG, "탈퇴 버튼 클릭")
                    WithDrawalService.withDrawal(this@SetupFragment, myUserIdx)
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
        // DB의 데이터 삭제
        val userDB = UserDatabase.getInstance(requireContext().applicationContext) // 룸에 내 idx에 맞는 데이터 있으면 불러오기.
        Log.d(TAG, userDB!!.userDao().getUser(myUserIdx).toString())
        userDB.userDao().clearAll()
        Log.d(TAG, "userTable 삭제됨 ${userDB.userDao()}")

        val chatDB = ChatDatabase.getInstance(requireContext(), ChatDatabase.provideGson())!!   //ChatDB
        chatDB.chatRoomDao().clearAll()
        chatDB.chatMessageItemDao().clearAll()

        // sharedPreference의 데이터 삭제
        deleteSharedPreferenceData()
        var testWithdrawal = ""
        if (getUserIdx() == 0) {
            testWithdrawal = "성공함"
        } else {
            testWithdrawal = "실패함"
        }
        Log.d(TAG, testWithdrawal)
        // 초기화면 SplashActivity로 가기
        val intent = Intent(activity, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)

    }

    override fun onWithdrawalFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        Log.d(TAG, "code: $code , message : $message ")
    }

    override fun onLogOutSuccess() {
        val userDB = UserDatabase.getInstance(requireContext().applicationContext) // 룸에 내 idx에 맞는 데이터 있으면 불러오기.
        userDB!!.userDao().clearAll()

        Log.d(TAG, "UserDB : ${userDB.userDao().getUser(myUserIdx)}")
        // sharedPreference의 데이터 삭제
        deleteSharedPreferenceData()
        var testWithdrawal = ""
        if (getUserIdx() == 0) {
            testWithdrawal = "성공함"
        } else {
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
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        Log.d(TAG, "${code}  ${message}")
        //showToast("code : $code, message : $message")
    }

}
