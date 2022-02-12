package com.example.duos.ui.main.chat

import android.content.Intent
import android.widget.EditText
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.databinding.ActivityChattingBinding
import androidx.recyclerview.widget.DefaultItemAnimator

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.duos.data.entities.ChatType

import android.util.Log
import android.text.Editable
import android.text.TextWatcher

import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.entities.chat.sendMessageData
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.appointment.AppointmentActivity
import com.example.duos.ui.main.appointment.AppointmentExistView
import com.example.duos.ui.main.appointment.AppointmentInfoActivity
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getUserIdx
import com.example.duos.utils.saveCurrentChatRoomIdx
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//import java.util.*


class ChattingActivity: BaseActivity<ActivityChattingBinding>(ActivityChattingBinding::inflate), SendMessageView, AppointmentExistView {
    //lateinit var binding: ActivityChattingBinding
    private var chatListDatas = ArrayList<ChatRoom>()
    var roomIdx: Int = 0
    lateinit var userId: String
    private var thisUserIdx = getUserIdx()!!
    var partnerIdx: Int = 0 // initAfterBinding에서 ChatListFragment에서 partnerIdx 인텐트 넘겨받음
    private var layoutManager: LayoutManager? = null
    lateinit var chatRoomIdx : String
    lateinit var chattingMessagesRVAdapter: ChattingMessagesRVAdapter
    lateinit var chattingRV: RecyclerView
    lateinit var chattingEt: EditText
    lateinit var chatRoomName: TextView
    //var chatRoomIdx: String = "9af55ffe-17cc-45e9-bc28-a674e6a9785b"
    lateinit var chatDB: ChatDatabase
    lateinit var chatRoom : ChatRoom
    lateinit var viewModel: ViewModel

    private fun addChatItem(senderId: String, body: String, formattedSentAt: String, sentAt:LocalDateTime, type: String) {
        this.runOnUiThread {
            if (type.equals("DATE")) {    // 날짜일때 ex) "2021년 10월 28일"
                val chatMessageItem = ChatMessageItem(senderId, body, formattedSentAt, sentAt, ChatType.CENTER_MESSAGE, chatRoomIdx)
                chattingMessagesRVAdapter.addItem(chatMessageItem)  // 리사이클러뷰에 띄움
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
                chatDB.chatMessageItemDao().insert(chatMessageItem) // 룸DB에 저장
            } else {    // 받은 메세지일때
                val chatMessageItem = ChatMessageItem(senderId, body, formattedSentAt, sentAt, ChatType.LEFT_MESSAGE, chatRoomIdx)
                chattingMessagesRVAdapter.addItem(chatMessageItem)  // 리사이클러뷰에 띄움
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
                // FirebaseMessagingServiceUtil에서 지난메세지 불러오는 API 호출 성공하면 룸DB에 저장되므로 여기서 저장 안해도 됨!
            }
        }
    }

