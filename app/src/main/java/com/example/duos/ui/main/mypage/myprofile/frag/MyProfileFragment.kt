package com.example.duos.ui.main.mypage.myprofile.frag

import android.annotation.SuppressLint
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
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.entities.MyProfileResult
import com.example.duos.data.entities.PartnerProfileReviewItem
import com.example.duos.data.remote.myProfile.MyProfileService
import com.example.duos.databinding.FragmentMyProfileBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.ProfileReviewRVAdapter
import com.google.gson.Gson

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate), ProfileListView {
    val TAG: String = "MyProfileFragment"
    private var myProfileReviewDatas = ArrayList<PartnerProfileReviewItem>()


    override fun initAfterBinding() {

        Log.d(TAG, "Start_MypageFragment")
        //TODO userIdx에 어떤 값이 들어갈지
        MyProfileService.myProfileInfo(this, 1)

        (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility = View.GONE
        (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "나의 프로필"


    }

    override fun onGetMyProfileInfoSuccess(myProfile: MyProfileResult) {
        setMyProfileInfo(myProfile) // 위쪽 데이터 설정
        setExperienceView()

        myProfileReviewDatas.clear()
        myProfileReviewDatas.addAll(myProfile.reviews)   // API 로 받아온 데이터 다 넣어주기 (더미데이터 넣듯이)

        // 리사이클러뷰에 어댑터 연결, 데이터 연결, 레이아웃 매니저 설정
        val profileReviewRVAdapter = initRecyclerView()

        /* 다른 회원 프로필로 이동*/
        goPlayerProfile(profileReviewRVAdapter)

        /* 나의 모든 후기 보기 페이지로 이동*/
        goEveryReview(myProfile)
    }

    override fun onGetMyProfileInfoFailure(code: Int, message: String) {
        Toast.makeText(context, "sdf", Toast.LENGTH_LONG).show()
    }

    /* 나의 모든 후기 보기 페이지로 이동*/
    private fun goEveryReview(myProfile: MyProfileResult) {
        binding.playingReviewCountTv.setOnClickListener {
            val profileNickname = binding.myNicknameTv.text.toString()
            val fragmentTransaction: FragmentTransaction =
                (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.my_profile_into_fragment_container_fc, EveryReviewFragment().apply {
                        arguments = Bundle().apply {
                            val gson = Gson()
                            val profileJson = gson.toJson(myProfile.profileInfo)
                            putString("profile", profileJson)
                            //                        putString("profileInfo", myProfile.profileInfo.nickname)

                        }

                    })

            fragmentTransaction.addToBackStack(null)// 해당 transaction 을 BackStack에 저장
            fragmentTransaction.commit()    // commit(): FragmentManager가 이미 상태를 저장하지는 않았는지를 검사. 이미 상태를 저장한 경우, IllegalStateException 예외 던짐.

            // 상단 텍스트 변경
            val reviewCount = binding.playingReviewCountTv.text
            (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = reviewCount.toString()
            (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
        }
    }

    /* 다른 회원 프로필로 이동*/
    private fun goPlayerProfile(profileReviewRVAdapter: ProfileReviewRVAdapter) {
        profileReviewRVAdapter.clickPlayerReviewListener(
            object : ProfileReviewRVAdapter.PlayerReviewItemClickListener {
                override fun onItemClick(myProfileReviewItem: PartnerProfileReviewItem) {
                    val fragmentTransaction: FragmentTransaction = (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                            Log.d(TAG, "MyProfileFrag -> PlayerFrag")
                            arguments = Bundle().apply {
                                putInt("thisIdx", myProfileReviewItem.writerIdx!!)
                                /*TODO : 후기를 작성한 writerIdx에 맞게 Fragment 이동 시 해당 Idx를 가진 회원의 프로필로 이동해야되 그 Idx 만 전달*/

                            }
                        })
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()    // commit() : FragmentManager가 이미 상태를 저장하지는 않았는지를 검사 이미 상태를 저장한 경우 IllegalStateExceptoion이라는 예외 던짐

                    // 상단 텍스트 변경
                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                    (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility = View.VISIBLE
                }
            })
    }

    // 리사이클러뷰에 어댑터 연결, 데이터 연결, 레이아웃 매니저 설정
    private fun initRecyclerView(): ProfileReviewRVAdapter {
        val profileReviewRVAdapter = ProfileReviewRVAdapter(myProfileReviewDatas)
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return profileReviewRVAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setMyProfileInfo(myProfile: MyProfileResult) {
        Glide.with(binding.myProfileImgIv.context)
            .load(myProfile.profileInfo.profileImgUrl)
            .into(binding.myProfileImgIv)
        binding.myNicknameTv.text = myProfile.profileInfo.nickname
        binding.myGenerationTv.text = myProfile.profileInfo.age
        binding.myLocationTv.text = myProfile.profileInfo.location
        binding.myGradeNumTv.text = myProfile.profileInfo.rating.toString()
        binding.myGradeRb.rating = myProfile.profileInfo.rating!!
        binding.myIntroductionTv.text = myProfile.profileInfo.introduction
        binding.careerYearNumTv.text = myProfile.profileInfo.experience
        binding.careerPlayedNumTv.text = myProfile.profileInfo.gamesCount.toString()
        binding.playingReviewCountTv.text = "후기(${myProfile.profileInfo.reviewCount.toString()})"
    }

    private fun setExperienceView() {
        val textExperience = binding.careerYearNumTv
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

        // API의 reviewCount의 View -> 위에서 하면 되지 않을까
//        val textReviewCount = binding.playingReviewCountTv
//        var textReviewCountData : String = textReviewCount.text.toString()
//        textReviewCountData = "후기($textReviewCountData)"
//        binding.playingReviewCountTv.text = textReviewCountData

    }

}










