package com.example.duos.ui.main.chat

import android.graphics.Insets.add
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.ChatList
import com.example.duos.databinding.FragmentChatListBinding
import com.example.duos.ui.BaseFragment

class ChatListFragment(): BaseFragment<FragmentChatListBinding>(FragmentChatListBinding::inflate) {
    private var chatListDatas = ArrayList<ChatList>()

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
        }

        val chatListRVAdapter = ChatListRVAdapter(chatListDatas)

        binding.chatListRv.adapter = chatListRVAdapter

        binding.chatListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    companion object {
        fun newInstance(): ChatListFragment = ChatListFragment()
    }
}