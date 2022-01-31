package com.example.duos.ui.main.mypage.myprofile.frag

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.entities.MyProfileResult
import com.example.duos.data.entities.MyProfileReviewItem
import com.example.duos.data.remote.myProfile.MyProfileService
import com.example.duos.databinding.FragmentMyProfileBinding
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.ProfileReviewRVAdapter

class MyProfileFragment : Fragment(), ProfileListView {
    val TAG: String = "MyProfileFragment"
    private var myProfileReviewDatas = ArrayList<MyProfileReviewItem>()

    lateinit var binding: FragmentMyProfileBinding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMyProfileBinding.inflate(inflater, container, false)
        Log.d(TAG, "Start_MypageFragment")

        MyProfileService.myProfileInfo(this, 1)
        // 클릭리스너
        return binding.root
    }
//    lateinit var compositeDisposable: CompositeDisposable    // 메모리 누수 방지?



    override fun onGetMyProfileInfoSuccess(myProfile: MyProfileResult) {
        Glide.with(binding.myProfileImgIv.context)
            .load(myProfile.profileInfo.profileImgUrl)
            .into(binding.myProfileImgIv)
        binding.myNicknameTv.text = myProfile.profileInfo.nickname
        binding.myGenerationTv.text = myProfile.profileInfo.age
        binding.myLocationTv.text = myProfile.profileInfo.location
        binding.myGradeNumTv.text = myProfile.profileInfo.rating.toString()
        binding.myIntroductionTv.text = myProfile.profileInfo.introduction
        binding.careerYearNumTv.text = myProfile.profileInfo.experience
        binding.careerPlayedNumTv.text = myProfile.profileInfo.gamesCount.toString()
        binding.playingReviewCountTv.text = myProfile.profileInfo.reviewCount.toString()

        myProfileReviewDatas.addAll(myProfile.reviews)   // API 로 받아온 데이터 다 넣어주기 (더미데이터 넣듯이)
        val profileReviewRVAdapter = ProfileReviewRVAdapter(myProfileReviewDatas)
        // 리사이클러뷰에 어댑터 연결
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        //레이아웃 매니저 설정
        binding.playingReviewContentRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        profileReviewRVAdapter.clickPlayerReviewListener(
            object : ProfileReviewRVAdapter.PlayerReviewItemClickListener {
                override fun onItemClick(myProfileReviewItem: MyProfileReviewItem) {
                    val fragmentTransaction: FragmentTransaction = (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                        .replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
//                            Log.d(TAG,"나의 프로필 페이지에서 PartnerPage로 이동")
                            arguments = Bundle().apply {
                                // TODO : 후기를 작성한 writerIdx에 맞게 Fragment 이동 시 해당 Idx를 가진 회원의 프로필로 이동해야되 그 Idx 는 Intent 로 전달해도될 듯???

                            }
                        })
                    fragmentTransaction.addToBackStack(null)
                    // commit() : FragmentManager가 이미 상태를 저장하지는 않았는지를 검사 이미 상태를 저장한 경우 IllegalStateExceptoion이라는 예외 던짐
                    fragmentTransaction.commit()

                    // 상단 텍스트 변경
                    (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
                    (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                }
            })

        //         해당 회원의 모든 후기 보기 페이지로 이동
        binding.playingReviewCountTv.setOnClickListener {
            val profileNickname = binding.myNicknameTv.text.toString()
            val fragmentTransaction: FragmentTransaction = (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.my_profile_into_fragment_container_fc, EveryReviewFragment().apply {
                    arguments = Bundle().apply {

                        // TODO:  해당 회원의 Idx 값을 받아서 API 파싱해서 받는 곳에서 받으면 될듯.
                    }

                })

            fragmentTransaction.addToBackStack(null)// 해당 transaction 을 BackStack에 저장
            // commit(): FragmentManager가 이미 상태를 저장하지는 않았는지를 검사. 이미 상태를 저장한 경우, IllegalStateException 예외 던짐.
            fragmentTransaction.commit()

            // 상단 텍스트 변경
            val reviewCount = binding.playingReviewCountTv.text
            (context as MyProfileActivity).findViewById<TextView>(R.id.top_myProfile_tv).text = reviewCount.toString()
            (context as MyProfileActivity).findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
        }

    }

    override fun onGetMyProfileInfoFailure(code: Int, message: String) {
        Toast.makeText(context,"sdf",Toast.LENGTH_LONG).show()
    }

}

