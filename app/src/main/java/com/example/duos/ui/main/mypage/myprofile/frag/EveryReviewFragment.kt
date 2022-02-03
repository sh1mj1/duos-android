package com.example.duos.ui.main.mypage.myprofile.frag

import android.os.Bundle
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
import com.example.duos.R
import com.example.duos.data.entities.EveryReviewsItem
import com.example.duos.data.remote.everyReviews.EveryReviewsResponse
import com.example.duos.data.remote.everyReviews.EveryReviewsService
import com.example.duos.databinding.FragmentEveryReviewBinding
import com.example.duos.ui.main.mypage.myprofile.EveryReviewRVAdapter
import com.example.duos.ui.main.mypage.myprofile.EveryReviewsItemView
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity

class EveryReviewFragment : Fragment(), EveryReviewsItemView {
    val TAG: String = "MyProfileFragment"

    //    private var reviewDatas = ArrayList<MyProfileReviewItem>()
    private var everyReviewDatas = ArrayList<EveryReviewsItem>()
    lateinit var binding: FragmentEveryReviewBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentEveryReviewBinding.inflate(inflater, container, false)
        Log.d(TAG, "Start_EveryReviewFragment")

        // userIdx 에 내 사용자 인덱스가 들어갈 수도, 파트너의 Idx 가 들어갈 수도
        EveryReviewsService.getEveryReviews(this, 1)

        return binding.root
    }

    override fun onGetEveryReviewsItemSuccess(everyReviewsResponse: EveryReviewsResponse) {
        everyReviewDatas.addAll(everyReviewsResponse.result)   // API 로 받아온 데이터 다 넣어주기 (더미데이터 넣듯이)

        initRecyclerView()


    }

    private fun initRecyclerView() {
        // 어댑터 연결
        val everyReviewRVAdapter = EveryReviewRVAdapter(everyReviewDatas)
        binding.playingReviewContentRv.adapter = everyReviewRVAdapter
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        // 어댑터 클릭 리스터
        everyReviewRVAdapter.clickPlayerReviewListener(object : EveryReviewRVAdapter.EveryReviewItemClickListener {
            override fun onItemClick(everyReveiwsItem: EveryReviewsItem) {
                val fragmentTransaction: FragmentTransaction = (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                        arguments = Bundle().apply {

                        }

                    })
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()    // commit() : FragmentManager가 이미 상태를 저장하지는 않았는지를 검사 이미 상태를 저장한 경우 IllegalStateExceptoion이라는 예외 던짐

                (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                (context as MyProfileActivity).findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility = View.VISIBLE
            }

        })
    }

    override fun onGetEveryReviewsItemFailure(code: Int, message: String) {
        Toast.makeText(context, "GetEveryReviewItemFailure", Toast.LENGTH_LONG).show()

    }


}


