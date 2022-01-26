package com.example.duos.ui.main.mypage.myprofile.frag

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.MyProfileReview
import com.example.duos.databinding.FragmentMyProfileBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.ProfileReviewRVAdapter

class MyProfileFragment : BaseFragment<FragmentMyProfileBinding>(FragmentMyProfileBinding::inflate) {
    private var reviewDatas = ArrayList<MyProfileReview>()

    override fun initAfterBinding() {
        // 더미데이터 넣기 (내 프로필에)
        reviewDatas.apply {
            add(
                MyProfileReview(
                    R.drawable.tennis_racket_img_4,
                    "5.0",
                    "koko0311_1",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                MyProfileReview(
                    R.drawable.tennis_racket_img_4,
                    "3.0",
                    "koko0311_2",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                MyProfileReview(
                    R.drawable.tennis_racket_img_4,
                    "4.0",
                    "koko0311_3",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                MyProfileReview(
                    R.drawable.tennis_racket_img_4,
                    "5.0",
                    "koko0311_4",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )

        }

        // 더미 데이터와 Adapter 연결
        val profileReviewRVAdapter = ProfileReviewRVAdapter(reviewDatas)
        // 리사이클러뷰에 어댑터 연결
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        //레이아웃 매니저 설정
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.VERTICAL, false)

        // 리사이클러뷰 아이템 클릭 리스너
        profileReviewRVAdapter.clickPlayerReviewListener(
            object : ProfileReviewRVAdapter.PlayerReviewItemClickListener {
                override fun onItemClick(player: MyProfileReview) {
                    (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                            arguments = Bundle().apply {
                                putString("nickname", player.profileNickname)
                                putString("introduction", player.introduction)
                                putInt("coverImg", player.profileImg!!)
                            }
                        }).commitAllowingStateLoss()
                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                }
            })

        binding.playingReviewTv.setOnClickListener {
            (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.my_profile_into_fragment_container_fc, EveryReviewFragment().apply {
                    arguments = Bundle().apply {

                    }
                }).commitAllowingStateLoss()
            val reviewCount = binding.playingReviewTv.text
            (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = reviewCount.toString()
            (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
        }

    }


}

