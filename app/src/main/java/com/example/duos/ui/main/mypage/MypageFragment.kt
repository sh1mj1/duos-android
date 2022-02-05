package com.example.duos.ui.main.mypage

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.duos.data.entities.MyPageInfo
import com.example.duos.data.remote.myPage.MyPageService
import com.example.duos.databinding.FragmentMypageBinding
import com.example.duos.ui.main.mypage.customerservice.CustomerServiceActivity
import com.example.duos.ui.main.mypage.lastpromise.PreviousGameActivity
import com.example.duos.ui.main.mypage.myprofile.MyPageItemView
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.notion.NotionActivity
import com.example.duos.ui.main.mypage.setup.SetupActivity

class MypageFragment() : Fragment(), MyPageItemView {

    companion object {
        private const val TAG: String = "MyPageService"
    }

    lateinit var binding: FragmentMypageBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMypageBinding.inflate(inflater, container, false)
        Log.d(TAG, "Start_MypageFragment")

        MyPageService.getUserPage(this, 2)
        // 클릭리스너
        initClickListener()
        return binding.root
    }

    private fun initClickListener() {
        //        나의 프로필 클릭 리스너
        binding.myProfileHomeConstraintLayoutCl.setOnClickListener {
            val intent = Intent(activity, MyProfileActivity::class.java)
            startActivity(intent)
        }
        //        지난 약속 클릭 리스너
        binding.myPageLastAppointmentTextTv.setOnClickListener {
            val intent = Intent(activity, PreviousGameActivity::class.java)
            startActivity(intent)
        }
        //        공지 사항 클릭 리스너
        binding.myPageNoticeTextTv.setOnClickListener {
            val intent = Intent(activity, NotionActivity::class.java)
            startActivity(intent)
        }
        //        고객 센터 클릭 리스너
        binding.myPageCustomerServiceCenterTextTv.setOnClickListener {
            val intent = Intent(activity, CustomerServiceActivity::class.java)
            startActivity(intent)
        }
        //       설정 클릭 리스너
        binding.myPageSetTextTv.setOnClickListener {
            val intent = Intent(activity, SetupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onGetMyPageItemSuccess(myPageInfo: MyPageInfo) {
        Log.d(TAG, "onGetMyPageItemSuccess")
        binding.myProfileHomeNicknameTv.text = myPageInfo.nickname
        var phoneNumberView = myPageInfo.phoneNumber
        if (phoneNumberView.length >= 9) {
            phoneNumberView = phoneNumberView.substring(0, 9)
        }
        binding.myProfileHomePhoneNumberFirstTv.text = phoneNumberView
        binding.myProfileHomeCareerYearNumTv.text = myPageInfo.experience
        binding.myProfileHomeCareerPlayedNumTv.text = myPageInfo.gamesCount.toString()
        Glide.with(binding.myProfileHomeProfileImageIv.context)
            .load(myPageInfo.profileImgUrl)
            .into(binding.myProfileHomeProfileImageIv)
        //  ?년 미만 구력 참조
        val textExperience = binding.myProfileHomeCareerYearNumTv
        // String 문자열 데이터 취득
        val textExperienceData: String = textExperience.text.toString()
        // SpannableStringBuilder 타입으로 변환
        val textExperienceBuilder = SpannableStringBuilder(textExperienceData)
        // index=0 에 해당하는 문자열(0)에 볼드체, 크기 적용
        val boldSpanEx = StyleSpan(Typeface.BOLD)
        textExperienceBuilder.setSpan(boldSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val sizeBigSpanEx = RelativeSizeSpan(1.56f)
        textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // TextView에 적용
        textExperience.text = textExperienceBuilder

    }

    override fun onGetMyPageItemFailure(code: Int, message: String) {
        Log.d(TAG, "code: $code , message : $message ")
    }

}

