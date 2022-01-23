package com.example.duos.ui.main.chat

import androidx.recyclerview.widget.RecyclerView

interface ChatListItemTouchHelperListener {
    fun onItemSwipe(position: Int)
    fun onRightClick(position: Int, viewHolder: RecyclerView.ViewHolder)
}