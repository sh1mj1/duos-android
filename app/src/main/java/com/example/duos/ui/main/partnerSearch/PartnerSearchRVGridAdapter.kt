package com.example.duos.ui.main.partnerSearch

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.duos.data.entities.RecommendedPartner
import com.example.duos.databinding.ItemRecommendedPartnerGridBinding
import kotlin.math.round

import kotlin.math.roundToInt

class PartnerSearchRVGridAdapter(val recommendedPartnerList: ArrayList<RecommendedPartner>): RecyclerView.Adapter<PartnerSearchRVGridAdapter.ViewHolder>() {
    //private val recommendedPartners = ArrayList<RecommendedPartner>()
    private lateinit var context: Context
    // 클릭 인터페이스 정의
    interface recommendedPartnerItemClickListener{
        fun onItemClick(recommendedPartner: RecommendedPartner)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: recommendedPartnerItemClickListener

    fun setRecommendedPartnerItemClickListener(itemClickListener: recommendedPartnerItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemRecommendedPartnerGridBinding = ItemRecommendedPartnerGridBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        context = viewGroup.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(recommendedPartnerList[position])
    }

    override fun getItemCount(): Int = recommendedPartnerList.size

//    fun addRecommendedPartners(recommendedPartners: ArrayList<RecommendedPartner>){
//        this.recommendedPartners.clear()
//        this.recommendedPartners.addAll(recommendedPartners)
//
//        notifyDataSetChanged()
//    }

    inner class ViewHolder(val binding: ItemRecommendedPartnerGridBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(recommendedPartner : RecommendedPartner){

            Glide.with(context).load(recommendedPartner.profileImg).into(binding.partnerSearchRecommendedPartnerIv)
            binding.partnerSearchRecommendedPartnerLocationTv.text = recommendedPartner.location
            binding.partnerSearchRecommendedPartnerBallCapabilityTv.text = recommendedPartner.ballCapacity
            binding.partnerSearchRecommendedPartnerIdTv.text = recommendedPartner.id

            // age값으로 나이대 계산
            var age = recommendedPartner.age
            var ageRange = ""
            if (age != null) {
                ageRange = (round(age.toFloat()/10)*10).roundToInt().toString() + "대"
            }
            binding.partnerSearchRecommendedPartnerAgeTv.text = ageRange
            binding.partnerSearchRecommendedPartnerStarRatingTv.text = recommendedPartner.starRating.toString()
            binding.partnerSearchRecommendedPartnerReviewCountTv.text = recommendedPartner.reviewCount.toString()
        }
    }
}