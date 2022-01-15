package com.example.duos.ui.main.friendList

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.Friend
import com.example.duos.databinding.ItemFragmentLastRecommendFriendListTodayBinding

class LastRecommendFriendListTodayRVAdapter (private val friendlist : ArrayList<Friend>) : RecyclerView.Adapter<LastRecommendFriendListTodayRVAdapter.ViewHolder>() {


    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onDeleteFriend(friendId : String)
        fun onAddFriend(friendId: String)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): LastRecommendFriendListTodayRVAdapter.ViewHolder {
        val binding: ItemFragmentLastRecommendFriendListTodayBinding = ItemFragmentLastRecommendFriendListTodayBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(friendlist[position], position)

        // 친구 삭제 버튼 클릭시 삭제
        holder.binding.lastRecommendFriendListTodayDeleteBtn.setOnClickListener {
            mItemClickListener.onDeleteFriend(friendlist[position].profileId)
            removeFriend(position)
        }

    }

    private fun removeFriend(position: Int) {
        friendlist.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, friendlist.size)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addFriend(friends: ArrayList<Friend>) {
        this.friendlist.clear()
        this.friendlist.addAll(friends)

        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = friendlist.size



    // 뷰홀더
    inner class ViewHolder(val binding: ItemFragmentLastRecommendFriendListTodayBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(friend : Friend, position: Int){
            binding.lastRecommendFriendListTodayIdTv.text = friend.profileId
            friend.profileImg?.let { binding.lastRecommendFriendListTodayProfileImageIv.setImageResource(it) }
            binding.lastRecommendFriendListTodayAgeTv.text = friend.profileAge.toString()
            binding.lastRecommendFriendListTodaySexTv.text = friend.profileSex
        }
    }
}