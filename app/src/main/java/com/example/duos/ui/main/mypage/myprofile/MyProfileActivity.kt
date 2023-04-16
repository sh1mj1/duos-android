package com.example.duos.ui.main.mypage.myprofile

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.duos.PartnerProfileOptionDialog
import com.example.duos.R
import com.example.duos.data.entities.block_report.BlockRequest
import com.example.duos.data.entities.block_report.ReportRequest
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.partnerProfile.PartnerProfileService
import com.example.duos.databinding.ActivityMyprofileBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.mypage.myprofile.editprofile.EditProfileActivity
import com.example.duos.ui.main.mypage.myprofile.frag.MyProfileFragment
import com.example.duos.ui.main.mypage.myprofile.frag.PlayerFragment
import com.example.duos.ui.main.partnerSearch.PartnerBlockView
import com.example.duos.ui.main.partnerSearch.PartnerProfileOptionListener
import com.example.duos.ui.main.partnerSearch.PartnerReportView
import com.example.duos.utils.getUserIdx

class MyProfileActivity : BaseActivity<ActivityMyprofileBinding>(
    ActivityMyprofileBinding::inflate
), PartnerProfileOptionListener,
    PartnerBlockView,
    PartnerReportView
/*,
    CreateChatRoomView*/ {
    var thisUserIdx = 102
    var targetUserIdx = 2
    var partnerUserIdx = 2
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
        binding.partnerProfileOptionIconIv.visibility = View.VISIBLE
        binding.profileBottomChatBtnCl.visibility = View.VISIBLE
        binding.topMyProfileTv.text = "프로필"

        this.partnerUserIdx = partnerUserIdx

        val playerFragment = PlayerFragment()
        val bundle = Bundle()
        bundle.putInt("partnerUserIdx", partnerUserIdx)   // bundle로 PlayerFragment 에
        playerFragment.arguments = bundle                  // PartnerSearchFrag 에서 넘어온 thisIdx 넘겨줌
        supportFragmentManager.beginTransaction()
            .replace(R.id.my_profile_into_fragment_container_fc, playerFragment)
            .commitAllowingStateLoss()

        binding.partnerProfileOptionIconIv.setOnClickListener {
            val bottomSheet = PartnerProfileOptionDialog()
            bottomSheet.show(supportFragmentManager, bottomSheet.tag)
        }
    }

    // 유저 차단
    override fun onClickBlock() {
        PartnerProfileService.partnerBlock(this, BlockRequest(getUserIdx()!!, partnerUserIdx))
    }

    // 유저 신고
    override fun onClickReport() {
        PartnerProfileService.partnerReport(this, ReportRequest(getUserIdx()!!, partnerUserIdx))
    }

    override fun onPartnerBlockSuccess() {
        showToast("해당 유저를 차단하여 더이상 나타나지 않습니다.")
        finish()
    }

    override fun onPartnerBlockFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        finish()
    }

    override fun onPartnerReportSuccess() {
        showToast("해당 유저에 대한 신고가 접수되어 고객센터에서 검토할 예정입니다.")
        finish()
    }

    override fun onPartnerReportFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        finish()
    }

}

