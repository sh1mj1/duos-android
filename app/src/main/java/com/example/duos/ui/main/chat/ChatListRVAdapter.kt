package com.example.duos.ui.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.ChatList
import com.example.duos.databinding.ItemChatListBinding

class ChatListRVAdapter(private var chatList: ArrayList<ChatList>) : RecyclerView.Adapter<ChatListRVAdapter.ViewHolder>(), ChatListItemTouchHelperListener{
    interface ChatListItemClickListener{
        fun onItemClick(chatList: ChatList)
    }

    private lateinit var mItemClickListener: ChatListItemClickListener

    fun setChatListItemClickListener(itemClickListener: ChatListItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChatListBinding = ItemChatListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(chatList[position]) }
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

    override fun onItemSwipe(position: Int) {
        chatList.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder) { //오른쪽 삭제 버튼 누르면 삭제 확인 팝업
        chatList.removeAt(position)
        notifyItemRemoved(position)
    }



}