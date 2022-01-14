package com.example.duos.ui.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.ChatList
import com.example.duos.databinding.ItemChatListBinding

class ChatListRVAdapter(private val chatList: ArrayList<ChatList>) : RecyclerView.Adapter<ChatListRVAdapter.ViewHolder>(){

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChatListBinding = ItemChatListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatList[position])
    }

    override fun getItemCount(): Int = chatList.size

    inner class ViewHolder(val binding: ItemChatListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatList : ChatList){
            binding.chatListProfileIv.setImageResource(chatList.profileImg!!)
            binding.chatListUserIdTv.text = chatList.id
            binding.chatListChatPreviewTv.text = chatList.contentPreview
            binding.chatListChatMessageTime.text = chatList.messageTime
        }
    }
}