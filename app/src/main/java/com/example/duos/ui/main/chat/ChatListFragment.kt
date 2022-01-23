package com.example.duos.ui.main.chat

import android.content.Intent
import android.graphics.Insets.add
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.R
import com.example.duos.data.entities.ChatList
import com.example.duos.databinding.FragmentChatListBinding
import com.example.duos.ui.BaseFragment

class ChatListFragment(): BaseFragment<FragmentChatListBinding>(FragmentChatListBinding::inflate) {
    private var chatListDatas = ArrayList<ChatList>()
//    private lateinit var chatListRVAdapter: ChatListRVAdapter
//    private lateinit var chatListRv: RecyclerView

    override fun initAfterBinding() {
        chatListDatas.apply {
            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
        }
    }

    companion object {
        fun newInstance(): ChatListFragment = ChatListFragment()
    }

    override fun onResume() {
        super.onResume()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        var chatListRVAdapter = ChatListRVAdapter(chatListDatas)

        var chatListRv = binding.chatListRv
        chatListRv.adapter = chatListRVAdapter

        chatListRVAdapter.setChatListItemClickListener(object: ChatListRVAdapter.ChatListItemClickListener {
            override fun onItemClick(chatList: ChatList) {
                val intent = Intent(activity, ChattingActivity::class.java)
                startActivity(intent)
            }
        })

        chatListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeHelperCallback = ChatListItemTouchHelperCallback(chatListRVAdapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat()/4)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(chatListRv)

        chatListRv.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(chatListRv)
            false
        }
    }
}