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

class MorePreviousGameReviewRVAdapter(private val morePreviousPlayerList: ArrayList<AppointmentResDto>) :
    RecyclerView.Adapter<MorePreviousGameReviewRVAdapter.ViewHolder>() {

    interface MorePreiousPlayerItemclickListener {
       fun onProfileCLick(appointmentItem: AppointmentResDto)
    }

    private lateinit var mItemClickListener: MorePreiousPlayerItemclickListener
    fun morePreviousItemClickListener(itemClickListener: MorePreiousPlayerItemclickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MorePreviousGameReviewRVAdapter.ViewHolder {
        val binding: PreviousGamePlayerItemBinding = PreviousGamePlayerItemBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MorePreviousGameReviewRVAdapter.ViewHolder, position: Int) {
        holder.bind(morePreviousPlayerList[position])
        holder.binding.previousGamePlayerIv.setOnClickListener {
            mItemClickListener.onProfileCLick(morePreviousPlayerList[position])
        }
    }

    override fun getItemCount(): Int = morePreviousPlayerList.size


    inner class ViewHolder(val binding: PreviousGamePlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(morePreviousGame: AppointmentResDto) {
            binding.previousGamePlayerNicknameTv.text = morePreviousGame.nickname
            Glide.with(binding.previousGamePlayerIv.context)
                .load(morePreviousGame.profilePhotoUrl)
                .into(binding.previousGamePlayerIv)
            binding.previousGamePlayTimeTv.text = morePreviousGame.appointmentTime
            binding.previousGamePlayTimeTv.text = toDateStr(morePreviousGame.appointmentTime)
            binding.writeDoneTv.visibility = View.GONE
            binding.btnWriteReview.visibility = View.GONE

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