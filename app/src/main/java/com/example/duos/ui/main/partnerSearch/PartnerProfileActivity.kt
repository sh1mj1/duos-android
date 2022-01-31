package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.example.duos.R
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.databinding.ActivityPartnerProfileBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.chat.ChattingActivity
import com.example.duos.ui.main.chat.CreateChatRoomView

class PartnerProfileActivity: BaseActivity<ActivityPartnerProfileBinding>(ActivityPartnerProfileBinding::inflate), CreateChatRoomView{
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

    fun startLoadingProgress(){
        Log.d("로딩중","채팅방 생성 api")
        Handler(Looper.getMainLooper()).postDelayed(Runnable { progressOFF() }, 3500)
    }

    override fun onCreateChatRoomLoading() {
        startLoadingProgress()
    }

    override fun onCreateChatRoomSuccess() {
        startChattingActivity()
    }

    override fun onCreateChatRoomFailure(code: Int, message: String) {
        Toast.makeText(this,"code: $code, message: $message", Toast.LENGTH_LONG)
    }


}