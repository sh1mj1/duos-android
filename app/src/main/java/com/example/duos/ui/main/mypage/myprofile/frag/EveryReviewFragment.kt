package com.example.duos.ui.main.mypage.myprofile.frag

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.MyProfileReviewItem
import com.example.duos.databinding.FragmentEveryReviewBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.ProfileReviewRVAdapter

class EveryReviewFragment : BaseFragment<FragmentEveryReviewBinding>(FragmentEveryReviewBinding::inflate) {

    private var reviewDatas = ArrayList<MyProfileReviewItem>()
    override fun initAfterBinding() {

        reviewDatas.apply {
            add(
                MyProfileReviewItem(
                    0,
                    0,
                    "00",
                    "gg",
                    1.0f,
                    "11.1.1",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                MyProfileReviewItem(
                    1,
                    1,
                    "01",
                    "gg",
                    1.1f,
                    "22.2.2",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                MyProfileReviewItem(
                    1,
                    1,
                    "01",
                    "gg",
                    1.1f,
                    "22.2.2",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )
            add(
                MyProfileReviewItem(
                    0,
                    0,
                    "00",
                    "gg",
                    1.0f,
                    "11.1.1",
                    "처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"
                )
            )

        }

//        val profileNickname = arguments?.getString("nickname")
//        val profileIntroduction = arguments?.getString("introduction")

//        binding.playerNicknameTv.text = profileNickname
        // 나머지 바인딩
        //실제로는 여기서 API에서 받아온 Idx 값에 따른 데이터들을 받아오면 된다.
        // 일단 더미 데이터로 recyclerView에 넣어두자


// 더미 데이터와 Adapter 연결
        val profileReviewRVAdapter = ProfileReviewRVAdapter(reviewDatas)
        // 리사이클러뷰에 어댑터 연결
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        //레이아웃 매니저 설정
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL, false
        )
        // 리사이클러뷰 아이템 클릭 리스너 -> 여기서 만약 내가 작성한 후기를 누른다면 PlayerFragment가 아니라 MyFragment로 이동해야.
        profileReviewRVAdapter.clickPlayerReviewListener(
            object : ProfileReviewRVAdapter.PlayerReviewItemClickListener {
                override fun onItemClick(myProfileReviewItem: MyProfileReviewItem) {
                    val fragmentTransaction: FragmentTransaction = (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                            arguments = Bundle().apply {

                            }

                        })

                    // 해당 transaction을 BackStack에 저장
                    fragmentTransaction.addToBackStack(null)

                    // 해당 transaction 실행
                    // commit() : FragmentManager가 이미 상태를 저장하지는 않았는지를 검사 이미 상태를 저장한 경우 IllegalStateExceptoion이라는 예외 던짐
                    fragmentTransaction.commit()

                    // 상단 텍스트 변경
                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                }
            })


    }

}

