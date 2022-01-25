package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.util.TypedValue
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.duos.data.entities.PartnerSearchData
import com.example.duos.data.entities.RecommendedPartner
import com.example.duos.data.remote.partnerSearch.PartnerSearchService
import com.example.duos.databinding.FragmentPartnerSearchBinding
import com.example.duos.ui.BaseFragment

class PartnerSearchFragment(): BaseFragment<FragmentPartnerSearchBinding>(FragmentPartnerSearchBinding::inflate), PartnerSearchView {
    private var recommendedPartnerDatas = ArrayList<RecommendedPartner>()
    private lateinit var partnerSearchRVGridAdapter:PartnerSearchRVGridAdapter

    override fun initAfterBinding() {
        PartnerSearchService.partnerSearchData(this, 0)
        binding.partnerSearchFilteringIc.setOnClickListener{
            startActivity(Intent(activity, PartnerFilterActivity::class.java))
        }
    }

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

        Log.d("get recommendedPartnerList","ongetSuccess")

        val userNickName = partnerSearchData.userNickname

        Glide.with(this).load(partnerSearchData.userProfileImageUrl)
            .apply(RequestOptions().circleCrop()).into(binding.partnerSearchMyProfileIv)    //이미지 원형으로 크롭

        binding.partnerSearchUserIdTv.text = userNickName

        recommendedPartnerDatas.addAll(partnerSearchData.recommendedPartnerList)
        var partnerSearchRecommendedPartnerRv = binding.partnerSearchRecommendedPartnerRv
        //partnerSearchRecommendedPartnerRv.layoutManager = GridLayoutManager(context, 2)

        var gap : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, context?.resources?.displayMetrics).toInt()

        partnerSearchRecommendedPartnerRv.layoutManager = object : GridLayoutManager(context, 2){
            override fun checkLayoutParams(lp: RecyclerView.LayoutParams) : Boolean {
                // force width of viewHolder to be a fraction of RecyclerViews
                // this will override layout_width from xml
                lp.width = (width - gap) / 2
                return true
            }
        }

//        val deviceWidthPixels = resources.displayMetrics.widthPixels
//        var space : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, context?.resources?.displayMetrics).toInt()
//        val widthPixels = (deviceWidthPixels - space * 3) / 2
//
//        partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas, widthPixels)

        partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas)

        binding.partnerSearchRecommendedPartnerRv.adapter = partnerSearchRVGridAdapter

        var marginStart : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, context?.resources?.displayMetrics).toInt()
        var marginTop : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F, context?.resources?.displayMetrics).toInt()
        var marginBottom : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, context?.resources?.displayMetrics).toInt()

        binding.partnerSearchRecommendedPartnerRv.addItemDecoration(GridSpacingItemDecoration(marginStart, marginTop, marginBottom))

        partnerSearchRVGridAdapter.setRecommendedPartnerItemClickListener(object:PartnerSearchRVGridAdapter.recommendedPartnerItemClickListener{
            override fun onItemClick(recommendedPartner: RecommendedPartner) {
                // 파트너 세부 화면으로 이동
            }
        })

//        var bottomNavigationBarHeight : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72F, context?.resources?.displayMetrics).toInt()   // 72는 bottomNavigationBar 높이
//
//        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
//        var deviceNavigationBarHeight = 0
//        if (resourceId > 0) {
//            deviceNavigationBarHeight = resources.getDimensionPixelSize(resourceId)
//        }
//        var deviceHeight = deviceNavigationBarHeight + bottomNavigationBarHeight
//        binding.partnerSearchLayout.setPadding(0,0,0,deviceHeight)

//        for (partnerSearchData in partnerSearchDataList){
//            val userProfileImageUrl = partnerSearchData.userProfileImageUrl
//            val userNickName = partnerSearchData.userNickname
//
//            val userProfileImageUri = Uri.parse(userProfileImageUrl)
//
//            binding.partnerSearchMyProfileIv.setImageURI(userProfileImageUri)
//            binding.partnerSearchUserIdTv.text = userNickName
//
//            recommendedPartnerDatas.addAll(partnerSearchData.recommendedPartnerList)
//            var partnerSearchRecommendedPartnerRv = binding.partnerSearchRecommendedPartnerRv
//            partnerSearchRecommendedPartnerRv.layoutManager = GridLayoutManager(context, 2)
//
//            val deviceWidthPixels = resources.displayMetrics.widthPixels
//            var space : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, context?.resources?.displayMetrics).toInt()
//            val widthPixels = (deviceWidthPixels - space * 3) / 2
//
//            partnerSearchRVGridAdapter = PartnerSearchRVGridAdapter(recommendedPartnerDatas, widthPixels)
//
//            binding.partnerSearchRecommendedPartnerRv.adapter = partnerSearchRVGridAdapter
//
//            var marginStart : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, context?.resources?.displayMetrics).toInt()
//            var marginTop : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20F, context?.resources?.displayMetrics).toInt()
//            var marginBottom : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 16F, context?.resources?.displayMetrics).toInt()
//
//            binding.partnerSearchRecommendedPartnerRv.addItemDecoration(GridSpacingItemDecoration(marginStart, marginTop, marginBottom))
//
////        var bottomNavigationBarHeight : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 72F, context?.resources?.displayMetrics).toInt()   // 72는 bottomNavigationBar 높이
////
////        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
////        var deviceNavigationBarHeight = 0
////        if (resourceId > 0) {
////            deviceNavigationBarHeight = resources.getDimensionPixelSize(resourceId)
////        }
////        var deviceHeight = deviceNavigationBarHeight + bottomNavigationBarHeight
////        binding.partnerSearchLayout.setPadding(0,0,0,deviceHeight)
//        }
//        partnerSearchRVGridAdapter.setRecommendedPartnerItemClickListener(object:PartnerSearchRVGridAdapter.recommendedPartnerItemClickListener{
//            override fun onItemClick(recommendedPartner: RecommendedPartner) {
//                // 파트너 세부 화면으로 이동
//            }
//        }
//        )
    }

    override fun onGetPartnerSearchDataFailure(code: Int, message: String) {
        showToast("code : $code, message : $message")
    }
}