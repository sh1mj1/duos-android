package com.example.duos.ui.main.mypage.myprofile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.duos.R
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.databinding.ActivityMyprofileBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.chat.ChattingActivity
import com.example.duos.ui.main.chat.CreateChatRoomView
import com.example.duos.ui.main.mypage.myprofile.editprofile.EditProfileActivity
import com.example.duos.ui.main.mypage.myprofile.frag.EditProfileFragment
import com.example.duos.ui.main.mypage.myprofile.frag.MyProfileFragment
import com.example.duos.ui.main.mypage.myprofile.frag.PlayerFragment
import com.example.duos.utils.getUserIdx

class MyProfileActivity : BaseActivity<ActivityMyprofileBinding>(ActivityMyprofileBinding::inflate)/*,
    CreateChatRoomView*/ {
    var thisUserIdx = 102
    var targetUserIdx = 2
    val userIdx = getUserIdx()!!

    override fun initAfterBinding() {
        val db = UserDatabase.getInstance(applicationContext)
        val myProfileDB = db!!.userDao().getUser(userIdx)

        /* FromPartnerSearchFrag ? True else> 기본값 false*/
        val isFromSearch = intent.getBooleanExtra("isFromSearch", false)
        /* AppointmentFragment 에서 넘어왔다면 True else> 기본값 false*/
        val isFromAppointment = intent.getBooleanExtra("isFromAppointment", false)

        val partnerUserIdx = intent.getIntExtra("partnerUserIdx", 0)
        /*TODO :위 thisIdx는 PartnerSearchFragment 에서 혹은 LastAppointmentFragment 에서
            아이템 클릭시 해당 회원의 고유 인덱스 값이 들어가야 해. 0이 default지만 할당될 일 없음*/
        Log.d("PartnerUserIdx", "isFromParterSearch? : ${isFromSearch}  isFromAppointment? : ${isFromAppointment}  partnerUserIdx : ${partnerUserIdx}" )


        // PartnerSearch OR Appointment OR Chatting 에서 호출될 때 항상 다른 PlayerFrag 로감.
        if (isFromSearch or isFromAppointment) {
            goToPlayerProfile(partnerUserIdx)
        } else {    /* 그게 아니라면 항상 나의 프로필 Frag 가 되고 이때 backStack 이 없음.*/
            supportFragmentManager.beginTransaction()
                .replace(R.id.my_profile_into_fragment_container_fc, MyProfileFragment())
                .commitAllowingStateLoss()
            binding.editMyProfileTv.visibility = View.VISIBLE
            binding.profileBottomChatBtnCl.visibility = View.GONE
            binding.topMyProfileTv.text = "나의 프로필"
        }

//        }
        binding.editMyProfileTv.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java)
            startActivity(intent)
        }

//        binding.partnerProfileChattingBtn.setOnClickListener {
//            createRoom()
//        }

        binding.topLeftArrowIv.setOnClickListener {
            finish()
        }
    }

    private fun goToPlayerProfile(partnerUserIdx: Int) {
        binding.editMyProfileTv.visibility = View.GONE
        binding.profileBottomChatBtnCl.visibility = View.VISIBLE
        binding.topMyProfileTv.text = "프로필"

        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putInt("partnerUserIdx", partnerUserIdx)   // bundle로 PlayerFragment 에
        playerFragment.arguments = bundle                  // PartnerSearchFrag 에서 넘어온 thisIdx 넘겨줌
        supportFragmentManager.beginTransaction()
            .replace(R.id.my_profile_into_fragment_container_fc, playerFragment)
            .commitAllowingStateLoss()
    }


//    /*  아래 희주님이 작성하신 코드                 */
//    fun createRoom() {
//        // val chatRoom = ChatRoom(thisUserIdx, targetUserIdx)
//        Log.d("채팅방 생성한 user의 userIdx", userIdx.toString())
//        Log.d("채팅방 생성: 상대 user의 userIdx", targetUserIdx.toString())
//        ChatService.createChatRoom(this, userIdx, targetUserIdx)
//
//    }
//
//    private fun startChattingActivity() {
//        val intent = Intent(this, ChattingActivity::class.java)
////        intent.apply {
////            putExtra()
////        }
//        startActivity(intent)
//    }
//
//    fun startLoadingProgress() {
//        Log.d("로딩중", "채팅방 생성 api")
//        Handler(Looper.getMainLooper()).postDelayed(Runnable { progressOFF() }, 3500)
//    }
//
//    override fun onCreateChatRoomLoading() {
//        startLoadingProgress()
//    }
//
//    override fun onCreateChatRoomSuccess() {
//        startChattingActivity()
//    }
//
//    override fun onCreateChatRoomFailure(code: Int, message: String) {
////        Toast.makeText(this, "code: $code, message: $message", Toast.LENGTH_LONG).show()
//    }

}

