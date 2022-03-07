package com.example.duos.ui.main.mypage.appointment

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.data.entities.appointment.AppointmentResDto
import com.example.duos.databinding.PreviousGamePlayerItemBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class PreviousGameReviewRVAdapter(private val previousGameList: ArrayList<AppointmentResDto>) :
    RecyclerView.Adapter<PreviousGameReviewRVAdapter.ViewHolder>() {

    interface PreviousPlayerItemClickListener {
        fun onProfileClick(appointmentItem: AppointmentResDto)         // 해당 회원 프로필로 이동
        fun onWriteBtnClick(appointmentItem: AppointmentResDto)     // 리뷰 작성하기로 이동
    }

    private lateinit var mItemClickListener: PreviousPlayerItemClickListener

    fun previousReviewItemClickListener(itemClickListener: PreviousPlayerItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            : PreviousGameReviewRVAdapter.ViewHolder {
        val binding: PreviousGamePlayerItemBinding = PreviousGamePlayerItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: PreviousGameReviewRVAdapter.ViewHolder, position: Int) {
        holder.bind(previousGameList[position])

        holder.binding.previousGamePlayerCv.setOnClickListener {
            mItemClickListener.onProfileClick(previousGameList[position])
        }
        holder.binding.btnWriteReview.setOnClickListener {
            mItemClickListener.onWriteBtnClick(previousGameList[position])
        }
    }

    override fun getItemCount(): Int = previousGameList.size

    inner class ViewHolder(val binding: PreviousGamePlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(myAppointment: AppointmentResDto) {
            binding.previousGamePlayerNicknameTv.text = myAppointment.nickname
            Glide.with(binding.previousGamePlayerIv.context)
                .load(myAppointment.profilePhotoUrl)
                .into(binding.previousGamePlayerIv)
            binding.previousGamePlayTimeTv.text = myAppointment.appointmentTime
            binding.previousGamePlayTimeTv.text = toDateStr(myAppointment.appointmentTime)
            if (myAppointment.reviewed) {
                // 이미 리뷰 됨
                binding.writeDoneTv.visibility = View.VISIBLE   // 작성 완료가 보임
                binding.btnWriteReview.visibility = View.GONE   // 후기 작성이 안보이고
            } else {
                binding.btnWriteReview.visibility = View.VISIBLE   // 후기 작성이 보임
                binding.writeDoneTv.visibility = View.GONE   // 작성 완료가 안 보임
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun toDateStr(date : String):String{
        val dateFromApi = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME)
        val dateDifference = ChronoUnit.DAYS.between(dateFromApi, LocalDateTime.now())
        var dateStr : String = ""
        if(dateDifference <= 0){    /* 오늘이면*/
            if(dateFromApi.hour <= 12){
                dateStr = "오늘 오전 ${dateFromApi.hour}시"
            }else{
                dateStr = "오늘 오후 ${dateFromApi.hour}시"
            }

        } else if(dateDifference <= 1){
            if(dateFromApi.hour <= 12){
                dateStr = "어제 오전 ${dateFromApi.hour}시"
            }else{
                dateStr = "어제 오후 ${dateFromApi.hour - 12}시"
            }
        } else{
            dateStr = dateFromApi.toLocalDate().toString()
            //DateTimeFormatter.ofPattern ( "yyyy-MM-dd HH:mm:ss.SSSX" )
        }

        return dateStr
    }
}

