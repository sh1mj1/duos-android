package com.example.duos.ui.main.mypage.myprofile.frag

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.entities.MyProfileResult
import com.example.duos.data.entities.PartnerProfileReviewItem
import com.example.duos.data.entities.profile.ProfileListView
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.myProfile.MyProfileService
import com.example.duos.databinding.FragmentMyProfileBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.adapter.ProfileReviewRVAdapter
import com.example.duos.utils.getUserIdx
import com.google.gson.Gson

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate),
    ProfileListView {
    val TAG: String = "MyProfileFragment"
    private var myProfileReviewDatas = ArrayList<PartnerProfileReviewItem>()
    private val myUserIdx = getUserIdx()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
    }

    override fun onResume() {
        super.onResume()
        MyProfileService.myProfileInfo(this, myUserIdx)
    }

    override fun initAfterBinding() {
        // 내 프로필 데이터 불러오기(Room)
        val db = UserDatabase.getInstance(requireContext())
        val myProfileDB = db!!.userDao().getUser(getUserIdx())
        binding.mySexTv.text = toGenderStr(myProfileDB.gender!!)


        // 화면 상단 뷰 설정 (채팅 Constraint layout GONE, 상단 text : 나의 프로필, 나의 프로필 수정하기, 뒤로가기)
        val fragmentTransaction: FragmentManager = requireActivity().supportFragmentManager
        (context as MyProfileActivity).apply {
            findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility = View.GONE
            findViewById<TextView>(R.id.top_myProfile_tv).text = "나의 프로필"
            findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.VISIBLE
            findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
                if (fragmentTransaction.backStackEntryCount >= 1){
                    fragmentTransaction.popBackStack()
                }else{
                    requireActivity().finish()
                }
            }
        }


    }

    override fun onGetMyProfileInfoSuccess(myProfile: MyProfileResult) {
        setMyProfileInfo(myProfile) // 위쪽 데이터 (프로필) 설정
        setExperienceView()

        myProfileReviewDatas.clear()
        myProfileReviewDatas.addAll(myProfile.reviews)
        startPostponedEnterTransition()
        /* 리사이클러뷰에 어댑터 연결, 데이터 연결, 레이아웃 매니저 설정*/
        val profileReviewRVAdapter = initRecyclerView()
        goPlayerProfile(profileReviewRVAdapter) /* 다른 회원 프로필로 이동 ClickListener */
        goEveryReview(myProfile)    /* 나의 모든 후기 보기 페이지로 이동 ClickListener */
    }

    override fun onGetMyProfileInfoFailure(code: Int, message: String) {
        startPostponedEnterTransition()
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        // 내 프로필 데이터 불러오기 (Room)
        setMyProfileInfoWithoutAPI()
        setExperienceView()
    }

    private fun setMyProfileInfoWithoutAPI() {
        val db = UserDatabase.getInstance(requireContext())
        val myProfileDB = db!!.userDao().getUser(myUserIdx)
        Log.d(TAG, "myProfileDB :  $myProfileDB")

        Glide.with(binding.myProfileImgIv.context)
            .load(myProfileDB.profileImg)
            .into(binding.myProfileImgIv)
        binding.myNicknameTv.text = myProfileDB.nickName
        binding.mySexTv.text = toGenderStr(myProfileDB.gender!!)
        binding.myLocationTv.text = toLocationStr(myProfileDB.location!!)
        binding.myIntroductionTv.text = myProfileDB.introduce
        binding.careerYearNumTv.text = toCareerStr(myProfileDB.experience)
    }


    /* 나의 모든 후기 보기 페이지로 이동*/
    @SuppressLint("SetTextI18n")
    private fun goEveryReview(myProfile: MyProfileResult) {
        binding.playingReviewCountTv.setOnClickListener {
            val fragmentTransaction: FragmentTransaction =
                (context as MyProfileActivity).supportFragmentManager.beginTransaction()
            fragmentTransaction.apply {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.enter_from_bottom_anim,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.exit_to_bottom
                )
                replace(R.id.my_profile_into_fragment_container_fc, EveryReviewFragment().apply {
                    arguments = Bundle().apply {
                        val gson = Gson()   /* 나의 프로필 정보 gson.toJson 해서 모든 리뷰 보기 Frag로  보내주기*/
                        val profileJson = gson.toJson(myProfile.profileInfo)
                        putString("profile", profileJson)
                        putBoolean("isFromMyProfile", true)
                    }
                })
                addToBackStack(null)
                commit()
            }

            // 상단 텍스트 변경
            (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text =
                "후기(${myProfile.profileInfo.reviewCount.toString()})"
            (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility =
                View.GONE
        }
    }

    /* 다른 회원 프로필로 이동*/
    private fun goPlayerProfile(profileReviewRVAdapter: ProfileReviewRVAdapter) {
        profileReviewRVAdapter.clickPlayerReviewListener(
            object : ProfileReviewRVAdapter.PlayerReviewItemClickListener {
                override fun onItemClick(myProfileReviewItem: PartnerProfileReviewItem) {
                    val fragmentTransaction: FragmentTransaction =
                        (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                    fragmentTransaction.apply {
                        setReorderingAllowed(true)
                        setCustomAnimations(
                            R.anim.enter_from_right_anim,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.exit_to_right
                        )
                        replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                            Log.d(TAG, "MyProfileFrag -> PlayerFrag")
                            arguments = Bundle().apply {
                                putInt("partnerUserIdx", myProfileReviewItem.writerIdx!!)
                            }
                        })
                        addToBackStack(null)
                        commit()
                    }
                    // 상단 텍스트 변경
                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text =
                        "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility =
                        View.GONE
                    (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility =
                        View.VISIBLE
                }
            })
    }

    // 리사이클러뷰에 어댑터 연결, 데이터 연결, 레이아웃 매니저 설정
    private fun initRecyclerView(): ProfileReviewRVAdapter {
        val profileReviewRVAdapter = ProfileReviewRVAdapter(myProfileReviewDatas)
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        binding.playingReviewContentRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return profileReviewRVAdapter
    }

    // 내 프로필 데이터 설정 (API) {닉네임, 나이, 위치, 평점(2), 소개글, 구력, 게임 수
    @SuppressLint("SetTextI18n")
    private fun setMyProfileInfo(myProfile: MyProfileResult) {
        Glide.with(binding.myProfileImgIv.context)
            .load(myProfile.profileInfo.profileImgUrl)
            .into(binding.myProfileImgIv)
        binding.myNicknameTv.text = myProfile.profileInfo.nickname  // nickname
        binding.myGenerationTv.text = myProfile.profileInfo.age     // 나이
        binding.myLocationTv.text = myProfile.profileInfo.location  // 위치 이것도 Str 로 넘어오네
        binding.myGradeNumTv.text = myProfile.profileInfo.rating.toString() // 평점 텍스트
        binding.myGradeRb.rating = myProfile.profileInfo.rating!!           // 평점
        binding.myIntroductionTv.text = myProfile.profileInfo.introduction  // 소개글
        binding.careerYearNumTv.text = myProfile.profileInfo.experience     // 구력 Str 로 넘어옴.
        binding.careerPlayedNumTv.text = myProfile.profileInfo.gamesCount.toString()    // 게임수
        binding.playingReviewCountTv.text = "후기(${myProfile.profileInfo.reviewCount.toString()})"
    }

    // /* 구력 텍스트 년(숫자) 부분 확대 및 bold 체 적용 */
    private fun setExperienceView() {
        val textExperience = binding.careerYearNumTv
        val textExperienceData: String = textExperience.text.toString()
        val textExperienceBuilder = SpannableStringBuilder(textExperienceData)
        val boldSpanEx = StyleSpan(Typeface.BOLD)
        if (textExperienceData == "10년 이상" || textExperienceData == "10년 미만") {
            textExperienceBuilder.setSpan(boldSpanEx, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val sizeBigSpanEx = RelativeSizeSpan(1.56f)
            textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else {
            textExperienceBuilder.setSpan(boldSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val sizeBigSpanEx = RelativeSizeSpan(1.56f)
            textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        // TextView에 적용
        textExperience.text = textExperienceBuilder
    }
}










