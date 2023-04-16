package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.duos.BottomSheetDialog01
import com.example.duos.BottomSheetDialog02
import com.example.duos.BottomSheetDialog03
import com.example.duos.R
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.data.remote.chat.chat.CreateChatRoomResultData
import com.example.duos.data.remote.dailyMatching.DailyMatchingService
import com.example.duos.databinding.ActivityMyprofileBinding
import com.example.duos.databinding.ActivityPartnerProfileBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.chat.ChattingActivity
import com.example.duos.ui.main.chat.CreateChatRoomView
import com.example.duos.ui.main.mypage.myprofile.frag.PlayerFragment
import com.example.duos.utils.getUserIdx

class PartnerProfileActivity: BaseActivity<ActivityPartnerProfileBinding>(ActivityPartnerProfileBinding::inflate), CreateChatRoomView
//    , PartnerProfileOptionListener
{
    var thisUserIdx = 102
    var targetUserIdx = 76

    override fun initAfterBinding() {
        supportFragmentManager.beginTransaction().replace(R.id.partner_profile_fragment_container_fc, PartnerProfileFragment())
            .commitAllowingStateLoss()

        binding.partnerProfileChattingBtn.setOnClickListener {
            createRoom()
        }

        binding.partnerProfileBackIv.setOnClickListener {
            finish()
        }

        binding.partnerProfileOptionIconIv.setOnClickListener {
            val bottomSheet = BottomSheetDialog02()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    fun createRoom(){
        // val chatRoom = ChatRoom(thisUserIdx, targetUserIdx)
        Log.d("채팅방 생성한 user의 userIdx", thisUserIdx.toString())
        Log.d("채팅방 생성: 상대 user의 userIdx", targetUserIdx.toString())
        ChatService.createChatRoom(this, thisUserIdx, targetUserIdx)
    }

    private fun startChattingActivity(){
        var intent = Intent(this, ChattingActivity::class.java)
        startActivity(intent)
    }

//    override fun onCreateChatRoomLoading() {
//        progressON()
//        Log.d("로딩중","채팅방 생성 api")
//        Handler(Looper.getMainLooper()).postDelayed(Runnable { progressOFF() }, 3500)
//    }

    override fun onCreateChatRoomSuccess(createChatRoomResultData: CreateChatRoomResultData) {
//        TODO("Not yet implemented")
    }

//    override fun onCreateChatRoomSuccess() {
//        startChattingActivity()
//    }

    override fun onCreateChatRoomFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //Toast.makeText(this,"code: $code, message: $message", Toast.LENGTH_LONG).show()
    }

//    // 유저 차단
//    override fun onClickBlock() {
//        DailyMatchingService.dailyMatchingBlock(this, DailyMatchingBlockRequest(getUserIdx()!!, targetUserIdx))
//    }
//
//    // 유저 신고
//    override fun onClickReport() {
//        DailyMatchingService.dailyMatchingReport(this, DailyMatchingReportRequest(getUserIdx()!!, targetUserIdx))
//    }
}