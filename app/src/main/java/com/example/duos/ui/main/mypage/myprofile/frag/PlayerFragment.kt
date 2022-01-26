package com.example.duos.ui.main.mypage.myprofile.frag

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.PlayerProfileInfo
import com.example.duos.databinding.FragmentPlayerBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.ProfileReviewRVAdapter

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate) {

    private var reviewDatas = ArrayList<PlayerProfileInfo>()

    override fun initAfterBinding() {

        // arguments로 이전 프래그먼트에서 데이터 받아오고 뷰에 연동하는 부분

        val playerNickname = arguments?.getString("nickname")
        val playerIntroduction = arguments?.getString("introduction")
        val playerImg = arguments?.getInt("coverImg")

        binding.playerNicknameTv.text = playerNickname.toString()
        binding.playerIntroductionTv.text = playerIntroduction.toString()
        binding.playerProfileImgIv.setImageResource(playerImg!!)

        // 실제로는 여기서 API를 받아와서 해당 Player의 인덱스(Idx)값을 가진 데이터를 가져오고 파싱하는 과정.
        // 더미데이터 (리뷰) 넣기
        reviewDatas.apply {
            add(
                PlayerProfileInfo(
                    R.drawable.tennis_racket_img_4,
                    "5.0",
                    "koko0311_1",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                PlayerProfileInfo(
                    R.drawable.tennis_racket_img_4,
                    "3.0",
                    "koko0311_2",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                PlayerProfileInfo(
                    R.drawable.tennis_racket_img_4,
                    "4.0",
                    "koko0311_3",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                PlayerProfileInfo(
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
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )

        // 리사이클러뷰 아이템 클릭 리스너 : 또 다른 플레이어 프로필로 이동
        profileReviewRVAdapter.clickPlayerReviewListener(
            object : ProfileReviewRVAdapter.PlayerReviewItemClickListener {
                override fun onItemClick(player: PlayerProfileInfo) {
                    val fragmentTransaction: FragmentTransaction = (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                            arguments = Bundle().apply {
                                putString("nickname", player.profileNickname)
                                putString("introduction", player.introduction)
                                putInt("coverImg", player.profileImg!!)

                            }
                        })
                    // 해당 transaction 을 BackStack에 저장
                    fragmentTransaction.addToBackStack(null)

                    // 해당 transaction 실행
                    // commit(): FragmentManager가 이미 상태를 저장하지는 않았는지를 검사. 이미 상태를 저장한 경우, IllegalStateException 예외 던짐.
                    fragmentTransaction.commit()

                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                }
            })
        // 모든 후기 보기

        // 해당 회원의 모든 후기 보기 페이지로 이동
        binding.playerPlayingReviewCountTv.setOnClickListener {
            val profileNickname = binding.playerNicknameTv.text.toString()
//            val profileIntroduction = binding.myIntroductionTv.text.toString()
//            val profileImg = resources.getInteger(binding.myProfileImgIv)
            val fragmentTransaction: FragmentTransaction = (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.my_profile_into_fragment_container_fc, EveryReviewFragment().apply {
                    arguments = Bundle().apply {
//                        TODO: 넘길 데이터
                        // 해당 회원의 Idx 값을 받아서 API 파싱해서 받는 곳에서 받으면 될듯.
                        // 일단은 BackStack 테스트 하기 위해서 하나만 넣어두자
                        putString("nickname", profileNickname)
//                        putInt("coverImg", playerProfiㅣleInfo.profileImg!!)

                    }
                })

            // 해당 transaction 을 BackStack에 저장
            fragmentTransaction.addToBackStack(null)

            // 해당 transaction 실행
            // commit(): FragmentManager가 이미 상태를 저장하지는 않았는지를 검사. 이미 상태를 저장한 경우, IllegalStateException 예외 던짐.
            fragmentTransaction.commit()

            // 상단 텍스트 변경
            val reviewCount = binding.playerPlayingReviewCountTv.text
            (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = reviewCount.toString()
            (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
        }


    }


}

