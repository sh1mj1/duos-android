package com.example.duos.ui.main.chat

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.MessageItem

import com.example.duos.data.entities.ChatType

import android.view.LayoutInflater

import android.view.View
import java.util.ArrayList

import android.widget.TextView
import com.example.duos.R


class ChattingMessagesRVAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var chattingMessage : ArrayList<MessageItem> = ArrayList<MessageItem>()
    lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View
        val context: Context = parent.getContext()
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        return if (viewType === ChatType.CENTER_MESSAGE) {
            view = inflater.inflate(R.layout.date_border, parent, false)
            CenterViewHolder(view)
        } else if (viewType === ChatType.LEFT_MESSAGE) {
            view = inflater.inflate(R.layout.received_message, parent, false)
            LeftViewHolder(view)
        } else { // if (viewType == ChatItem.RIGHT_MESSAGE){
            view = inflater.inflate(R.layout.my_message, parent, false)
            RightViewHolder(view)
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (viewHolder is CenterViewHolder) {
            val item: MessageItem = chattingMessage.get(position)
            (viewHolder as CenterViewHolder).setItem(item)
        } else if (viewHolder is LeftViewHolder) {
            val item: MessageItem = chattingMessage.get(position)
            (viewHolder as LeftViewHolder).setItem(item)
        } else { // if (viewHolder instanceof RightViewHolder) {
            val item: MessageItem = chattingMessage.get(position)
            (viewHolder as RightViewHolder).setItem(item)
        }
    }

    override fun getItemCount(): Int = chattingMessage.size

    fun addItem(item: MessageItem) {
        chattingMessage.add(item)
        notifyDataSetChanged()
    }

    fun setItems(items: ArrayList<MessageItem>) {
        chattingMessage = items
    }

    fun getItem(position: Int): MessageItem {
        return chattingMessage.get(position)
    }

    override fun getItemViewType(position: Int): Int {
        return chattingMessage.get(position).viewType
    }

    class CenterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentText: TextView
        fun setItem(item: MessageItem) {
            contentText.setText(item.content)
        }

        init {
            contentText = itemView.findViewById(R.id.enter_tv)
        }
    }

    class LeftViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nameText: TextView
        var contentText: TextView
        var sendTimeText: TextView
        fun setItem(item: MessageItem) {
            nameText.setText(item.from)
            contentText.setText(item.content)
            sendTimeText.setText(item.sendTime)
        }

        init {
            nameText = itemView.findViewById(R.id.chatting_partner_id_tv)
            contentText = itemView.findViewById(R.id.chatting_received_message_body)
            sendTimeText = itemView.findViewById(R.id.chatting_received_message_time)
        }
    }

    class RightViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var contentText: TextView
        var sendTimeText: TextView
        fun setItem(item: MessageItem) {
            contentText.setText(item.content)
            sendTimeText.setText(item.sendTime)
        }

        init {
            contentText = itemView.findViewById(R.id.my_message_body)
            sendTimeText = itemView.findViewById(R.id.my_message_time)
        }
    }
}