    private fun addChatItem(chatMessageItemData: ChatMessageItem) {
        this.runOnUiThread {
            val type = chatMessageItemData.viewType
            if (type == ChatType.CENTER_MESSAGE) {    // 날짜일때 ex) "2021년 10월 28일"
                chattingMessagesRVAdapter.addItem(chatMessageItemData)  // 리사이클러뷰에 띄움
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
                chatDB.chatMessageItemDao().insert(chatMessageItemData) // 룸DB에 저장
            } else {    // 받은 메세지일때
                chattingMessagesRVAdapter.addItem(chatMessageItemData)  // 리사이클러뷰에 띄움
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
                // FirebaseMessagingServiceUtil에서 지난메세지 불러오는 API 호출 성공하면 룸DB에 저장되므로 여기서 저장 안해도 됨!
            }
        }
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private fun toDate(currentMiliis: Long): String {

        if(currentMiliis != null){

        }
        return SimpleDateFormat("a hh:mm").format(Date(currentMiliis))
    }

    override fun onStart() {
        super.onStart()
        Log.d("생명주기","onStart")
        // 사용자가 백그라운드에서 돌아왔을 때 호출됨
        // 즉 백그라운드에서 푸시알림을 눌러 ChattingActivity로 왔을 때 onCreate가 아닌 onStart부터 호출됨
        // initAfterBinding이 아닌 여기서 api를 호출해서 지난 채팅 메세지 데이터를 띄워줘야할 듯
        getFCMIntent()

        // 약속 여부 받아오기
        if(isNetworkAvailable(this)){   // 인터넷 연결 돼있을 때
            AppointmentService.isAppointmentExist(this, thisUserIdx, partnerIdx)
            Log.d("인터넷 연결 확인", "CONNECTED")
        }else{
            Log.d("인터넷 연결 확인", "DISCONNECTED")
            if (chatDB.chatRoomDao().getAppointmentExist(chatRoomIdx)){ // 잡혀있는 약속이 있을 때
                setAppointmentBtnExist()
            }else{
                setAppointmentBtnNotExist()
            }
        }

    }

    override fun initAfterBinding() {
        Log.d("생명주기","onCreate(initAfterBinding)")
        chattingEt = binding.chattingEt
        chattingRV = binding.chattingMessagesRv
        chatRoomName = binding.chattingTitlePartnerIdTv

        if(intent != null){
            chatRoomIdx = intent.getStringExtra("chatRoomIdx")!!
            chatRoomName.text = intent.getStringExtra("senderId")!!
            partnerIdx = intent.getIntExtra("partnerIdx", 0)
        }

        val userDB = UserDatabase.getInstance(this)!!
        userId = userDB.userDao().getUserNickName(thisUserIdx)

        saveCurrentChatRoomIdx(chatRoomIdx)
        chatDB = ChatDatabase.getInstance(this, ChatDatabase.provideGson())!!
        chatRoom = chatDB.chatRoomDao().getChatRoom(chatRoomIdx)

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModel::class.java)



        var sendBtn: ImageView = binding.chattingSendBtn

        chattingRV.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).setStackFromEnd(true)    //
        chattingRV.setLayoutManager(layoutManager)
        chattingRV.setItemAnimator(DefaultItemAnimator())
        chattingMessagesRVAdapter = ChattingMessagesRVAdapter(chatRoomIdx)
        chattingRV.setAdapter(chattingMessagesRVAdapter)

        // chatting test code
        val currentTime = LocalDateTime.now()
        Log.d("currentTime", currentTime.toString())
        var formattedCurrentTime = getFormattedDateTime(currentTime.toString())
        Log.d("formattedCurrentTime", formattedCurrentTime)

//        addChatItem("userId", "2021년 01월 21일", formattedCurrentTime, currentTime, "DATE")
//        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
//
//        addChatItem(chatRoomName.text.toString(), "안녕하세요~^^", formattedCurrentTime, currentTime,"MESSAGE")
//        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)

        // 날짜 바뀌면 "2022년 01월 21일" 이런식으로 뜨게 하는거 eventTime이 바뀌면 해당 인덱스에 추가하는거 해보다가 말음
//        chatListDatas.apply {
//            add(MessageData("957cfc80-481c-4ae4-88a0-25a9599dd511", "DATE", thisUserIdx, thisUserIdx,"2022년 01월 21일", System.currentTimeMillis())
//            add(MessageData("957cfc80-481c-4ae4-88a0-25a9599dd511", "Message", targetUserIdx, thisUserIdx,"2022년 01월 21일", System.currentTimeMillis()))
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
//        for (i: Int in 0..chatListDatas.size-1){
//            val dateTime = getFormattedDateTime(chatListDatas[i].lastUpdatedAt)
//            val nextDateTime = getFormattedDateTime(chatListDatas[i].lastUpdatedAt)
//            if(dateTime != nextDateTime){
//
//            }
//        }


        // 채팅 EditText focus되면 전송 아이콘(비행기 아이콘) primary색으로 활성화, 아닐때 비활성화
        chattingEt.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(chattingEt.length() > 0){
                    sendBtn.isClickable = true
                    sendBtn.setImageResource(R.drawable.ic_btn_airplane_send_blue)
                } else{
                    sendBtn.isClickable = false
                    sendBtn.setImageResource(R.drawable.ic_btn_airplane_send_gray)
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        sendBtn.setOnClickListener{
            postSendMessage()
        }

        binding.chattingMakePlanBtn.setOnClickListener ({

            if (chatDB.chatRoomDao().getChatRoom(chatRoomIdx).isAppointmentExist) {
                // 약속현황 보기
                val intent = Intent(this, AppointmentInfoActivity::class.java)
                intent.putExtra("chatRoomIdx", chatRoomIdx)
                intent.putExtra("partnerIdx", partnerIdx)
                startActivity(intent)
            }
            else {
                // 약속 잡기
                val intent = Intent(this, AppointmentActivity::class.java)
                intent.putExtra("chatRoomIdx", chatRoomIdx)
                intent.putExtra("partnerIdx", partnerIdx)
                startActivity(intent)
            }
        })

        binding.chattingBackIv.setOnClickListener {
            finish()
        }
    }

