package com.example.duos.ui.main.dailyMatching

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.databinding.ItemDailyMatchingTimeSelectorBinding
import android.R
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.util.SparseBooleanArray
import android.view.View
import androidx.core.content.ContextCompat
import java.util.*
import kotlin.collections.ArrayList


class DailyMatchingTimeSelectRVAdapter(val time: Int) :
    RecyclerView.Adapter<DailyMatchingTimeSelectRVAdapter.ViewHolder>() {

    private val selectedStatus = SparseBooleanArray()
    var startTime : Int = time
    var startPosition: Int = -1

    // 클릭 인터페이스 정의
    interface MyItemClickListener {
        fun onClickItem(startPosition: Int, endPosition: Int)
        fun onResetItem(startPosition: Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }


    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): DailyMatchingTimeSelectRVAdapter.ViewHolder {
        val binding: ItemDailyMatchingTimeSelectorBinding =
            ItemDailyMatchingTimeSelectorBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )


        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(position)

    }

    override fun getItemCount(): Int = 24 - startTime

    fun updateStartTime(time: Int) {
        startTime = time
        for (i in 0 until itemCount){
            selectedStatus.put(i, false)
        }
        notifyDataSetChanged()

    }


    // 뷰홀더
    inner class ViewHolder(val binding: ItemDailyMatchingTimeSelectorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {

            binding.dailyMatchingTimeSelectorTv.text = (startTime + position + 1).toString() + " : 00"

            binding.dailyMatchingTimeSelectorBtn.isSelected = selectedStatus[position]

            binding.dailyMatchingTimeSelectorBtn.setOnClickListener {
                if (startPosition == -1) {
                    for (i in 0 until itemCount){
                        selectedStatus.put(i, false)
                    }

                    startPosition = position
                    mItemClickListener.onResetItem(startPosition)

                    selectedStatus.put(position, true)
                    binding.dailyMatchingTimeSelectorBtn.isSelected = true

                } else {

                    if (position >= startPosition) {
                        mItemClickListener.onClickItem(startPosition, position)
                        for (i in startPosition until position + 1) {
                            selectedStatus.put(i, true)
                        }
                        startPosition = -1
                    } else {

                        selectedStatus.put(startPosition, false)
                        notifyDataSetChanged()
                        startPosition = position
                        mItemClickListener.onResetItem(startPosition)
                        selectedStatus.put(startPosition, true)
                        binding.dailyMatchingTimeSelectorBtn.isSelected = true
                    }
                }
                notifyItemChanged(position)
            }
        }
    }
}