package com.example.duos.ui.main.mypage.myprofile

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.data.entities.ReviewResDto
import com.example.duos.databinding.MyPlayingReviewItemBinding
import com.example.duos.ui.main.mypage.myprofile.PartnerProfileReviewRVAdapter.ViewHolder

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

    /*
    @Override
public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, @NonNull List<Object> payloads) {
    if (payloads.isEmpty()) {
        super.onBindViewHolder(holder, position, payloads);
    }else {
        for (Object payload : payloads) {
            if (payload instanceof String) {
                String type = (String) payload;
                if (TextUtils.equals(type, "click") && holder instanceof TextHolder) {
                    TextHolder textHolder = (TextHolder) holder;
                    textHolder.mFavorite.setVisibility(View.VISIBLE);
                    textHolder.mFavorite.setAlpha(0f);
                    textHolder.mFavorite.setScaleX(0f);
                    textHolder.mFavorite.setScaleY(0f);

                    //animation
                    textHolder.mFavorite.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .alpha(1f)
                            .setInterpolator(new OvershootInterpolator())
                            .setDuration(300);

                }
            }
        }
    }
     */

    // 데이터 리스트의 크기          /
    override fun getItemCount(): Int = partnerProfileReviewItemList.size

    // 내부클래스 ViewHolder. bind 메서드 정의(리사이클러뷰의 아이템에 데이터리스트의 데이터 연결)
    inner class ViewHolder(val binding: MyPlayingReviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(partnerProfileReviewItem: ReviewResDto) {
//            binding.partnerProfileReviewItemListNonNullCl.visibility = View.VISIBLE /* 후기 보이고, 후기 없음 안 보이기*/
//            binding.partnerProfileReviewItemListNullCl.visibility = View.GONE

            /* writerIdx*/
            binding.playerGradeTv.text = partnerProfileReviewItem.rating.toString()          /*rating*/
            binding.playerGradeRb.rating = partnerProfileReviewItem.rating!!
            binding.reviewContentTv.text = partnerProfileReviewItem.reviewContent            /*reviewContent*/
            binding.reviewDateTv.text = partnerProfileReviewItem.createdAt                        /*createdAt*/
            binding.playerReviewNicknameTv.text = partnerProfileReviewItem.writerNickname    /*writerNickname*/
            Glide.with(binding.profileImgIv.context)                                    /*writerProfileImgUrl*/
                .load(partnerProfileReviewItem.writerProfilePhotoUrl)
                .into(binding.profileImgIv)
        }

//        fun noticebind() {
//            binding.partnerProfileReviewItemListNonNullCl.visibility = View.GONE    /* 후기 없음 보이고, 후기 안 보이기*/
//            binding.partnerProfileReviewItemListNullCl.visibility = View.VISIBLE
//        }
    }
}
