package com.example.duos.ui.main.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.ChatListItem
import com.example.duos.data.entities.RecommendedFriend
import com.example.duos.data.remote.chatList.ChatListService
import com.example.duos.databinding.FragmentChatListBinding
import com.example.duos.ui.main.friendList.RecommendFriendListRVAdapter
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.LocalTime
import org.threeten.bp.Period
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ChatListFragment() : Fragment(), ChatListView {
    lateinit var binding: FragmentChatListBinding
    private var chatListDatas = ArrayList<ChatListItem>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatListBinding.inflate(inflater, container, false)

        ChatListService.chatList(this, 0)
//        chatListDatas.apply {
//            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
//            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
//            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
//            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
//            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
//            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
//            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
//            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
//            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
//            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
//            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
//            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
//            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
//            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
//            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
//            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
//        }
        initRecyclerView()
        return binding.root
    }

    companion object {
        fun newInstance(): ChatListFragment = ChatListFragment()
    }

    override fun onResume() {
        super.onResume()
    }

    private fun initRecyclerView() {

    }

    override fun onGetChatListSuccess(chatList: List<ChatListItem>) {

        chatListDatas.addAll(chatList)

        var chatListRVAdapter = ChatListRVAdapter(chatListDatas)

        var chatListRv = binding.chatListRv
        chatListRv.adapter = chatListRVAdapter

        chatListRVAdapter.setChatListItemClickListener(object : ChatListRVAdapter.ChatListItemClickListener {
            override fun onItemClick(chatListItem: ChatListItem) {
                val intent = Intent(activity, ChattingActivity::class.java)
                startActivity(intent)
            }
        })

        chatListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        val swipeHelperCallback = ChatListItemTouchHelperCallback(chatListRVAdapter).apply {
            setClamp(resources.displayMetrics.widthPixels.toFloat() / 4)
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(chatListRv)

        chatListRv.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(chatListRv)
            false
        }
    }

    override fun onGetChatListFailure(code: Int, message: String) {
        Toast.makeText(activity, "code: $code, message: $message", Toast.LENGTH_LONG)
    }
}