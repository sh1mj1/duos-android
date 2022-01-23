package com.example.duos.ui.main.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.CustomDialog
import com.example.duos.data.entities.ChatList
import com.example.duos.databinding.ItemChatListBinding

class ChatListRVAdapter(private var chatList: ArrayList<ChatList>) : RecyclerView.Adapter<ChatListRVAdapter.ViewHolder>(){
    private lateinit var dialogBuilder: CustomDialog.Builder

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

            binding.chatListDeleteBtn.setOnClickListener {

                showDialog(it.context, this.layoutPosition)
                dialogBuilder.show()
            }
        }
    }

    // position 위치의 데이터를 삭제 후 어댑터 갱신
    fun removeData(position: Int) {
        chatList.removeAt(position)
        notifyItemRemoved(position)
    }

    fun showDialog(context: Context, position: Int){
        // 만약 액티비티에서 사용한다면 아래 requireContext() 가 아닌 context를 사용하면 됨.
        dialogBuilder = CustomDialog.Builder(context)
            .setCommentMessage("채팅방을 나가면 상대와의 약속이 취소됩니다.\n삭제하시겠습니까?")// Dialog텍스트 설정하기 "~~~ "
            .setRightButton("삭제", object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {//오른쪽 버튼 클릭시 이벤트 설정하기
                    // API호출해서 채팅목록 삭제시키고,그에 따른 이벤트처리
                    Log.d("CustomDialog in SetupFrag", message.toString())//테스트 로그
                    removeData(position)
                    dialog.dismiss()
                }
            })
            .setLeftButton("취소",  object : CustomDialog.CustomDialogCallback {
                override fun onClick(dialog: CustomDialog, message: String) {//왼쪽 버튼 클릭시 이벤트 설정하기
                    //바로 dismiss되야야 함
                    Log.d("CustomDialog in SetupFrag", message.toString())//테스트 로그
                    dialog.dismiss()
                }
            })
    }
}