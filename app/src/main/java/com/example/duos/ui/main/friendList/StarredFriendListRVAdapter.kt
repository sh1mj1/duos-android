package com.example.duos.ui.main.friendList

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.duos.data.entities.StarredFriend
import com.example.duos.databinding.ItemFragmentStarredFriendListBinding

class StarredFriendListRVAdapter(private val friendlist : ArrayList<StarredFriend>) : RecyclerView.Adapter<StarredFriendListRVAdapter.ViewHolder>() {


    // 클릭 인터페이스 정의
    interface MyItemClickListener{
        fun onDeleteFriend(partnerIdx : Int)
        fun onGetFriendCount()
        fun gotoPartnerProfileActivity(partnerIdx : Int)
    }

    // 리스너 객체를 전달받는 함수랑 리스너 객체를 저장할 변수
    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): StarredFriendListRVAdapter.ViewHolder {
        val binding: ItemFragmentStarredFriendListBinding = ItemFragmentStarredFriendListBinding.inflate(
            LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.d("friendList",friendlist.toString())
        holder.bind(friendlist[position], position)
        mItemClickListener.onGetFriendCount()

        // 친구 삭제 버튼 클릭시 삭제
        holder.binding.myFriendListDeleteBtn.setOnClickListener {
            mItemClickListener.onDeleteFriend(friendlist[position].starredFrienedIdx!!)
            removeFriend(position)
            mItemClickListener.onGetFriendCount()
        }

        holder.binding.myFriendListProfileImageIv.setOnClickListener {
            mItemClickListener.gotoPartnerProfileActivity(friendlist[position].starredFrienedIdx!!)
        }

        holder.binding.myFriendListProfileIdTv.setOnClickListener {
            mItemClickListener.gotoPartnerProfileActivity(friendlist[position].starredFrienedIdx!!)
        }


    }

    private fun removeFriend(position: Int) {
        friendlist.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, friendlist.size)
    }


    override fun getItemCount(): Int = friendlist.size
    
    

    // 뷰홀더
    inner class ViewHolder(val binding: ItemFragmentStarredFriendListBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(myFriend : StarredFriend, position: Int){
            binding.myFriendListProfileIdTv.text = myFriend.starredFriendNickname
            binding.myFriendListAgeTv.text = (myFriend.starredFriendAge?.minus(myFriend.starredFriendAge!! % 10)).toString()
            binding.myFriendListSexTv.text =
                when(myFriend.starredFriendGender) {
                    1-> "남자"
                    else -> "여자"
                }
            Glide.with(binding.myFriendListProfileIdTv.context)
                .load(myFriend.starredFriendImgUrl)
                .into(binding.myFriendListProfileImageIv)

        }
    }
}