package com.example.duos.ui.main.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
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

class MypageFragment() : BaseFragment<FragmentMypageBinding>(FragmentMypageBinding::inflate),
    MyPageItemView {

    val TAG: String = "MyPageService"

    val myUserIdx = getUserIdx()!!  // sharedPreference 에 있는 내 userIdx

    override fun initAfterBinding() {

        Log.d(TAG, "Start_MypageFragment")
        Log.d(TAG, "현재 user의 userIdx : $myUserIdx")

        MyPageService.getUserPage(this, myUserIdx)

        // 클릭리스너
        initClickListener()

    }

    // 클릭리스너
    private fun initClickListener() {
        // 나의 프로필 클릭 리스너
        val appointmentIcon = binding.myPageLastAppointmentIconIv
        val appointmentText = binding.myPageLastAppointmentTextTv
        val appointmentArrow = binding.myPageLastAppointmentArrowIv

        val noticeIcon = binding.myPageNoticeIconIv
        val noticeText = binding.myPageNoticeTextTv
        val noticeArrow = binding.myPageNoticeArrowIv

        val customerIcon = binding.myPageCustomerServiceCenterIconIv
        val customerText = binding.myPageCustomerServiceCenterTextTv
        val customerArrow = binding.myPageCustomerServiceCenterArrowIv

        val setUpIcon = binding.myPageSetIconIv
        val setUpText = binding.myPageSetTextTv
        val setUpArrow = binding.myPageSetArrowIv

        binding.myProfileHomeConstraintLayoutCl.setOnClickListener {
            val intent = Intent(activity, MyProfileActivity::class.java)
            startActivity(intent)
        }

        // 지난 약속
        appointmentIcon.setOnClickListener {
            val intent = Intent(activity, LastAppointmentActivity::class.java)
            startActivity(intent) }
        appointmentText.setOnClickListener {
            val intent = Intent(activity, LastAppointmentActivity::class.java)
            startActivity(intent) }
        appointmentArrow.setOnClickListener {
            val intent = Intent(activity, LastAppointmentActivity::class.java)
            startActivity(intent) }
        // 공지 사항
        noticeIcon.setOnClickListener {
            val intent = Intent(activity, NotionActivity::class.java)
            startActivity(intent) }
        noticeText.setOnClickListener {
            val intent = Intent(activity, NotionActivity::class.java)
            startActivity(intent) }
        noticeArrow.setOnClickListener {
            val intent = Intent(activity, NotionActivity::class.java)
            startActivity(intent) }
        // 고객 센터
        customerIcon.setOnClickListener {
            val intent = Intent(activity, CustomerServiceActivity::class.java)
            startActivity(intent) }
        customerText.setOnClickListener {
            val intent = Intent(activity, CustomerServiceActivity::class.java)
            startActivity(intent) }
        customerArrow.setOnClickListener {
            val intent = Intent(activity, CustomerServiceActivity::class.java)
            startActivity(intent) }
        // 설정
        setUpIcon.setOnClickListener {
            val intent = Intent(activity, SetupActivity::class.java)
            startActivity(intent) }
        setUpText.setOnClickListener {
            val intent = Intent(activity, SetupActivity::class.java)
            startActivity(intent) }
        setUpArrow.setOnClickListener {
            val intent = Intent(activity, SetupActivity::class.java)
            startActivity(intent) }

    }

    override fun onGetMyPageItemSuccess(myPageInfo: MyPageInfo) {
        //Toast.makeText(context, "$TAG , onGetMyPageItemSuccess", Toast.LENGTH_LONG).show()

        Log.d(TAG, "onGetMyPageItemSuccess")
        val phoneNumberView = toMyPagePhoneNumber(myPageInfo.phoneNumber)

        binding.myProfileHomeNicknameTv.text = myPageInfo.nickname
        binding.myProfileHomePhoneNumberFirstTv.text = phoneNumberView
        binding.myProfileHomeCareerYearNumTv.text = myPageInfo.experience   // 이건 또 Str 타입이네
        binding.myProfileHomeCareerPlayedNumTv.text = myPageInfo.gamesCount.toString()
        Glide.with(binding.myProfileHomeProfileImageIv.context)
            .load(myPageInfo.profileImgUrl)
            .into(binding.myProfileHomeProfileImageIv)

        //  ?년 미만 구력 참조
        applyMyExperienceView()

    }


    @SuppressLint("SetTextI18n")
    override fun onGetMyPageItemFailure(code: Int, message: String) {
        Log.d(TAG, "code: $code , message : $message ")
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")

        // 룸에서 내 idx에 맞는 데이터 불러오기.
        val db = UserDatabase.getInstance(requireContext())
        val myProfileDB = db!!.userDao().getUser(myUserIdx)
        Log.d(TAG, "myProfileDB :  $myProfileDB")

        val phoneNumber = toMyPagePhoneNumber(myProfileDB.phoneNumber!!)    // 010 7441 **** 형태로 binding
        val myPageCareerStr = toCareerStr(myProfileDB.experience)   // CareerIdx -> CareerStr

        binding.myProfileHomeNicknameTv.text = myProfileDB.nickName
        binding.myProfileHomePhoneNumberFirstTv.text = phoneNumber
        Glide.with(binding.myProfileHomeProfileImageIv.context)
            .load(myProfileDB.profileImg)
            .into(binding.myProfileHomeProfileImageIv)
        binding.myProfileHomeCareerYearNumTv.text = myPageCareerStr

        //  ?년 미만 구력 참조
        applyMyExperienceView()

    }


    private fun applyMyExperienceView() {
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

}

