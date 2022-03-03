package com.example.duos.ui.main.dailyMatching

import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
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
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.dailyMatching.DailyMatching
import com.example.duos.ui.main.chat.ChattingMessagesRVAdapter
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import kotlin.collections.ArrayList

class DailyMatchingRVAdapter(val itemClickListener: OnItemClickListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var dailyMatchingItem : java.util.ArrayList<DailyMatching?> =
        java.util.ArrayList<DailyMatching?>()
    lateinit var context: Context

    companion object {
        private const val TYPE_ITEM = 0
        private const val TYPE_LOADING = 1
    }


    interface OnItemClickListener{
        fun onItemClicked(dailyMatching: DailyMatching)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        context = parent.getContext()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        when (viewType) {
            TYPE_ITEM -> {
                view =
                    inflater.inflate(R.layout.item_fragment_daily_matching_fragment, parent, false)
                return ItemViewHolder(view)
            }
            else -> {
                view = inflater.inflate(R.layout.message_loading, parent, false)
                return LoadingViewHolder(view)
            }

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is ItemViewHolder) {
            val item: DailyMatching? = dailyMatchingItem.get(position)
            if (item != null) {
                (viewHolder as ItemViewHolder).setItem(item, itemClickListener)
            }
        }
    }

    override fun getItemCount(): Int = dailyMatchingItem.size

    fun addItem(item: DailyMatching) {
        dailyMatchingItem.add(item)
        notifyDataSetChanged()
    }

    fun setPagingMessages(pagingList: List<DailyMatching?>?) {   // 페이징
        dailyMatchingItem.apply {
            clear()
            if (pagingList != null) {
                addAll(pagingList)
            }
        }
        notifyDataSetChanged()

    }

    fun addPagingMessages(pagingList: List<DailyMatching?>?) {   // 페이징
        if (pagingList != null) {
            dailyMatchingItem.addAll(pagingList)
        }
        notifyDataSetChanged()
    }

    fun setLoadingView(b: Boolean) {
        if (b) {
            Handler(Looper.getMainLooper()).post {
                dailyMatchingItem.add(null)
                notifyItemInserted(dailyMatchingItem.size - 1)
            }
        } else {
            if (this.dailyMatchingItem[dailyMatchingItem.size - 1] == null) {
                this.dailyMatchingItem.removeAt(dailyMatchingItem.size - 1)
                notifyItemRemoved(dailyMatchingItem.size)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (dailyMatchingItem[position]) {
            null -> TYPE_LOADING
            else -> TYPE_ITEM
        }
    }

    // 뷰홀더
    @RequiresApi(Build.VERSION_CODES.O)
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
            title = itemView.findViewById(R.id.daily_matching_post_preview_title_tv)
            matchPlace = itemView.findViewById(R.id.daily_matching_post_preview_location_tv)
            matchDate = itemView.findViewById(R.id.daily_matching_post_preview_date_tv)
            startTime = itemView.findViewById(R.id.daily_matching_post_preview_calendar_tv)
            recruitmentStatus = itemView.findViewById(R.id.daily_matching_post_preview_state_tv)
            userProfileImage =
                itemView.findViewById(R.id.daily_matching_post_preview_profile_image_iv)
            userNickname = itemView.findViewById(R.id.daily_matching_post_preview_nickname_tv)
            userAge = itemView.findViewById(R.id.daily_matching_post_preview_age_tv)
            userGender = itemView.findViewById(R.id.daily_matching_post_preview_gender_tv)
            viewCount = itemView.findViewById(R.id.daily_matching_post_preview_see_count_tv)
            matchTime = itemView.findViewById(R.id.daily_matching_post_preview_time_tv)
            messageCount = itemView.findViewById(R.id.daily_matching_post_preview_chat_count_tv)

        }

        fun setItem(item: DailyMatching, clickListener: OnItemClickListener) {
            if (item.recruitmentStatus.equals("recruiting")) {
                DrawableCompat.setTint(
                    matchDate.getBackground(),
                    ContextCompat.getColor(
                        title.context,
                        R.color.primary
                    )
                )
                matchDate.setTextColor(
                    ContextCompat.getColor(
                        title.context,
                        R.color.white
                    )
                )
                DrawableCompat.setTint(
                    recruitmentStatus.getBackground(),
                    ContextCompat.getColor(
                        title.context,
                        R.color.inch_worm
                    )
                )
                recruitmentStatus.setText(R.string.daily_matching_state_ongoing)
                recruitmentStatus.setTextColor(
                    ContextCompat.getColor(
                        title.context,
                        R.color.white
                    )
                )
            } else {
                DrawableCompat.setTint(
                    matchDate.getBackground(),
                    ContextCompat.getColor(
                        title.context,
                        R.color.grainsboro_D
                    )
                )
                matchDate.setTextColor(
                    ContextCompat.getColor(
                        title.context,
                        R.color.grey
                    )
                )
                DrawableCompat.setTint(
                    recruitmentStatus.getBackground(),
                    ContextCompat.getColor(
                        title.context,
                        R.color.grainsboro_D
                    )
                )
                recruitmentStatus.setText(R.string.daily_matching_state_complete)
                recruitmentStatus.setTextColor(
                    ContextCompat.getColor(
                        title.context,
                        R.color.grey
                    )
                )
            }
            val simpleDateFormat = SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US)
            val dateFormat = SimpleDateFormat("M/d")
            val matchingDate = simpleDateFormat.parse(simpleDateFormat.format(item.matchDate))
            if (((matchingDate.time - Date(System.currentTimeMillis()).time) / (24 * 60 * 60 * 1000)).toInt() == 0) {
                matchDate.text = matchDate.context.getString(R.string.daily_matching_date_today)

            } else if (((matchingDate.time - Date(System.currentTimeMillis()).time) / (24 * 60 * 60 * 1000)).toInt() == 1) {
                matchDate.text =
                    matchDate.context.getString(R.string.daily_matching_date_tomorrow)
            } else {

                matchDate.text = dateFormat.format(matchingDate)
            }

            title.text = item.title
            userNickname.text = item.userNickname
            matchPlace.text = item.matchPlace
            val format = SimpleDateFormat("M/d(E)", Locale.KOREAN)
            val timeFormat = SimpleDateFormat("HH:mm")
            val startTime1: org.threeten.bp.LocalDateTime = item.startTime
            val startTime2: LocalDateTime = LocalDateTime.parse(startTime1.toString());
            val endTime1: org.threeten.bp.LocalDateTime = item.endTime
            val endTime2: LocalDateTime = LocalDateTime.parse(endTime1.toString());
            startTime.text =
                format.format(Date.from(startTime2.atZone(ZoneId.systemDefault()).toInstant()))
            matchTime.text =
                timeFormat.format(
                    Date.from(
                        startTime2.atZone(ZoneId.systemDefault()).toInstant()
                    )
                )
                    .toString() + "~" +
                        timeFormat.format(
                            Date.from(
                                endTime2.atZone(ZoneId.systemDefault()).toInstant()
                            )
                        ).toString()
            viewCount.text = item.viewCount.toString()
            messageCount.text = item.messageCount.toString()
            Glide.with(userProfileImage.context)
                .load(item.userProfileImage)
                .into(userProfileImage)
            userGender.text =
                when (item.userGender) {
                    1 -> "남"
                    else -> "여"
                }
            userAge.text = (item.userAge - item.userAge % 10).toString() + "대"

            itemView.setOnClickListener {
                clickListener.onItemClicked(item)
            }
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var progressBar: ProgressBar

        init {
            progressBar = itemView.findViewById(R.id.chatting_loading_pb)
        }
    }

}