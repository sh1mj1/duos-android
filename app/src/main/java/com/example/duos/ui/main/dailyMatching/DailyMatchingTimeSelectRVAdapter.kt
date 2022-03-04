package com.example.duos.ui.main.dailyMatching

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.databinding.ItemDailyMatchingTimeSelectorBinding
import android.R


class DailyMatchingTimeSelectRVAdapter(val startTime : Int) : RecyclerView.Adapter<DailyMatchingTimeSelectRVAdapter.ViewHolder>() {

    lateinit var timeArray: Array<String>

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): DailyMatchingTimeSelectRVAdapter.ViewHolder {
        val binding: ItemDailyMatchingTimeSelectorBinding = ItemDailyMatchingTimeSelectorBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int = 24 - startTime


    // 뷰홀더
    inner class ViewHolder(val binding: ItemDailyMatchingTimeSelectorBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(position: Int) {
            binding.dailyMatchingTimeSelectorBtn.setOnClickListener {
                if (binding.dailyMatchingTimeSelectorBtn.isSelected){
                    binding.dailyMatchingTimeSelectorBtn.isSelected = false
                } else {
                    binding.dailyMatchingTimeSelectorBtn.isSelected = true
                }
            }
        }
    }
}