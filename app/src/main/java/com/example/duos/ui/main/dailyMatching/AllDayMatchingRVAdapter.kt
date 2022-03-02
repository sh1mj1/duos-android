package com.example.duos.ui.main.dailyMatching

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.dailyMatching.DailyMatching
import com.example.duos.databinding.ItemFragmentDailyMatchingFragmentBinding

class AllDayMatchingRVAdapter(private val dailyMatching : ArrayList<DailyMatching>) : RecyclerView.Adapter<AllDayMatchingRVAdapter.ViewHolder>() {


    // 클릭 인터페이스 정의
    interface MyItemClickListener{

    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): AllDayMatchingRVAdapter.ViewHolder {
        val binding: ItemFragmentDailyMatchingFragmentBinding = ItemFragmentDailyMatchingFragmentBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(dailyMatching[position], position)

    }

    override fun getItemCount(): Int = dailyMatching.size



    // 뷰홀더
    inner class ViewHolder(val binding: ItemFragmentDailyMatchingFragmentBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(dailyMatching : DailyMatching, position: Int){

            GlideApp.with(binding.dailyMatchingPostPreviewProfileImageIv.context)
                .load(dailyMatching.userProfileImage)
                .into(binding.dailyMatchingPostPreviewProfileImageIv)

        }
    }
}