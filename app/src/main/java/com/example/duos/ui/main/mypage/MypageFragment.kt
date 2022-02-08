package com.example.duos.ui.main.mypage

import android.content.Intent
import android.graphics.Typeface
import android.service.autofill.UserData
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.duos.ApplicationClass
import com.example.duos.data.entities.MyPageInfo
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.myPage.MyPageService
import com.example.duos.databinding.FragmentMypageBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.customerservice.CustomerServiceActivity
import com.example.duos.ui.main.mypage.appointment.LastAppointmentActivity
import com.example.duos.ui.main.mypage.myprofile.MyPageItemView
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.notion.NotionActivity
import com.example.duos.ui.main.mypage.setup.SetupActivity
import com.example.duos.utils.getUserIdx

class MypageFragment() : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate), MyPageItemView {

    val TAG: String = "MyPageService"

    lateinit var myPageCareerStr: String
    val userIdx = getUserIdx()

    override fun initAfterBinding() {

        Log.d(TAG, "Start_MypageFragment")
        Log.d(TAG, "현재 user의 userIdx : $userIdx")

        MyPageService.getUserPage(this, userIdx!!)        /*TODO : 왼족 userIdx에 내 userIdx 넣기 (Room)*/


        // 클릭리스너
        initClickListener()

    }



    private fun initClickListener() {
        // 나의 프로필 클릭 리스너
        binding.myProfileHomeConstraintLayoutCl.setOnClickListener {
            val intent = Intent(activity, MyProfileActivity::class.java)
            startActivity(intent)
        }
        // 지난 약속 클릭 리스너
        binding.myPageLastAppointmentTextTv.setOnClickListener {
            val intent = Intent(activity, LastAppointmentActivity::class.java)
            startActivity(intent)
        }
        // 공지 사항 클릭 리스너
        binding.myPageNoticeTextTv.setOnClickListener {
            val intent = Intent(activity, NotionActivity::class.java)
            startActivity(intent)
        }
        //  고객 센터 클릭 리스너
        binding.myPageCustomerServiceCenterTextTv.setOnClickListener {
            val intent = Intent(activity, CustomerServiceActivity::class.java)
            startActivity(intent)
        }
        //  설정 클릭 리스너
        binding.myPageSetTextTv.setOnClickListener {
            val intent = Intent(activity, SetupActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onGetMyPageItemSuccess(myPageInfo: MyPageInfo) {
        Toast.makeText(context, "$TAG , onGetMyPageItemSuccess", Toast.LENGTH_LONG)
        Log.d(TAG, "onGetMyPageItemSuccess")
        binding.myProfileHomeNicknameTv.text = myPageInfo.nickname
        var phoneNumberView = myPageInfo.phoneNumber
        if (phoneNumberView.length >= 9) {
            phoneNumberView = phoneNumberView.substring(0, 3) + " "+ phoneNumberView.substring(3,7) + " "
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
        val sizeBigSpanEx = RelativeSizeSpan(1.50f)
        textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // TextView에 적용
        textExperience.text = textExperienceBuilder

    }

    override fun onGetMyPageItemFailure(code: Int, message: String) {
        Log.d(TAG, "code: $code , message : $message ")
        Toast.makeText(context, "$TAG , onGetMyPageItemFailure", Toast.LENGTH_LONG)


        val db = UserDatabase.getInstance(requireContext())
        // 룸에 내 idx에 맞는 데이터 있으면 불러오기...
        val myProfileDB = db!!.userDao().getUser(userIdx!!)
        Log.d(TAG, "myProfileDB :  $myProfileDB")

        binding.myProfileHomeNicknameTv.text = myProfileDB.nickName
//        binding.myProfileHomePhoneNumberFirstTv.text = myProfileDB.phoneNumber
        var phoneNumberView = myProfileDB.phoneNumber
        if (phoneNumberView!!.length >= 9) {
            phoneNumberView = phoneNumberView.substring(0, 3) + " "+ phoneNumberView.substring(3,7) + " "
        }
        binding.myProfileHomePhoneNumberFirstTv.text = phoneNumberView
        Glide.with(binding.myProfileHomeProfileImageIv.context)
            .load(myProfileDB.profileImg)
            .into(binding.myProfileHomeProfileImageIv)
        val myPageCareerIdx = myProfileDB.experience
        makeCareerIntToStr(myPageCareerIdx)
        binding.myProfileHomeCareerYearNumTv.text = myPageCareerStr


        //  ?년 미만 구력 참조
        val textExperience = binding.myProfileHomeCareerYearNumTv
        // String 문자열 데이터 취득
        val textExperienceData: String = textExperience.text.toString()
        // SpannableStringBuilder 타입으로 변환
        val textExperienceBuilder = SpannableStringBuilder(textExperienceData)
        // index=0 에 해당하는 문자열(0)에 볼드체, 크기 적용
        val boldSpanEx = StyleSpan(Typeface.BOLD)
        textExperienceBuilder.setSpan(boldSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val sizeBigSpanEx = RelativeSizeSpan(1.50f)
        textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // TextView에 적용
        textExperience.text = textExperienceBuilder
    }




    private fun makeCareerIntToStr(myPageCareerIdx: Int?) {
        when (myPageCareerIdx) {
            1 -> myPageCareerStr = "1개월 미만"
            2 -> myPageCareerStr = "3개월 미만"
            3 -> myPageCareerStr = "6개월 미만"
            4 -> myPageCareerStr = "1년 미만"
            5 -> myPageCareerStr = "2년 미만"
            6 -> myPageCareerStr = "3년 미만"
            7 -> myPageCareerStr = "4년 미만"
            8 -> myPageCareerStr = "5년 미만"
            9 -> myPageCareerStr = "6년 미만"
            10 -> myPageCareerStr = "7년 미만"
            11 -> myPageCareerStr = "8년 미만"
            12 -> myPageCareerStr = "9년 미만"
            13 -> myPageCareerStr = "10년 미만"
            14 -> myPageCareerStr = "11년 이상"
        }
    }

}

