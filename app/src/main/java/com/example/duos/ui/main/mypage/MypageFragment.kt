package com.example.duos.ui.main.mypage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import com.bumptech.glide.Glide
import com.example.duos.data.entities.MyPageInfo
import com.example.duos.data.entities.User
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

    private val myUserIdx = getUserIdx()  // sharedPreference 에 있는 내 userIdx

    override fun initAfterBinding() {

        MyPageService.getUserPage(this, myUserIdx)

        initClickListener()
    }

    // 클릭리스너
    private fun initClickListener() {
        // 약속
        val appointmentIcon = binding.myPageLastAppointmentIconIv
        val appointmentText = binding.myPageLastAppointmentTextTv
        val appointmentArrow = binding.myPageLastAppointmentArrowIv
        // 공지사항
        val noticeIcon = binding.myPageNoticeIconIv
        val noticeText = binding.myPageNoticeTextTv
        val noticeArrow = binding.myPageNoticeArrowIv
        // 고객센터
        val customerIcon = binding.myPageCustomerServiceCenterIconIv
        val customerText = binding.myPageCustomerServiceCenterTextTv
        val customerArrow = binding.myPageCustomerServiceCenterArrowIv
        // 설정
        val setUpIcon = binding.myPageSetIconIv
        val setUpText = binding.myPageSetTextTv
        val setUpArrow = binding.myPageSetArrowIv

        // 나의 프로필 클릭 리스너
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

        Log.d(TAG, "onGetMyPageItemSuccess")
        val phoneNumberView = toMyPagePhoneNumber(myPageInfo.phoneNumber)

        binding.myProfileHomeNicknameTv.text = myPageInfo.nickname
        binding.myProfileHomePhoneNumberFirstTv.text = phoneNumberView
        binding.myProfileHomeCareerYearNumTv.text = myPageInfo.experience
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
        // 내 프로필 데이터 불러오기(Room)
        val db = UserDatabase.getInstance(requireContext())
        val myProfileDB = db!!.userDao().getUser(myUserIdx)

        setMyInfoWithoutAPI(myProfileDB)
        // 구력의 년 부분 (숫자) 텍스트 확대 및 Bold
        applyMyExperienceView()
    }

    private fun setMyInfoWithoutAPI(myProfileDB: User) {
        val phoneNumber = toMyPagePhoneNumber(myProfileDB.phoneNumber!!)
        val myPageCareerStr = toCareerStr(myProfileDB.experience)
        binding.myProfileHomeNicknameTv.text = myProfileDB.nickName
        binding.myProfileHomePhoneNumberFirstTv.text = phoneNumber
        Glide.with(binding.myProfileHomeProfileImageIv.context)
            .load(myProfileDB.profileImg)
            .into(binding.myProfileHomeProfileImageIv)
        binding.myProfileHomeCareerYearNumTv.text = myPageCareerStr
    }

    // 구력의 년 부분 (숫자) 텍스트 확대 및 Bold
    private fun applyMyExperienceView() {
        val textExperience = binding.myProfileHomeCareerYearNumTv
        val textExperienceData: String = textExperience.text.toString()
        val textExperienceBuilder = SpannableStringBuilder(textExperienceData)
        val boldSpanEx = StyleSpan(Typeface.BOLD)
        if(textExperienceData == "10년 이상" || textExperienceData == "10년 미만"){
            textExperienceBuilder.setSpan(boldSpanEx, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val sizeBigSpanEx = RelativeSizeSpan(1.56f)
            textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }else{
            textExperienceBuilder.setSpan(boldSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val sizeBigSpanEx = RelativeSizeSpan(1.56f)
            textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        textExperience.text = textExperienceBuilder
    }

}
