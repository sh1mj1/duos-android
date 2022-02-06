package com.example.duos.ui.main.chat

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.chat.chatList.ChatListService
import com.example.duos.databinding.FragmentChatListBinding
import com.example.duos.ui.BaseFragment
import kotlin.collections.ArrayList

class ChatListFragment(): BaseFragment<FragmentChatListBinding>(FragmentChatListBinding::inflate), ChatListView, ChatListRVAdapter.DeleteClickListener {
    // BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate)
    //lateinit var binding : FragmentChatListBinding
    private var chatListDatas = ArrayList<ChatRoom>()
    private lateinit var chatListRv: RecyclerView
    private lateinit var chatListRVAdapter: ChatListRVAdapter
    lateinit var swipeHelperCallback: ChatListItemTouchHelperCallback
    private lateinit var mContext:Context
    lateinit var chatDB: ChatDatabase
    val userIdx = 76

    override fun initAfterBinding() {
        chatListRv = binding.chatListRv
        chatListRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        chatListRVAdapter = ChatListRVAdapter(chatListDatas, this)
        chatListRv.adapter = chatListRVAdapter

        chatDB = ChatDatabase.getInstance(requireContext())!!

        if(isNetworkAvailable(mContext)){   // 인터넷 연결 돼있을 때
            ChatListService.chatList(this, userIdx)
            Log.d("인터넷 연결 확인", "CONNECTED")
        } else {    // 인터넷 연결 안돼있을 때
            Log.d("인터넷 연결 확인", "DISCONNECTED")
            chatListDatas.clear()
            chatListDatas.addAll(chatDB.chatRoomDao().getChatRoomList())
        }

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
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentChatListBinding.inflate(inflater, container, false)
//
//        chatListRv = binding.chatListRv
//        chatListRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
//        chatListRVAdapter = ChatListRVAdapter(chatListDatas, this)
//        chatListRv.adapter = chatListRVAdapter
//
//        ChatListService.chatList(this, 76)
////        chatListDatas.apply {
////            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
////            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
////            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
////            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
////            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
////            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
////            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
////            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
////            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
////            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
////            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
////            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
////            add(ChatList(R.drawable.chat_profile_img_1, "4evertennis", "네네 그럼 그때 뵐게요~!!", "오후 10:59"))
////            add(ChatList(R.drawable.chat_profile_img_2, "uiii_88", "알겠습니다", "오전 09:11"))
////            add(ChatList(R.drawable.chat_profile_img_3, "djeikd0620", "넵~", "오후 11:33"))
////            add(ChatList(R.drawable.chat_profile_img_4, "drahm0422", "아~~ 그러면", "오후 04:14"))
////        }
//        initRecyclerView()
//        return binding.root
//    }

    companion object {
        fun newInstance(): ChatListFragment = ChatListFragment()
    }

    private fun initRecyclerView(){
        chatListRVAdapter = ChatListRVAdapter(chatListDatas, this)
        chatListRv.adapter = chatListRVAdapter

        chatListRVAdapter.setChatListItemClickListener(object: ChatListRVAdapter.ChatListItemClickListener {
            override fun onItemClick(chatRoom: ChatRoom) {
                val intent = Intent(activity, ChattingActivity::class.java)
                startActivity(intent)
            }
        })

        chatListRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)

        swipeHelperCallback = ChatListItemTouchHelperCallback(chatListRVAdapter).apply {
            if(mContext != null){
                setClamp(resources.displayMetrics.widthPixels.toFloat()/4)
            }
        }
        ItemTouchHelper(swipeHelperCallback).attachToRecyclerView(chatListRv)

        chatListRv.setOnTouchListener { _, _ ->
            swipeHelperCallback.removePreviousClamp(chatListRv)
            false
        }
    }

    override fun onGetChatListSuccess(chatList: List<ChatRoom>) {

        chatListDatas.clear()

        var chatListGotten = chatList   // 서버에서 불러온 채팅방 목록

        chatListDatas.addAll(chatListGotten)    // 서버에서 불러온 채팅방을 리사이클러뷰에 그대로 띄워주기 위해 chatListDatas에 담음

        Log.d("채팅방 확인", chatDB.chatRoomDao().getChatRoomList().toString())

        initRecyclerView()

        // 룸디비에 변경된/추가된 채팅방 저장
        var chatListStored = chatDB.chatRoomDao().getChatRoomList()     // 룸DB에 저장되어있는 채팅방 목록
        var chatListUpdated = chatListGotten.filterNot { it in chatListStored } //서버에서 불러온 채팅방 목록 중 룸DB에 저장되어있지 않은 채팅방들의 리스트
        if(!chatListUpdated.isEmpty()){
            Log.d("업데이트된 채팅방 확인", chatListUpdated.toString())
            for (i: Int in 0..chatListUpdated.size-1){    // 룸DB에 아직 업데이트되지 않은 채팅방을 모두 룸DB에 저장
                if(chatDB.chatRoomDao().getChatRoomIdx(chatListUpdated[i].chatRoomIdx).isNullOrEmpty()){    // 새로 생성된 채팅방일 때 ---- 이 부분은 채팅방 생성 fcm 구현 후 수정 필요할 듯
                    chatDB.chatRoomDao().insert(chatListUpdated[i]) // 새로 생성된 채팅방 룸DB에 추가
                }else{  // 기존 채팅방에 업데이트된 내용이 있을 때
                    chatDB.chatRoomDao().update(chatListUpdated[i]) // 룸DB에서 update()는 primary key를 기준으로 한다
                }

            }
        }else{
            Log.d("업데이트된 채팅방 확인", "없음")
        }
    }

    override fun onGetChatListFailure(code: Int, message: String) {
        Toast.makeText(activity,"code: $code, message: $message", Toast.LENGTH_LONG)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun getIsClamped(viewHolder: RecyclerView.ViewHolder): Boolean {
        return swipeHelperCallback.getTag(viewHolder)
    }

    override fun onDeleteClick() {
        val intent = Intent(activity, ChattingActivity::class.java)
        startActivity(intent)
    }
}