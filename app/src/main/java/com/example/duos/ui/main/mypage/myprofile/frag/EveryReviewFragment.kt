package com.example.duos.ui.main.mypage.myprofile.frag

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.entities.everyReviews.EveryReviewsItem
import com.example.duos.data.entities.MyProfileInfoItem
import com.example.duos.data.entities.PartnerInfoDto
import com.example.duos.data.entities.PartnerResDto
import com.example.duos.data.remote.everyReviews.EveryReviewsResponse
import com.example.duos.data.remote.everyReviews.EveryReviewsService
import com.example.duos.databinding.FragmentEveryReviewBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.EveryReviewRVAdapter
import com.example.duos.data.entities.everyReviews.EveryReviewsItemView
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.utils.getUserIdx
import com.google.gson.Gson

class EveryReviewFragment : BaseFragment<FragmentEveryReviewBinding>(FragmentEveryReviewBinding::inflate), EveryReviewsItemView {

    val TAG: String = "EveryReviewFragment"
    private var gson: Gson = Gson()
    private var everyReviewDatas = ArrayList<EveryReviewsItem>()
    val myUserIdx = getUserIdx()!!

    override fun initAfterBinding() {

        Log.d(TAG, "Start_EveryReviewFragment")


        // HomeFragment 에서 넘어온 데이터 받아오기 혹은 PlayerFragment에서 넘어온 데이터 받아오기
        // -> 조건문 사용해서 argument로 받아온 것의 key가 무엇인지 체크하고 그에 해당하는 argument를 gson.fromJsom으로 받기

        val profileData = arguments?.getString("profile")   /* From MyProfileFrg Or From PlayerFrag*/
        val isFromMyProfile = arguments?.getBoolean("isFromMyProfile", false)
        val isFromPlayerProfile = arguments?.getBoolean("isFromPlayerProfile", false)

        if (isFromPlayerProfile == true) {
            val profile = gson.fromJson(profileData, PartnerInfoDto::class.java)
            setInit(profile)
            EveryReviewsService.getEveryReviews(this, myUserIdx, profile.userIdx!!)
        } else if (isFromMyProfile == true) {
            val profile = gson.fromJson(profileData, MyProfileInfoItem::class.java)
            setInit(profile)
            EveryReviewsService.getEveryReviews(this, myUserIdx, profile.userIdx!!)
        }

        // 상단 뒤로 가기 버튼 클릭
        val fragmentTransaction : FragmentManager = requireActivity().supportFragmentManager
        (context as MyProfileActivity).findViewById<ImageView>(com.example.duos.R.id.top_left_arrow_iv).setOnClickListener {
            //TODO  : IF :  backstack Exist -> popbackstack    ELSE :   finish()

            if(fragmentTransaction.backStackEntryCount >= 1){
                fragmentTransaction.popBackStack()
            }else{
                requireActivity().finish()
            }
        }
        (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility = View.GONE


    }

    override fun onGetEveryReviewsItemSuccess(everyReviewsResponse: EveryReviewsResponse) {
        everyReviewDatas.clear()
        everyReviewDatas.addAll(everyReviewsResponse.result)   // API 로 받아온 데이터 다 넣어주기 (더미데이터 넣듯이)
        // 리사이클러뷰 어댑터 연결
        val everyReviewRVAdapter = initRecyclerView()
        // 다른 PlayerProfile 로 이동
        goPlayerProfile(everyReviewRVAdapter)
    }

    override fun onGetEveryReviewsItemFailure(code: Int, message: String) {
        Toast.makeText(context, "네트워크 상태 확인 후 다시 시도해주세요.", Toast.LENGTH_LONG).show()

    }

    // 다른 PlayerProfile 로 이동
    private fun goPlayerProfile(everyReviewRVAdapter: EveryReviewRVAdapter) {
        everyReviewRVAdapter.clickPlayerReviewListener(object : EveryReviewRVAdapter.EveryReviewItemClickListener {
            override fun onItemClick(everyReveiwsItem: EveryReviewsItem) {

                if(everyReveiwsItem.writerIdx == myUserIdx){    /* 내 프로필로 이동*/
                    val fragmentTransaction: FragmentTransaction =
                        (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.my_profile_into_fragment_container_fc, MyProfileFragment())

                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()
                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                    (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility = View.VISIBLE

                }else{
                    val fragmentTransaction: FragmentTransaction =
                        (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                            .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                                arguments = Bundle().apply {
                                    putInt("partnerUserIdx", everyReveiwsItem.writerIdx!!)
                                }

                            })
                    fragmentTransaction.addToBackStack(null)
                    fragmentTransaction.commit()    // commit() : FragmentManager가 이미 상태를 저장하지는 않았는지를 검사 이미 상태를 저장한 경우 IllegalStateExceptoion이라는 예외 던짐

                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                    (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility =
                        View.VISIBLE
                }

            }

        })
    }

    // 리사이클러뷰 어댑터 연결
    private fun initRecyclerView(): EveryReviewRVAdapter {
        val everyReviewRVAdapter = EveryReviewRVAdapter(everyReviewDatas)
        binding.playingReviewContentRv.adapter = everyReviewRVAdapter
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return everyReviewRVAdapter
    }


    private fun setInit(profile: MyProfileInfoItem) {
        binding.playerNicknameTv.text = profile.nickname
        binding.playerGenerationTv.text = profile.age
        binding.playerLocationTv.text = profile.location
        binding.profileGradeRb.rating = profile.rating!!.toFloat()
        binding.profileGradeNumTv.text = toRatingStr(profile.rating!!)
        Glide.with(binding.playerProfileImgIv.context)
            .load(profile.profileImgUrl)
            .into(binding.playerProfileImgIv)
        (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text =
            "후기(${profile.reviewCount.toString()})"
    }

    private fun setInit(profile: PartnerInfoDto) {
        val locationFull = profile.locationCategory + " " + profile.locationName
        val ratingStr = toRatingStr(profile.rating!!)

        binding.playerNicknameTv.text = profile.nickname
        binding.playerGenerationTv.text = profile.age
        binding.playerLocationTv.text = locationFull
        binding.profileGradeRb.rating = profile.rating!!.toFloat()
        binding.profileGradeNumTv.text = ratingStr
        Glide.with(binding.playerProfileImgIv.context)
            .load(profile.profilePhotoUrl)
            .into(binding.playerProfileImgIv)
        (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text =
            "후기(${profile.reviewCount.toString()})"
    }


}





