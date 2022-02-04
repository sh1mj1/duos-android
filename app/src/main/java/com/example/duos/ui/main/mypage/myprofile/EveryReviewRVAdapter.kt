package com.example.duos.ui.main.mypage.myprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.data.entities.EveryReviewsItem
import com.example.duos.databinding.MyPlayingReviewItemBinding

// 어댑터의 parameter : 데이터리스트. 이 어댑터의 ViewHolder 상속받기
class EveryReviewRVAdapter(private val everyReveiwsItem: ArrayList<EveryReviewsItem>) :
    RecyclerView.Adapter<EveryReviewRVAdapter.ViewHolder>() {

    interface EveryReviewItemClickListener {
        fun onItemClick(everyReveiwsItem: EveryReviewsItem)
    }

    //  리스너 객체를 전달받는 함수와 리스터 객체를 저장할 변수
    private lateinit var mItemClickListener: EveryReviewItemClickListener

    // 외부에서 리스너 객체를 전달 받을 함수
    fun clickPlayerReviewListener(itemClickListener: EveryReviewItemClickListener) {
        mItemClickListener = itemClickListener
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ViewHolder 생성. (아이템 뷰 객체를 binding해서 뷰 홀더에 던져줌)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EveryReviewRVAdapter.ViewHolder {
        val binding: MyPlayingReviewItemBinding =
            MyPlayingReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // ViewHolder에 데이터를 binding (리사이클러뷰의 아이템(데이터)이 바뀔 때마다 실행됨)
    override fun onBindViewHolder(holder: EveryReviewRVAdapter.ViewHolder, position: Int) {
        holder.bind(everyReveiwsItem[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(everyReveiwsItem[position]) }
    }

    // 데이터 리스트의 크기
    override fun getItemCount(): Int = everyReveiwsItem.size

    // 내부클래스 ViewHolder. bind 메서드 정의(리사이클러뷰의 아이템에 데이터리스트의 데이터 연결)
    inner class ViewHolder(val binding: MyPlayingReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(everyReviewsItem: EveryReviewsItem) {
            binding.playerReviewNicknameTv.text = everyReviewsItem.writerNickname    /*writerNickname*/
            Glide.with(binding.profileImgIv.context)                                    /*writerProfilePhotoUrl*/
                .load(everyReviewsItem.writerProfilePhotoUrl)
                .into(binding.profileImgIv)
            binding.playerGradeTv.text = everyReviewsItem.rating.toString()          /*rating*/
            val playerGradeRate = binding.playerGradeTv.text.toString()
            binding.playerGradeRb.rating = playerGradeRate.toFloat()
            binding.reviewDateTv.text = everyReviewsItem.createdAt                      /*createdAt*/
            //TODO  이제 위 createedAt을 안드로이드 기기에서 감지하는 시간에 맞게 계산해서 어제, 오늘 등으로 만들어서 조건문에 따라 다르게 만들고
            // 형식 파싱하기
            
            binding.reviewContentTv.text = everyReviewsItem.reviewContent            /*reviewContent*/

        }
    }
}
