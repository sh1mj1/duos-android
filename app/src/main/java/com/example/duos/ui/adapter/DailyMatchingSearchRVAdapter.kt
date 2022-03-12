package com.example.duos.ui.adapter

import android.annotation.SuppressLint
import android.app.appsearch.SearchResult
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.remote.dailyMatching.SearchResultItem
import com.example.duos.databinding.ItemFragmentDailyMatchingFragmentBinding
import com.example.duos.ui.main.dailyMatching.dailyMatchingSearch.DailyMatchingSearchActivity
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

class DailyMatchingSearchRVAdapter(private val searchResultItem: ArrayList<SearchResultItem>) :
    RecyclerView.Adapter<DailyMatchingSearchRVAdapter.ItemViewHolder>() {

    lateinit var context: Context
    interface SearchResultItemClickListener{
        fun onItemClick(searchResultItem : SearchResultItem)
    }
    interface BindLastViewHolderListener{
        fun onLastBind()
    }

    interface ViewReadyCallback{
        fun onLayoutReady()
    }

    private lateinit var mItemClickListener : SearchResultItemClickListener
    private lateinit var mLastBindListener : BindLastViewHolderListener
//    private lateinit var mItemReadyCallback : ViewReadyCallback

    fun clickSearchResultListener(itemClickListener : SearchResultItemClickListener){
        mItemClickListener = itemClickListener
    }

    fun lastBindListener(param: BindLastViewHolderListener) {
        mLastBindListener = param
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view: View
        context = parent.context

        val binding: ItemFragmentDailyMatchingFragmentBinding =
            ItemFragmentDailyMatchingFragmentBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        return ItemViewHolder(binding)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        Log.d("DailyMatchingSearch", "onBindViewHolder position : $position , getItemCount : $itemCount")
        holder.setItem(searchResultItem[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(searchResultItem[position]) }
        // n번째 검색 시 이미 많이 bindViewHolder 해놓음
//        if(position >= itemCount-5){
//            // 첫 검색 혹은
//            Log.d("DailyMatchingSearch", "onBindViewHolder 마지막 ")
//            mLastBindListener.onLastBind()
//        }

    }

    override fun getItemCount(): Int {
        return searchResultItem.size
    }


    inner class ItemViewHolder(val binding: ItemFragmentDailyMatchingFragmentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        var title: TextView
        var matchPlace: TextView
        var matchDate: TextView
        var startTime: TextView
        var recruitmentStatus: TextView
        var userProfileImage: ImageView
        var userNickname: TextView
        var userAge: TextView
        var userGender: TextView
        var viewCount: TextView
        var messageCount: TextView
        var matchTime: TextView

        init {
            title = binding.dailyMatchingPostPreviewTitleTv
            matchPlace = binding.dailyMatchingPostPreviewLocationTv
            matchDate = binding.dailyMatchingPostPreviewDateTv
            startTime = binding.dailyMatchingPostPreviewCalendarTv
            recruitmentStatus = binding.dailyMatchingPostPreviewStateTv
            userProfileImage = binding.dailyMatchingPostPreviewProfileImageIv
            userNickname = binding.dailyMatchingPostPreviewNicknameTv
            userAge = binding.dailyMatchingPostPreviewAgeTv
            userGender = binding.dailyMatchingPostPreviewGenderTv
            viewCount = binding.dailyMatchingPostPreviewSeeCountTv
            matchTime = binding.dailyMatchingPostPreviewTimeTv
            messageCount = binding.dailyMatchingPostPreviewChatCountTv
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SimpleDateFormat", "SetTextI18n")
        fun setItem(item: SearchResultItem) {
            if (item.recruitmentStatus == "recruiting") {
                DrawableCompat.setTint(
                    matchDate.background, ContextCompat.getColor(title.context, R.color.primary)
                )
                matchDate.setTextColor(ContextCompat.getColor(title.context, R.color.white))
                DrawableCompat.setTint(
                    recruitmentStatus.background, ContextCompat.getColor(
                        title.context, R.color.inch_worm
                    )
                )
                recruitmentStatus.setText(R.string.daily_matching_state_ongoing)
                recruitmentStatus.setTextColor(
                    ContextCompat.getColor(
                        title.context, R.color.white
                    )
                )
            } else {
                DrawableCompat.setTint(
                    matchDate.background,
                    ContextCompat.getColor(title.context, R.color.grainsboro_D)
                )
                matchDate.setTextColor(ContextCompat.getColor(title.context, R.color.grey))
                DrawableCompat.setTint(
                    recruitmentStatus.background,
                    ContextCompat.getColor(title.context, R.color.grainsboro_D)
                )
                recruitmentStatus.setText(R.string.daily_matching_state_complete)
                recruitmentStatus.setTextColor(ContextCompat.getColor(title.context, R.color.grey))
            }

            val simpleDateFormat = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US)
            val dateFormat = SimpleDateFormat("M/d")
            val matchingDate = simpleDateFormat.parse(simpleDateFormat.format(item.matchDate))
            when {
                ((matchingDate.time - Date(System.currentTimeMillis()).time) / (24 * 60 * 60 * 1000)).toInt() == 0 -> {
                    matchDate.text = matchDate.context.getString(R.string.daily_matching_date_today)

                }
                ((matchingDate.time - Date(System.currentTimeMillis()).time) / (24 * 60 * 60 * 1000)).toInt() == 1 -> {
                    matchDate.text =
                        matchDate.context.getString(R.string.daily_matching_date_tomorrow)
                }
                else -> {
                    matchDate.text = dateFormat.format(matchingDate)
                }
            }
            title.text = item.title
            userNickname.text = item.userNickname
            matchPlace.text = item.matchPlace

            val format = SimpleDateFormat("M/d(E)", Locale.KOREAN)
            val timeFormat = SimpleDateFormat("HH:mm")
            val startTime1: org.threeten.bp.LocalDateTime = item.startTime
            val startTime2: LocalDateTime = LocalDateTime.parse(startTime1.toString())
            val endTime1: org.threeten.bp.LocalDateTime = item.endTime
            val endTime2: LocalDateTime = LocalDateTime.parse(endTime1.toString())

            startTime.text =
                format.format(Date.from(startTime2.atZone(ZoneId.systemDefault()).toInstant()))
            matchTime.text =
                timeFormat.format(Date.from(startTime2.atZone(ZoneId.systemDefault()).toInstant()))
                    .toString() + "~" + timeFormat.format(
                    Date.from(
                        endTime2.atZone(ZoneId.systemDefault()).toInstant()
                    )
                ).toString()
            viewCount.text = item.viewCount.toString()
            messageCount.text = item.messageCount.toString()
            Glide.with(userProfileImage.context).load(item.userProfileImage).into(userProfileImage)
            userGender.text = when (item.userGender) {
                1 -> "남"
                else -> "여"
            }
            userAge.text = (item.userAge - item.userAge % 10).toString() + "대"


        }
    }
}
