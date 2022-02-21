package com.example.duos.ui.main.chat

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.example.duos.data.entities.ChatType

import android.view.LayoutInflater

import android.view.View
import android.widget.ProgressBar
import java.util.ArrayList

import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.duos.R
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.local.ChatDatabase


class ChattingMessagesRVAdapter(private var chatRoomIdx: String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chatMessageItem : ArrayList<ChatMessageItem?> = ArrayList<ChatMessageItem?>()
    lateinit var context : Context
    lateinit var chatDB: ChatDatabase

    //private val messages = mutableListOf<ChatMessageItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        context = parent.getContext()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        chatDB = ChatDatabase.getInstance(context, ChatDatabase.provideGson())!!

        return if (viewType === ChatType.CENTER_MESSAGE) {
            view = inflater.inflate(R.layout.date_border, parent, false)
            CenterViewHolder(view)
        } else if (viewType === ChatType.LEFT_MESSAGE) {
            view = inflater.inflate(R.layout.received_message, parent, false)
            LeftViewHolder(view, context, chatDB.chatRoomDao().getPartnerProfileImgUrl(chatRoomIdx))
        } else if (viewType == ChatType.RIGHT_MESSAGE){ // if (viewType == ChatItem.RIGHT_MESSAGE){
            view = inflater.inflate(R.layout.my_message, parent, false)
             RightViewHolder(view)
        } else {
            view = inflater.inflate(R.layout.message_loading, parent, false)
            LoadingViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is CenterViewHolder) {
            val item: ChatMessageItem? = chatMessageItem.get(position)
            if (item != null) {
                (viewHolder as CenterViewHolder).setItem(item)
            }
        } else if (viewHolder is LeftViewHolder) {
            val item: ChatMessageItem? = chatMessageItem.get(position)
            if (item != null) {
                (viewHolder as LeftViewHolder).setItem(item)
            }
        } else if (viewHolder is RightViewHolder) { // if (viewHolder instanceof RightViewHolder) {
            val item: ChatMessageItem? = chatMessageItem.get(position)
            if (item != null) {
                (viewHolder as RightViewHolder).setItem(item)
            }
        } else {
            Log.d("하아어아", "으어아아")
        }
    }

    override fun getItemCount(): Int = chatMessageItem.size

    fun addItem(item: ChatMessageItem) {
        chatMessageItem.add(item)
        notifyDataSetChanged()
    }

    fun setPagingMessages(pagingMessages: List<ChatMessageItem>){   // 페이징
        this.chatMessageItem.apply {
            clear()
            addAll(pagingMessages)
        }
        notifyDataSetChanged()

    }

    fun addPagingMessages(pagingMessages: List<ChatMessageItem>){   // 페이징
        this.chatMessageItem.addAll(0, pagingMessages)
        notifyDataSetChanged()
    }

    fun setLoadingView(b: Boolean){
        if(b){
            Handler(Looper.getMainLooper()).post{
                this.chatMessageItem.add(0, null)
                notifyItemInserted(0)
            }
        } else {
            if(this.chatMessageItem[0] == null){
                this.chatMessageItem.removeAt(0)
                notifyItemRemoved(0)
            }
        }
    }

    fun setItems(items: ArrayList<ChatMessageItem?>) {
        chatMessageItem = items
    }

    fun getItem(position: Int): ChatMessageItem? {
        return chatMessageItem.get(position)
    }

    override fun getItemViewType(position: Int): Int {
        return when (chatMessageItem[position]){
            null -> -1
            else -> chatMessageItem.get(position)?.viewType!!
        }
    }

    class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private var progressBar : ProgressBar
        var contentText: TextView
        fun setItem(item: ChatMessageItem) {
            //progressBar.visibility = View.GONE
            contentText.setText(item.body)
        }

        init {
            contentText = itemView.findViewById(R.id.enter_tv)
            //progressBar= itemView.findViewById(R.id.chatting_loading_pb)
        }
    }

    class LeftViewHolder(itemView: View, context: Context, partnerProfileImgUrl: String) : RecyclerView.ViewHolder(itemView) {
        //private var progressBar : ProgressBar
        var nameText: TextView
        var contentText: TextView
        var sendTimeText: TextView
        fun setItem(item: ChatMessageItem) {
            //progressBar.visibility = View.GONE
            nameText.setText(item.senderId)
            contentText.setText(item.body)
            sendTimeText.setText(item.formattedSentAt)
        }

        init {
            //progressBar= itemView.findViewById(R.id.chatting_loading_pb)
            nameText = itemView.findViewById(R.id.chatting_partner_id_tv)
            contentText = itemView.findViewById(R.id.chatting_received_message_body)
            sendTimeText = itemView.findViewById(R.id.chatting_received_message_time)
            Glide.with(context).load(partnerProfileImgUrl).apply(RequestOptions().circleCrop()).into(itemView.findViewById(R.id.chatting_partner_profile_iv))
        }
    }

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        //private var progressBar : ProgressBar
        var contentText: TextView
        var sendTimeText: TextView
        fun setItem(item: ChatMessageItem) {
            //progressBar.visibility = View.GONE
            contentText.setText(item.body)
            sendTimeText.setText(item.formattedSentAt)
        }

        init {
            //progressBar= itemView.findViewById(R.id.chatting_loading_pb)
            contentText = itemView.findViewById(R.id.my_message_body)
            sendTimeText = itemView.findViewById(R.id.my_message_time)
        }
    }

    class LoadingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private var progressBar : ProgressBar

        init {
            progressBar= itemView.findViewById(R.id.chatting_loading_pb)
        }
    }
}