package com.example.duos.ui.main.mypage.myprofile

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.Review
import com.example.duos.databinding.ActivityMyprofileBinding

class MyProfileActivity : AppCompatActivity() {


    private var reviewDatas = ArrayList<Review>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMyprofileBinding.inflate(layoutInflater)

        // 더미데이터 넣기. 현재 리사이클러 뷰의 데이터 4개
        reviewDatas.apply {
            add(Review(R.drawable.tennis_racket_img_4, "5.0", "koko0311_1","처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"))
            add(Review(R.drawable.tennis_racket_img_4, "3.0", "koko0311_2","처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"))
            add(Review(R.drawable.tennis_racket_img_4, "4.0", "koko0311_3","처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"))
            add(Review(R.drawable.tennis_racket_img_4, "5.0", "koko0311_4","처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라 처음 뵀는데 어색하지 않고, 즐겁게 플레이 했습니다. 같은 동네에 사시는 분이라"))

        }
        // 더미 데이터와 Adapter 연결
        val profileReviewRVAdapter = ProfileReviewRVAdapter(reviewDatas)
        // 리사이클러뷰에 어댑터 연결
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        //레이아웃 매니저 설정
        // applicationContext??????
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(applicationContext,
            LinearLayoutManager.VERTICAL, false)

//        playing_review_content_rv
        setContentView(binding.root)


    }
}