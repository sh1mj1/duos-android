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
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.entities.PartnerResDto
import com.example.duos.data.entities.ReviewResDto
import com.example.duos.data.entities.StarredFriend
import com.example.duos.data.remote.myFriendList.FriendListService
import com.example.duos.data.remote.partnerProfile.PartnerProfileService
import com.example.duos.databinding.FragmentPlayerBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.friendList.AddStarredFriendView
import com.example.duos.ui.main.friendList.DeleteStarredFriendView
import com.example.duos.ui.main.friendList.StarredFriendListView
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.PartnerProfileListView
import com.example.duos.ui.main.mypage.myprofile.PartnerProfileReviewRVAdapter
import com.example.duos.utils.getUserIdx
import com.google.gson.Gson

// 내 프로필 , 모든 리뷰 보기, 지난 약속 보기, 파트너 서치, 채팅방에서 넘어올 수 있어
class PlayerFragment : BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate),
    PartnerProfileListView, AddStarredFriendView, DeleteStarredFriendView {
    val TAG: String = "PlayerFragment"

    private var partnerProfileReviewDatas = ArrayList<ReviewResDto>()
    private var friendListDatas = ArrayList<StarredFriend>()
    val myUserIdx = getUserIdx()!!
    private var isStarred: Boolean = false
    var partnerUserIdx: Int = 0

    override fun initAfterBinding() {
        Log.d(TAG, "Start_PlayerFragment")

        partnerUserIdx = requireArguments().getInt("partnerUserIdx")  /* From MyProfile OR PlayerProfile? thisIdx*/

        PartnerProfileService.partnerProfileInfo(this, getUserIdx()!!, partnerUserIdx)
        Log.d(TAG, "Create Retrofit")

        (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility = View.VISIBLE
        (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
        (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"

        (context as MyProfileActivity).findViewById<ImageView>(R.id.player_is_starred_iv).setOnClickListener {
            deleteStarredFriend()   /* 친구 찜 취소 */

        }
        (context as MyProfileActivity).findViewById<ImageView>(R.id.player_is_not_starred_iv).setOnClickListener {
            addStarredFriend()  /* 친구 찜하기 */

        }

    }


    private fun deleteStarredFriend() {
        FriendListService.deleteStarredFriend(this, myUserIdx, partnerUserIdx)
        initIsNotStarred()
    }

    private fun addStarredFriend() {
        FriendListService.addStarredFriend(this, myUserIdx, partnerUserIdx)
        initIsStarred()
    }

    @SuppressLint("CutPasteId")
    private fun initIsStarred() {
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_starred_iv).visibility = View.VISIBLE
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_starred_iv).isEnabled = true
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_not_starred_iv).visibility = View.INVISIBLE
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_not_starred_iv).isEnabled = false
    }

    @SuppressLint("CutPasteId")
    private fun initIsNotStarred() {
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_starred_iv).visibility = View.INVISIBLE
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_starred_iv).isEnabled = false
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_not_starred_iv).visibility = View.VISIBLE
        (context as MyProfileActivity).findViewById<ImageButton>(R.id.player_is_not_starred_iv).isEnabled = true
    }

    override fun onGetProfileInfoSuccess(partnerResDto: PartnerResDto) {
        setPartnerProfileInfo(partnerResDto) // 위쪽 프로필 데이터 설정
        setExperienceView()     // 구력 관련 글씨체 적용

        partnerProfileReviewDatas.clear()
        partnerProfileReviewDatas.addAll(partnerResDto.reviewResDto)   // API 로 받아온 데이터 다 넣어주기 (더미데이터 넣듯이)

        Log.d(TAG, partnerResDto.toString())
        // 리사이클러뷰 어댑터 연결, 데이터 연결, 레이아웃 매니저 설정
        val profileReviewRVAdapter = initRecyclerView()

        /* 다른 회원 프로필로 이동*/
        goPlayerProfile(profileReviewRVAdapter)

        /* 해당 회원의 모든 후기 보기 페이지로 이동*/
        goEveryReview(partnerResDto)
    }

    override fun onGetProfileInfoFailure(code: Int, message: String) {
        Toast.makeText(context, "네트워크 불안정", Toast.LENGTH_LONG).show()
        Log.d(TAG, "CODE : $code , MESSAGE : $message ")
    }

    private fun initRecyclerView(): PartnerProfileReviewRVAdapter {
        val profileReviewRVAdapter = PartnerProfileReviewRVAdapter(partnerProfileReviewDatas)
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        binding.playingReviewContentRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return profileReviewRVAdapter
    }


    /* 다른 회원 혹은 내 프로필로 이동*/
    private fun goPlayerProfile(profileReviewRVAdapter: PartnerProfileReviewRVAdapter) {
        profileReviewRVAdapter.clickPlayerReviewListener(object :
            PartnerProfileReviewRVAdapter.PlayerReviewItemClickListener {
            override fun onItemClick(partnerProfileReviewItem: ReviewResDto) {
                // TO other PlayerProfile (PlayerFragment)
                if (partnerProfileReviewItem.writerIdx != getUserIdx()) {
                    val fragmentTransaction: FragmentTransaction =
                        (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                                Log.d(TAG, "다른 회원 프로필에서 다른 회원 프로필로 이동")
                                arguments =
                                    Bundle().apply {/*TODO : 후기를 작성한 writerIdx에 맞게 Fragment 이동 시 해당 Idx를 가진 회원의 프로필로 이동 그 Idx만 전달해도될 듯???*/
                                        putInt("partnerUserIdx", partnerProfileReviewItem.writerIdx!!)
                                    }
                            })
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()    // commit() : FragmentManager가 이미 상태를 저장하지는 않았는지를 검사 이미 상태를 저장한 경우 IllegalStateExceptoion이라는 예외 던짐
                } else { /* TO MyProfile (MyProfileFrag)  */
                    val fragmentTransaction: FragmentTransaction =
                        (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.my_profile_into_fragment_container_fc, MyProfileFragment())
                    Log.d(TAG, "PlayerFrag -> MyProfileFrag")

                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                }

            }
        })
    }

    /* 해당 회원의 모든 후기 보기 페이지로 이동*/
    @SuppressLint("SetTextI18n")
    private fun goEveryReview(partnerResDto: PartnerResDto) {
        binding.playerPlayingReviewCountTv.setOnClickListener {
            val fragmentTransaction: FragmentTransaction =
                (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.my_profile_into_fragment_container_fc, EveryReviewFragment().apply {
                        arguments = Bundle().apply {
                            // 해당 회원의 프로필 정보를 gson.toJson 하여 보낸다.
                            val gson = Gson()
                            val profileJson = gson.toJson(partnerResDto.partnerInfoDto)
                            putString("profile", profileJson)
                            putBoolean("isFromPlayerProfile", true)
                        }
                    })
            fragmentTransaction.addToBackStack(null)// 해당 transaction 을 BackStack에 저장
            fragmentTransaction.commit()    // commit(): FragmentManager가 이미 상태를 저장하지는 않았는지를 검사. 이미 상태를 저장한 경우, IllegalStateException 예외 던짐.

            // 상단 텍스트 변경 (후기 갯수, 프로필 수정하기 버튼 없애기
            (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text =
                "후기(${partnerResDto.partnerInfoDto.reviewCount.toString()})"
            (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility =
                View.GONE

        }
    }

    @SuppressLint("SetTextI18n")
    private fun setPartnerProfileInfo(partnerResDto: PartnerResDto) {
        val profileGenderStr = toGenderStr(partnerResDto.partnerInfoDto.gender)
        val locationName = partnerResDto.partnerInfoDto.locationName
        val locationCategory = partnerResDto.partnerInfoDto.locationCategory
        val location = locationCategory + locationName

        binding.playerNicknameTv.text = partnerResDto.partnerInfoDto.nickname
        binding.playerGenerationTv.text = partnerResDto.partnerInfoDto.age
        binding.playerSexTv.text = profileGenderStr
        binding.profileLocationTv.text = location
        binding.partnerProfileGradeRb.rating = partnerResDto.partnerInfoDto.rating!!
        binding.partnerProfileGradeNumTv.text = partnerResDto.partnerInfoDto.rating.toString()
        binding.playerCareerYearNumTv.text = partnerResDto.partnerInfoDto.experience
        Glide.with(binding.playerProfileImgIv.context)
            .load(partnerResDto.partnerInfoDto.profilePhotoUrl)
            .into(binding.playerProfileImgIv)
        binding.playerIntroductionTv.text = partnerResDto.partnerInfoDto.introduction
        binding.playerCareerPlayedNumTv.text = partnerResDto.partnerInfoDto.gameCount.toString()
        binding.playerPlayingReviewCountTv.text =
            "후기(${partnerResDto.partnerInfoDto.reviewCount.toString()})"

        isStarred = partnerResDto.partnerInfoDto.starred
        if (isStarred) {
            initIsStarred()

        } else {
            initIsNotStarred()
        }
    }

    private fun setExperienceView() {
        val textExperience = binding.playerCareerYearNumTv
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

    override fun onAddStarredFriendSuccess() {
        Log.d(TAG, "친구 찜하기 성공")
    }

    override fun onAddStarredFriendFailure(code: Int, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    override fun onDeleteStarredFriendSuccess() {
        Log.d(TAG, "친구 삭제 성공")
    }

    override fun onDeleteStarredFriendFailure(code: Int, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


}









