package com.example.duos.ui.main.chat

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.chat.chatList.ChatListService
import com.example.duos.databinding.FragmentChatListBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.utils.getUserIdx
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
    val userIdx = getUserIdx()!!

    override fun initAfterBinding() {
        chatListRv = binding.chatListRv
        chatListRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false)
        chatListRVAdapter = ChatListRVAdapter(chatListDatas, this)
        chatListRv.adapter = chatListRVAdapter

        chatDB = ChatDatabase.getInstance(requireContext(), ChatDatabase.provideGson())!!

        if(isNetworkAvailable(mContext)){   // 인터넷 연결 돼있을 때
//            progressON()
            ChatListService.chatList(this, getUserIdx()!!)
            Log.d("인터넷 연결 확인", "CONNECTED")
        } else {    // 인터넷 연결 안돼있을 때
            Log.d("인터넷 연결 확인", "DISCONNECTED")
            chatListDatas.clear()
            chatListDatas.addAll(chatDB.chatRoomDao().getChatRoomList())
        }

        initRecyclerView()
    }

    companion object {
        fun newInstance(): ChatListFragment = ChatListFragment()
    }

    private fun initRecyclerView(){
        chatListRVAdapter = ChatListRVAdapter(chatListDatas, this)
        chatListRv.adapter = chatListRVAdapter

        chatListRVAdapter.setChatListItemClickListener(object: ChatListRVAdapter.ChatListItemClickListener {
            override fun onItemClick(chatRoom: ChatRoom) {
                val intent = Intent(activity, ChattingActivity::class.java)
                intent.putExtra("isFromChatList", true)
                intent.putExtra("isFromPlayerProfile", false)
                intent.putExtra("chatRoomIdx", chatRoom.chatRoomIdx)
                intent.putExtra("senderId", chatRoom.chatRoomName)
                intent.putExtra("partnerIdx", chatRoom.participantIdx)
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

//        progressOFF()

        chatListDatas.clear()

        var chatListGotten = chatList   // 서버에서 불러온 채팅방 목록

        chatListDatas.addAll(chatListGotten)    // 서버에서 불러온 채팅방을 리사이클러뷰에 그대로 띄워주기 위해 chatListDatas에 담음

        //Log.d("채팅방 확인", chatDB.chatRoomDao().getChatRoomList().toString())

        initRecyclerView()

        // 룸디비에 변경된/추가된 채팅방 저장
        var chatListStored = chatDB.chatRoomDao().getChatRoomList()     // 룸DB에 저장되어있는 채팅방 목록
        var chatListUpdated = chatListGotten.filterNot { it in chatListStored } //서버에서 불러온 채팅방 목록 중 룸DB에 저장되어있지 않은 채팅방들의 리스트
        var chatListDeleted = chatListStored.filterNot { it in chatListGotten } // 룸DB에 저장되어있는 채팅방 목록 중 서버에서 불러온 채팅방 목록에 속하지 않은 채팅방들의 리스트(회원탈퇴로 삭제된 채팅방)

        if(!chatListUpdated.isEmpty()){
            Log.d("업데이트된 채팅방 확인", chatListUpdated.toString())
            for (i: Int in 0..chatListUpdated.size-1){    // 룸DB에 아직 업데이트되지 않은 채팅방을 모두 룸DB에 저장
                if(chatDB.chatRoomDao().getChatRoomIdx(chatListUpdated[i].chatRoomIdx).isNullOrEmpty()){    // 새로 생성된 채팅방일 때 ---- 이 부분은 채팅방 생성 fcm 구현 후 수정 필요할 듯
                    chatDB.chatRoomDao().insert(chatListUpdated[i]) // 새로 생성된 채팅방 룸DB에 추가
                }else{  // 기존 채팅방에 업데이트된 내용이 있을 때
                    chatDB.chatRoomDao().update(chatListUpdated[i]) // 룸DB에서 update()는 primary key를 기준으로 한다
                }

            }
            Log.d("chatDB에 저장된 chatRoomList", chatDB.chatRoomDao().getChatRoomList().toString())
        }else{
            Log.d("업데이트된 채팅방 확인", "없음")
            //Log.d("chatDB에 저장된 chatRoomList", chatDB.chatRoomDao().getChatRoomList().toString())
        }

        if(!chatListDeleted.isEmpty()){
            Log.d("삭제된 채팅방 확인", chatListDeleted.toString())

            for (i: Int in 0..chatListDeleted.size-1){    // 룸DB에 아직 삭제처리되지 않은 채팅방을 모두 룸DB에서 삭제
                chatDB.chatRoomDao().delete(chatListDeleted[i])
            }
            //Log.d("chatDB에 저장된 chatRoomList", chatDB.chatRoomDao().getChatRoomList().toString())
        }else{
            Log.d("삭제된 채팅방 확인", "없음")
            //Log.d("chatDB에 저장된 chatRoomList", chatDB.chatRoomDao().getChatRoomList().toString())
        }
    }

    override fun onGetChatListFailure(code: Int, message: String) {
//        progressOFF()
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //Toast.makeText(activity,"code: $code, message: $message", Toast.LENGTH_LONG).show()
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