package com.example.duos.ui.main.chat

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.duos.CustomDialog
import com.example.duos.data.entities.ChatListItem
import com.example.duos.databinding.ItemChatListBinding
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.format.DateTimeFormatter
import kotlin.collections.ArrayList

class ChatListRVAdapter(private var chatList: ArrayList<ChatListItem>) : RecyclerView.Adapter<ChatListRVAdapter.ViewHolder>(){
    private lateinit var dialogBuilder: CustomDialog.Builder
    private lateinit var context: Context

    interface ChatListItemClickListener{
        fun onItemClick(chatListItem: ChatListItem)
    }

    private lateinit var mItemClickListener: ChatListItemClickListener

    fun setChatListItemClickListener(itemClickListener: ChatListItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChatListBinding = ItemChatListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        context = viewGroup.context
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(chatList[position])
        holder.itemView.setOnClickListener { mItemClickListener.onItemClick(chatList[position]) }
    }

    override fun getItemCount(): Int = chatList.size

    inner class ViewHolder(val binding: ItemChatListBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(chatListItem : ChatListItem){
//            binding.chatListProfileIv.setImageResource(chatListItem.profileImg!!)
//            binding.chatListUserIdTv.text = chatListItem.id
//            binding.chatListChatPreviewTv.text = chatListItem.contentPreview
//            binding.chatListChatMessageTime.text = chatListItem.messageTime

            val messageTime = formatDateTime(chatListItem.lastUpdatedAt)
            binding.chatListChatMessageTime.text = messageTime
            binding.chatListChatPreviewTv.text = chatListItem.lastMessage
            binding.chatListUserIdTv.text = chatListItem.chatRoomName
            Glide.with(context).load(chatListItem.chatRoomImg).apply(RequestOptions().circleCrop()).into(binding.chatListProfileIv)

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

    fun formatDateTime(localDateTime: String): String{
        // 마지막 채팅 온 시간 formatting
        //var lastUpdatedAt = chatListData.lastUpdatedAt
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val dateTime = LocalDateTime.parse(localDateTime, formatter).toString()
        //var date = LocalDate.parse(lastUpdatedAt, formatter)
        //val time = LocalTime.parse(lastUpdatedAt, formatter)

//        lastUpdatedAt = lastUpdatedAt.format(
//            DateTimeFormatter.ofPattern("a HH: mm").withLocale(
//                Locale.forLanguageTag("ko")))
        val pattern = DateTimeFormatter.ofPattern("a hh:mm")
        val lastUpdatedAt = dateTime.format(pattern)
        Log.d("after formatting", lastUpdatedAt)
        return lastUpdatedAt
    }

    // 임시.... 10보다 작으면 0이 앞에 붙도록 고쳐야함
    fun formatDateTime2(localDateTime: String): String{
        // 마지막 채팅 온 시간 formatting
        //var lastUpdatedAt = chatListData.lastUpdatedAt
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        //val dateTime = LocalDateTime.parse(localDateTime, formatter).toString()
        //var date = LocalDate.parse(lastUpdatedAt, formatter)
        val time = LocalTime.parse(localDateTime, formatter)
        var ampm = ""
        var hour = time.hour
        var min = ""
        if(time.hour < 12){
            ampm = "오전 "
        }else{
            ampm = "오후 "
            hour = hour - 12
        }
        var lastUpdatedAt = ampm + hour.toString() + ":"+min
//        lastUpdatedAt = lastUpdatedAt.format(
//            DateTimeFormatter.ofPattern("a HH: mm").withLocale(
//                Locale.forLanguageTag("ko")))
//        val pattern = DateTimeFormatter.ofPattern("a hh:mm")
//        val lastUpdatedAt = dateTime.format(pattern)
        Log.d("after formatting", lastUpdatedAt)
        return lastUpdatedAt
    }
}