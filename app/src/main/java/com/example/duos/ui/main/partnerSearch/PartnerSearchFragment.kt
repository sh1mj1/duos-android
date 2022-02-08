package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.data.entities.PartnerSearchData
import com.example.duos.data.entities.RecommendedPartner
import com.example.duos.data.local.RecommendedPartnerDatabase
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.partnerSearch.PartnerSearchService
import com.example.duos.databinding.FragmentPartnerSearchBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.utils.getCheckUserAppliedPartnerFilterMoreThanOnce
import com.example.duos.utils.getLastUpdatedDate
import com.example.duos.utils.saveLastUpdatedDate
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class PartnerSearchFragment(): BaseFragment<FragmentPartnerSearchBinding>(FragmentPartnerSearchBinding::inflate), PartnerSearchView {
    private var recommendedPartnerDatas = ArrayList<RecommendedPartner>()
    private lateinit var partnerSearchRVGridAdapter:PartnerSearchRVGridAdapter
    private lateinit var partnerSearchRecommendedPartnerRv:RecyclerView
    var userIdx: Int = 1
    lateinit var recommendedPartnerDatabase: RecommendedPartnerDatabase

    companion object {
        fun newInstance(): PartnerSearchFragment = PartnerSearchFragment()
    }

    override fun onGetPartnerSearchDataLoading() {
//        progressON()
        Log.d("로딩중","파트너 추천 api")
        //Handler(Looper.getMainLooper()).postDelayed(Runnable { progressOFF() }, 3500)
    }

    override fun onGetPartnerSearchDataSuccess(partnerSearchData: PartnerSearchData) {
         // 서울시 마포구로 필터링됐을 떄를 가정
        //        recommendedPartnerDatas.apply {
//            // FLO clone coding 211110자 커밋 참고했음
//            add(RecommendedPartner(R.drawable.partner_profile_img_1, "서울시 마포구", "구력 1년", "berlinalena", "30대", 4.8, 11))
//            add(RecommendedPartner(R.drawable.partner_profile_img_2, "서울시 서대문구", "구력 3년", "time456 ","30대", 4.7, 21))
//            add(RecommendedPartner(R.drawable.partner_profile_img_3, "서울시 마포구", "구력 2년", "qop123", "20대", 4.7, 15))
//            add(RecommendedPartner(R.drawable.partner_profile_img_4, "서울시 마포구", "구력 1년", "Olivia", "30대", 4.6, 8))
//            add(RecommendedPartner(R.drawable.partner_profile_img_2, "서울시 마포구", "구력 1년", "berlinalena", "30대", 4.8, 11))
//            add(RecommendedPartner(R.drawable.partner_profile_img_1, "서울시 서대문구", "구력 3년", "time456 ","30대", 4.7, 21))
//            add(RecommendedPartner(R.drawable.partner_profile_img_4, "서울시 마포구", "구력 2년", "qop123", "20대", 4.7, 15))
//            add(RecommendedPartner(R.drawable.partner_profile_img_3, "서울시 마포구", "구력 1년", "Olivia", "30대", 4.6, 8))
//            add(RecommendedPartner(R.drawable.partner_profile_img_1, "서울시 마포구", "구력 1년", "berlinalena", "30대", 4.8, 11))
//            add(RecommendedPartner(R.drawable.partner_profile_img_2, "서울시 마포구", "구력 3년", "time456 ","30대", 4.7, 21))
//        }

        Log.d("get_recommendedPartnerList","ongetSuccess")
        progressOFF()

        val userNickName = partnerSearchData.userNickname

        recommendedPartnerDatabase.recommendedPartnerDao().deleteAll()
        var recommendedPartnerList = partnerSearchData.recommendedPartnerList
        for(i: Int in 0 .. recommendedPartnerList.size-1){
            recommendedPartnerDatabase.recommendedPartnerDao().insert(recommendedPartnerList[i])
        }

        recommendedPartnerDatas.clear()
        recommendedPartnerDatas.addAll(partnerSearchData.recommendedPartnerList)
        partnerSearchRecommendedPartnerRv.layoutManager = GridLayoutManager(context, 2)

        partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas)

        binding.partnerSearchRecommendedPartnerRv.adapter = partnerSearchRVGridAdapter

        partnerSearchRVGridAdapter.setRecommendedPartnerItemClickListener(object: PartnerSearchRVGridAdapter.recommendedPartnerItemClickListener{
            override fun onItemClick(recommendedPartner: RecommendedPartner) {
                // 파트너 세부 화면으로 이동
                Log.d("그리드","itemClick")
                var intent = Intent(activity, MyProfileActivity::class.java)
                intent.apply {
                    this.putExtra("isFromSearch", true)
                    this.putExtra("partnerUserIdx", recommendedPartner.partnerIdx)
                }
                startActivity(intent)
            }
        })
    }

    override fun onGetPartnerSearchDataFailure(code: Int, message: String) {
        Toast.makeText(activity,"code: $code, message: $message", Toast.LENGTH_LONG)
    }

    override fun initAfterBinding() {

        recommendedPartnerDatabase = RecommendedPartnerDatabase.getInstance(requireContext())!!

        // 로그인할 때 저장해둔 userIdx를 불러와서, userIdx로 user의 닉네임과 프로필이미지를 룸디비에서 찾아 load시키도록 수정해야함.



        //수

        //정

        //필

        //요

//        val userDB = UserDatabase.getInstance(requireContext())!!
//        val userNickName = userDB.userDao().
//
//        Glide.with(this).load(partnerSearchData.userProfileImageUrl)
//            .apply(RequestOptions().circleCrop()).into(binding.partnerSearchMyProfileIv)    //이미지 원형으로 크롭

        //수

        //정

        //필

        //요


        // 임시로 사용자의 프로필이미지와 닉네임 세팅함
        Glide.with(this).load("https://duosimage.s3.ap-northeast-2.amazonaws.com/profile/5.jpg")
            .apply(RequestOptions().circleCrop()).into(binding.partnerSearchMyProfileIv)    //이미지 원형으로 크롭
        binding.partnerSearchUserIdTv.text = "tennis1010"

        partnerSearchRecommendedPartnerRv = binding.partnerSearchRecommendedPartnerRv
        partnerSearchRecommendedPartnerRv.layoutManager = GridLayoutManager(context, 2)
        partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas)
        binding.partnerSearchRecommendedPartnerRv.adapter = partnerSearchRVGridAdapter





        if(getCheckUserAppliedPartnerFilterMoreThanOnce()){ //필터를 한번이라도 적용한 적이 있을 때
            // 룸디비에 저장되어있던 파트너 추천 목록 데이터를 가져옴
            recommendedPartnerDatas.clear()
            var storedRecommendedPartnerList = recommendedPartnerDatabase.recommendedPartnerDao().getRecommendedPartnerList()
            recommendedPartnerDatas.addAll(storedRecommendedPartnerList)
            partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas)
            binding.partnerSearchRecommendedPartnerRv.adapter = partnerSearchRVGridAdapter
            Log.d("필터를 한번이라도 적용한 적 있음", " ")

        }else{  // 필터를 한 번도 적용한 적이 없는 사용자일때
            //날짜 확인
            var currentTime = System.currentTimeMillis()
            var todayDate = Date(currentTime)
            var dateFormatter = SimpleDateFormat("yyyy-MM-dd")
            var formattedTodayDate = dateFormatter.format(todayDate)

            if(getLastUpdatedDate().equals(formattedTodayDate)){    // 오늘 파트너 추천을 받은 적이 있는지 확인하고 있다면 룸디비에서 데이터 가져옴
                Log.d("파트너 추천 받은 적 있음", getLastUpdatedDate())
                recommendedPartnerDatas.clear()
                var storedRecommendedPartnerList = recommendedPartnerDatabase.recommendedPartnerDao().getRecommendedPartnerList()
                recommendedPartnerDatas.addAll(storedRecommendedPartnerList)
            }else{
                PartnerSearchService.partnerSearchData(this, userIdx)
                saveLastUpdatedDate(formattedTodayDate)
                Log.d("saved lastUpdatedDate", getLastUpdatedDate())
            }
        }

        partnerSearchRVGridAdapter.setRecommendedPartnerItemClickListener(object: PartnerSearchRVGridAdapter.recommendedPartnerItemClickListener{
            override fun onItemClick(recommendedPartner: RecommendedPartner) {
                // 파트너 세부 화면으로 이동
                Log.d("그리드","itemClick")
                var intent = Intent(activity, PartnerProfileActivity::class.java)
                startActivity(intent)
            }
        })



        binding.partnerSearchFilteringIc.setOnClickListener{
            startActivity(Intent(activity, PartnerFilterActivity::class.java))
        }

//        // fcm 등록f토큰 받아오기
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }

        // fcm 등록토큰 받아오기
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token // FCM 등록 토큰 get
            val token = task.result

            // Log and toast
            //val msg = getString(R.string.msg_token_fmt, token)
            //Log.d(TAG, msg)
            Log.d("토큰 확인", token)
            //Toast.makeText(context, token, Toast.LENGTH_LONG).show()
        })
    }
}