package com.example.duos.ui.adapter

import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.data.entities.ReviewResDto
import com.example.duos.databinding.MyPlayingReviewItemBinding
import com.example.duos.ui.adapter.PartnerProfileReviewRVAdapter.ViewHolder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

// 어댑터의 parameter : 데이터리스트. 이 어댑터의 ViewHolder 상속받기
class PartnerProfileReviewRVAdapter(private val partnerProfileReviewItemList: ArrayList<ReviewResDto>) :
    RecyclerView.Adapter<ViewHolder>() {

    val TAG = "PartnerProfileReviewRVAdapter"

    interface PlayerReviewItemClickListener {
        fun onItemClick(partnerProfileReviewItem: ReviewResDto)
    }

    //  리스너 객체를 전달받는 함수와 리스터 객체를 저장할 변수
    private lateinit var mItemClickListener: PlayerReviewItemClickListener

    // 외부에서 리스너 객체를 전달 받을 함수
    fun clickPlayerReviewListener(itemClickListener: PlayerReviewItemClickListener) {
        mItemClickListener = itemClickListener
    }

    // ViewHolder 생성. (아이템 뷰 객체를 binding해서 뷰 홀더에 던져줌)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: MyPlayingReviewItemBinding =
            MyPlayingReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // ViewHolder에 데이터를 binding (리사이클러뷰의 아이템(데이터)이 바뀔 때마다 실행됨)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        if (getItemCount ()!= 0) {
        Log.d(TAG, "partnerProfileReviewItemList 가 null 이 아님")
        holder.bind(partnerProfileReviewItemList[position])
        holder.binding.profileImgCv.setOnClickListener {
            mItemClickListener.onItemClick(partnerProfileReviewItemList[position])
        }
        holder.binding.playerReviewNicknameTv.setOnClickListener {
            mItemClickListener.onItemClick(partnerProfileReviewItemList[position])
        }
        holder.binding.playerGradeRb.setOnClickListener {
            mItemClickListener.onItemClick(partnerProfileReviewItemList[position])
        }
        holder.binding.playerGradeTv.setOnClickListener {
            mItemClickListener.onItemClick(partnerProfileReviewItemList[position])
        }

//        } else {
//            Log.d(TAG, "partnerProfileReviewItemList 가 null 임")
//            holder.noticebind()
//        }


    }

    // 데이터 리스트의 크기          /
    override fun getItemCount(): Int = partnerProfileReviewItemList.size

    // 내부클래스 ViewHolder. bind 메서드 정의(리사이클러뷰의 아이템에 데이터리스트의 데이터 연결)
    inner class ViewHolder(val binding: MyPlayingReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(partnerProfileReviewItem: ReviewResDto) {
            /* writerIdx*/
            binding.playerGradeTv.text = toRatingStr(partnerProfileReviewItem.rating!!)          /*rating*/
            binding.playerGradeRb.rating = partnerProfileReviewItem.rating
            binding.reviewContentTv.text = partnerProfileReviewItem.reviewContent            /*reviewContent*/
//            binding.reviewDateTv.text = partnerProfileReviewItem.createdAt
            binding.reviewDateTv.text = toDateStr(partnerProfileReviewItem.createdAt)       /*createdAt*/
            binding.playerReviewNicknameTv.text = partnerProfileReviewItem.writerNickname    /*writerNickname*/
            Glide.with(binding.profileImgIv.context)                                    /*writerProfileImgUrl*/
                .load(partnerProfileReviewItem.writerProfilePhotoUrl)
                .into(binding.profileImgIv)
        }
    }
    fun toRatingStr(ratingFloat : Float): String{
        val ratingStr = String.format("%.0f", ratingFloat*10).toDouble()/10
        return ratingStr.toString()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDateStr(date : String):String{
        val dateFromApi = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val dateDifference = ChronoUnit.DAYS.between(dateFromApi, LocalDateTime.now())
        var dateStr : String = ""
        if(dateDifference <= 0){    /* 오늘이면*/
            if(dateFromApi.hour <= 12){
                dateStr = "오늘 오전 ${dateFromApi.hour}시"
            }else{
                dateStr = "오늘 오후 ${dateFromApi.hour}시"
            }

        } else if(dateDifference <= 1){
            if(dateFromApi.hour <= 12){
                dateStr = "어제 오전 ${dateFromApi.hour}시"
            }else{
                dateStr = "어제 오후 ${dateFromApi.hour - 12}시"
            }
        } else{
            dateStr = dateFromApi.toLocalDate().toString()
            //DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm:ss.SSSX" )
        }

        return dateStr
    }


}
