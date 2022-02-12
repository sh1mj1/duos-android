package com.example.duos.ui.main.mypage.appointment


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.appointment.AppointmentResDto
import com.example.duos.databinding.PreviousGamePlayerItemBinding
import com.example.duos.utils.GlideApp

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

    override fun onBindViewHolder(holder: MorePreviousGameReviewRVAdapter.ViewHolder, position: Int) {
        holder.bind(morePreviousPlayerList[position])
        holder.binding.previousGamePlayerIv.setOnClickListener {
            mItemClickListener.onProfileCLick(morePreviousPlayerList[position])
        }
    }

    override fun getItemCount(): Int = morePreviousPlayerList.size


    inner class ViewHolder(val binding: PreviousGamePlayerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(morePreviousGame: AppointmentResDto) {
            binding.previousGamePlayerNicknameTv.text = morePreviousGame.nickname
            GlideApp.with(binding.previousGamePlayerIv.context)
                .load(morePreviousGame.profilePhotoUrl)
                .into(binding.previousGamePlayerIv)
            binding.previousGamePlayTimeTv.text = morePreviousGame.appointmentTime

            binding.writeDoneTv.visibility = View.GONE
            binding.btnWriteReview.visibility = View.GONE

        }
    }

}