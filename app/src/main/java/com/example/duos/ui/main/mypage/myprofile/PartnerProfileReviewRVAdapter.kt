package com.example.duos.ui.main.mypage.myprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.data.entities.PartnerProfileReviewItem
import com.example.duos.data.entities.ReviewResDto
import com.example.duos.databinding.MyPlayingReviewItemBinding

// 어댑터의 parameter : 데이터리스트. 이 어댑터의 ViewHolder 상속받기
class PartnerProfileReviewRVAdapter(private val partnerProfileReviewItemList: ArrayList<ReviewResDto>) :
    RecyclerView.Adapter<PartnerProfileReviewRVAdapter.ViewHolder>() {

    interface PlayerReviewItemClickListener {
        fun onItemClick(partnerProfileReviewItem: ReviewResDto)
    }

    //  리스너 객체를 전달받는 함수와 리스터 객체를 저장할 변수
    private lateinit var mItemClickListener: PlayerReviewItemClickListener

    // 외부에서 리스너 객체를 전달 받을 함수
    fun clickPlayerReviewListener(itemClickListener: PlayerReviewItemClickListener) {
        mItemClickListener = itemClickListener
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ViewHolder 생성. (아이템 뷰 객체를 binding해서 뷰 홀더에 던져줌)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PartnerProfileReviewRVAdapter.ViewHolder {
        val binding: MyPlayingReviewItemBinding =
            MyPlayingReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // ViewHolder에 데이터를 binding (리사이클러뷰의 아이템(데이터)이 바뀔 때마다 실행됨)
    override fun onBindViewHolder(holder: PartnerProfileReviewRVAdapter.ViewHolder, position: Int) {
        holder.bind(partnerProfileReviewItemList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(partnerProfileReviewItemList[position]) }
    }

    // 데이터 리스트의 크기
    override fun getItemCount(): Int = partnerProfileReviewItemList.size

    // 내부클래스 ViewHolder. bind 메서드 정의(리사이클러뷰의 아이템에 데이터리스트의 데이터 연결)
    inner class ViewHolder(val binding: MyPlayingReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(partnerProfileReviewItem: ReviewResDto) {
            /* writerIdx*/
            binding.playerGradeTv.text = partnerProfileReviewItem.rating.toString()          /*rating*/
            var playerGradeRate = binding.playerGradeTv.text.toString()
            binding.playerGradeRb.rating = playerGradeRate.toFloat()
            binding.reviewContentTv.text = partnerProfileReviewItem.reviewContent            /*reviewContent*/
            binding.reviewDateTv.text = partnerProfileReviewItem.createdAt                        /*createdAt*/
            binding.playerReviewNicknameTv.text = partnerProfileReviewItem.writerNickname    /*writerNickname*/
            Glide.with(binding.profileImgIv.context)                                    /*writerProfileImgUrl*/
                .load(partnerProfileReviewItem.writerProfilePhotoUrl)
                .into(binding.profileImgIv)

        }
    }
}