    private fun postSendMessage() {
        val messageData = sendMessageData(chatRoomIdx, "MESSAGE",
            thisUserIdx, partnerIdx, chattingEt.text.toString())

        Log.d("메세지 보내기", messageData.toString())

        ChatService.sendMessage(this, messageData.receiverIdx, messageData.senderIdx, messageData.message, messageData.type, messageData.chatRoomIdx)
    }

    fun sendMessage(){
        var sendTime = System.currentTimeMillis()
        Log.d(
            "MESSAGE", sendMessageData(
                chatRoomIdx,
                "MESSAGE",
                thisUserIdx, partnerIdx,
                chattingEt.text.toString()
            ).toString()
        )

        // sendTime과 룸디비의 sentAt 확인해서 addItem 한번 더해주도록 수정 필요

        val chatMessageItem = ChatMessageItem(userId, chattingEt.text.toString(), toDate(sendTime), LocalDateTime.now(), ChatType.RIGHT_MESSAGE, chatRoomIdx)
        chattingMessagesRVAdapter.addItem(chatMessageItem)
        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
        chatDB.chatMessageItemDao().insert(chatMessageItem)
        chattingEt.setText("")
    }

    override fun onSendMessageLoading() {
        //progressON()
        Log.d("로딩중","채팅 메세지 보내기 api")
    }

    override fun onSendMessageSuccess() {
        Log.d("채팅 메세지 보내기 POST", "성공")
        sendMessage()
        //progressOFF()
    }

    override fun onSendMessageFailure(code: Int, message: String) {
        Toast.makeText(this,"code: $code, message: $message", Toast.LENGTH_LONG)
    }

    override fun onNewIntent(intent: Intent?) {
        if (intent == null){
            Log.d("채팅화면일때 인텐트","is null")
        }

        Log.d("채팅화면일때", "1")
        var bundle = intent?.extras
        if(bundle != null){
            Log.d("채팅화면일때", "2")
            val senderId = bundle.getString("senderId")
            val body = bundle.getString("body")
            var type = bundle.getString("type")
            partnerIdx = bundle.getString("partnerIdx")?.toInt() ?: 0
            val sentAtString = bundle.getString("sentAt")
            var sendTime = bundle.getString("sentAt")?.let { getFormattedDateTime(it) }!!
            //var currentTime = toDate(System.currentTimeMillis())

            if (!senderId.isNullOrEmpty() && !body.isNullOrEmpty() && !sendTime.isNullOrEmpty()) {
                // 채팅화면을 마지막으로 백그라운드로 전환했을 때 푸시알림을 누르면 onMessageReceived를 거치지 않고 onNewIntent가 호출되고 senderId가 null으로 와서
                    // .toString()을 통해 ""이 되어버리는 듯.. 그래서 senderId != null했을 때 true가 되어버림.. 그래서 isNullOrBlank()로 해서 false가 되도록 수정
                    // 즉 채팅화면을 마지막으로 백그라운드로 전환했을 때 푸시알림을 눌러도 addChatItem이 되지 않도록 함함
               Log.d("채팅화면일때", "3")
                Log.d("발신자 아이디", senderId)
                if(type.equals("MESSAGE")){
                    //addChatItem(senderId, body, sendTime, LocalDateTime.now(), "MESSAGE")
                        val chatMessageList = chatDB.chatMessageItemDao().getChatMessages(chatRoomIdx)
                    val chatMessageListSize = chatMessageList.size
                    if(chatMessageListSize != 0){
                        for(i: Int in 0..chatMessageListSize-1){
                            addChatItem(chatMessageList[i])
                        }
                    }else{
                        Log.d("주고받은 채팅메세지가","없음~")
                    }

                } else if(type.equals("CREATE_APPOINTMENT")){
                    // 약속 생성 ("약속 잡기" 버튼 -> "약속" 버튼) // FirebaseMessagingServiceUtil에서 이미 약속정보 roomDB에 저장해줌!!
                    setAppointmentBtnExist()
                } else if(type.equals("DELETE_APPOINTMENT")){
                    // 약속 취소 ("약속" 버튼 -> "약속 잡기" 버튼)
                    setAppointmentBtnNotExist()
                } else{
                    // 약속 수정 - 딱히 해줄 거 없을듯?
                }

            }else{  // 채팅화면을 마지막으로 백그라운드로 전환했다가 푸시알림을 통해 다시 왔을 때 여기로 옴, onStart에서 api 호출해줄 것이므로 비워두면 됨
                Log.d("채팅화면일때", "3 - null 존재")
                Log.d("sendTime 확인", sendTime)
            }
//            }
        }else{
            Log.d("채팅화면일때", "2-error")
        }

        super.onNewIntent(intent)
    }

