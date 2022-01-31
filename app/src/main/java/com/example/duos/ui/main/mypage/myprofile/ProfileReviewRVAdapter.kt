package com.example.duos.ui.main.mypage.myprofile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.data.entities.MyProfileReviewItem
import com.example.duos.databinding.MyPlayingReviewItemBinding

// 어댑터의 parameter : 데이터리스트. 이 어댑터의 ViewHolder 상속받기
class ProfileReviewRVAdapter(private val myProfileReviewItemList: ArrayList<MyProfileReviewItem>) :
    RecyclerView.Adapter<ProfileReviewRVAdapter.ViewHolder>() {

    interface PlayerReviewItemClickListener {
        fun onItemClick(myProfileReviewItem: MyProfileReviewItem)
    }

    //  리스너 객체를 전달받는 함수와 리스터 객체를 저장할 변수
    private lateinit var mItemClickListener: PlayerReviewItemClickListener

    // 외부에서 리스너 객체를 전달 받을 함수
    fun clickPlayerReviewListener(itemClickListener: PlayerReviewItemClickListener) {
        mItemClickListener = itemClickListener
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    // ViewHolder 생성. (아이템 뷰 객체를 binding해서 뷰 홀더에 던져줌)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProfileReviewRVAdapter.ViewHolder {
        val binding: MyPlayingReviewItemBinding =
            MyPlayingReviewItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // ViewHolder에 데이터를 binding (리사이클러뷰의 아이템(데이터)이 바뀔 때마다 실행됨)
    override fun onBindViewHolder(holder: ProfileReviewRVAdapter.ViewHolder, position: Int) {
        holder.bind(myProfileReviewItemList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(myProfileReviewItemList[position]) }
    }

    // 데이터 리스트의 크기
    override fun getItemCount(): Int = myProfileReviewItemList.size

    // 내부클래스 ViewHolder. bind 메서드 정의(리사이클러뷰의 아이템에 데이터리스트의 데이터 연결)
    inner class ViewHolder(val binding: MyPlayingReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(myProfileReviewItem: MyProfileReviewItem) {
            binding.playerReviewNicknameTv.text = myProfileReviewItem.writerNickname    /*writerNickname*/
            Glide.with(binding.profileImgIv.context)                                    /*writerProfileImgUrl*/
                .load(myProfileReviewItem.writerProfileImgUrl)
                .into(binding.profileImgIv)
            binding.playerGradeTv.text = myProfileReviewItem.rating.toString()          /*rating*/
            binding.reviewDateTv.text = myProfileReviewItem.date                        /*date*/
            binding.reviewContentTv.text = myProfileReviewItem.reviewContent            /*reviewContent*/

            //TODO : rating 에 있는 실수형 데이터를 저기 rating bar 에 binding 시켜서 별 보이게 하면 될듯.

        }
    }
}
