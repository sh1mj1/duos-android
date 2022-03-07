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
import com.example.duos.databinding.ItemDailyMatchingTimeTvSelectorBinding
import java.util.*
import kotlin.collections.ArrayList


class DailyMatchingTimeTvSelectRVAdapter(val startTime: Int) :
    RecyclerView.Adapter<DailyMatchingTimeTvSelectRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        viewGroup: ViewGroup,
        viewType: Int
    ): DailyMatchingTimeTvSelectRVAdapter.ViewHolder {
        val binding: ItemDailyMatchingTimeTvSelectorBinding =
            ItemDailyMatchingTimeTvSelectorBinding.inflate(
                LayoutInflater.from(viewGroup.context), viewGroup, false
            )

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

    }

    override fun getItemCount(): Int = 24 - startTime


    // 뷰홀더
    inner class ViewHolder(val binding: ItemDailyMatchingTimeTvSelectorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) {
            binding.dailyMatchingTimeSelectorTv.text = (startTime + position).toString() + " : 00"
        }
    }
}