    fun getFCMIntent(){
        // chatRoomIdx만 받도록 수정 (여기서 데이터 받을필요 없이 chatRoomIdx로 이전 채팅 데이터 api 호출하게 하면 됨)
        Log.d("FCM인텐트", "1")
        var bundle = intent?.extras
        if(bundle != null){
            var chatRoomIdx = bundle.getString("chatRoomIdx").toString()
            Log.d("FCM인텐트", "2 - 푸시알림을 통해 채팅화면으로 옴")
            Log.d("chatRoomIdx is", chatRoomIdx)
            if (!chatRoomIdx.isNullOrEmpty()) {
                Log.d("FCM인텐트", "3 - chatRoomIdx를 받아옴")
                // chatRoomIdx 값에 따라 지난 채팅 데이터 가져오는 api 호출
            }else{
                Log.d("FCM인텐트", "3 - chatRoomIdx is null")
            }
//            if (chatRoomIdx != null && senderIdx != null && body != null && sendTime != null) {
//                Log.d("채팅액티비티", "3")
//                updateMessage(chatRoomIdx, "MESSAGE", senderIdx, thisUserIdx, body, sendTime)
//            }else{
//                Log.d("채팅액티비티", "3 - null 존재")
//            }
        }else{
            Log.d("FCM인텐트", "푸시알림을 통해 채팅화면으로 온 것이 아님, 혹은 ")
            // ChatListFragment에서 인텐트로 온 chatRoomIdx 값에 따라 지난 채팅 데이터 가져오는 api 호출
            // 포그라운드에서 온 경우 인텐트를 받음
        }
    }

    @Throws(Exception::class)
    fun getFormattedDateTime(dateTime: String):String {
        // 대상 날짜로 LocalDateTime 만들기
        Log.d("채팅메세지수신시간포매팅 1",dateTime)
        var parsedDateTimeArray = dateTime.split(".")
        var parsedDateTime = parsedDateTimeArray[0]
        Log.d("채팅메세지수신시간포매팅 2", parsedDateTime)

        val parsedLocalDateTime = LocalDateTime.parse(parsedDateTime)
        Log.d("채팅메세지수신시간포매팅 3", parsedLocalDateTime.toString())

        // LocalDateTime에서 필요한 내용 필요한 형식으로 뽑기
//        val yyyyMMdd = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//        val yyyy = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy"))
//        val MM = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("MM"))
//        val dd = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("dd"))
        val time = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("a hh:mm"))
//        println(yyyyMMdd)
//        println(yyyy)
//        println(MM)
//        println(dd)
        Log.d("채팅메세지수신시간포매팅 4",time)

        return time
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("생명주기","onDestroy")
        saveCurrentChatRoomIdx("")
    }

    override fun onAppointmentExistSuccess(appointmentIdx: Int) {
        Log.d("약속여부 성공", appointmentIdx.toString())
        if (appointmentIdx == -1){
            // 약속 없음
            setAppointmentBtnNotExist()
            chatDB.chatRoomDao().updateAppointmentExist(chatRoomIdx, false)
        } else {
            // 약속 존재함
            setAppointmentBtnExist()
            chatDB.chatRoomDao().updateAppointmentExist(chatRoomIdx, true)
            chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx, appointmentIdx)
        }
    }

    override fun onAppointmentExistFailure(code: Int, message: String) {
        Log.d("약속 여부 받아오기 실패", code.toString() + " : " + message)
    }

    fun setAppointmentBtnExist(){
        binding.chattingMakePlanBtn.background = getDrawable(R.drawable.unchecked_check_box)
        binding.chattingMakePlanBtn.setTextColor(
            ContextCompat.getColor(
            applicationContext,
            R.color.dark_gray_B4
        ))
        binding.chattingMakePlanBtn.setText(getString(R.string.chatting_show_plan))
    }

    fun setAppointmentBtnNotExist(){
        binding.chattingMakePlanBtn.background = getDrawable(R.drawable.selected_btn)
        binding.chattingMakePlanBtn.setTextColor(
            ContextCompat.getColor(
                applicationContext,
                R.color.primary
            ))
        binding.chattingMakePlanBtn.setText(getString(R.string.chatting_make_plan))
    }
}