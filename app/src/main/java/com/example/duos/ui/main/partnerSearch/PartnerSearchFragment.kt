package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.R
import com.example.duos.data.entities.PartnerSearchData
import com.example.duos.data.entities.RecommendedPartner
import com.example.duos.data.remote.partnerSearch.PartnerSearchService
import com.example.duos.databinding.FragmentPartnerSearchBinding
import com.example.duos.databinding.FragmentSignup03Binding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.frag.PlayerFragment
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging

class PartnerSearchFragment(): BaseFragment<FragmentPartnerSearchBinding>(FragmentPartnerSearchBinding::inflate), PartnerSearchView {
    private var recommendedPartnerDatas = ArrayList<RecommendedPartner>()
    private lateinit var partnerSearchRVGridAdapter:PartnerSearchRVGridAdapter
    private lateinit var partnerSearchRecommendedPartnerRv:RecyclerView

    companion object {
        fun newInstance(): PartnerSearchFragment = PartnerSearchFragment()
    }

    override fun onGetPartnerSearchDataSuccess(partnerSearchData: PartnerSearchData) {
        //        recommendedPartnerDatas.apply {
//            // 서울시 마포구로 필터링됐을 떄를 가정
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

        val userNickName = partnerSearchData.userNickname

        Glide.with(this).load(partnerSearchData.userProfileImageUrl)
            .apply(RequestOptions().circleCrop()).into(binding.partnerSearchMyProfileIv)    //이미지 원형으로 크롭

        binding.partnerSearchUserIdTv.text = userNickName

        recommendedPartnerDatas.clear()
        recommendedPartnerDatas.addAll(partnerSearchData.recommendedPartnerList)
        partnerSearchRecommendedPartnerRv.layoutManager = GridLayoutManager(context, 2)

        partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas)

        binding.partnerSearchRecommendedPartnerRv.adapter = partnerSearchRVGridAdapter

        partnerSearchRVGridAdapter.setRecommendedPartnerItemClickListener(object: PartnerSearchRVGridAdapter.recommendedPartnerItemClickListener{
            override fun onItemClick(recommendedPartner: RecommendedPartner) {
                // 파트너 세부 화면으로 이동
                Log.d("그리드","itemClick")
                var intent = Intent(activity, PartnerProfileActivity::class.java)
                startActivity(intent)
            }
        })
    }

    override fun onGetPartnerSearchDataFailure(code: Int, message: String) {
        Toast.makeText(activity,"code: $code, message: $message", Toast.LENGTH_LONG)
    }

    override fun initAfterBinding() {
        partnerSearchRecommendedPartnerRv = binding.partnerSearchRecommendedPartnerRv
        partnerSearchRecommendedPartnerRv.layoutManager = GridLayoutManager(context, 2)
        partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas)
        binding.partnerSearchRecommendedPartnerRv.adapter = partnerSearchRVGridAdapter

        PartnerSearchService.partnerSearchData(this, 0)

        binding.partnerSearchFilteringIc.setOnClickListener{
            startActivity(Intent(activity, PartnerFilterActivity::class.java))
        }

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