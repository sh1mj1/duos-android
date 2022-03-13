package com.example.duos.ui.main.mypage.myprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.duos.R
import com.example.duos.data.local.UserDatabase
import com.example.duos.databinding.ActivityMyprofileBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.mypage.myprofile.editprofile.EditProfileActivity
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
        val isFromDailyMatching = intent.getBooleanExtra("isFromDailyMatching", false)
        /* AppointmentFragment 에서 넘어왔다면 True else> 기본값 false*/
        val isFromAppointment = intent.getBooleanExtra("isFromAppointment", false)

        val partnerUserIdx = intent.getIntExtra("partnerUserIdx", 0)

        Log.d("PartnerUserIdx", "isFromParterSearch? : ${isFromSearch} isFromDailyMatching? : $isFromDailyMatching  isFromAppointment? : ${isFromAppointment}  partnerUserIdx : ${partnerUserIdx}" )


        // PartnerSearch OR Appointment OR Chatting 에서 호출될 때 항상 다른 PlayerFrag 로감.
        if (isFromSearch or isFromAppointment or isFromDailyMatching) {
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




}

