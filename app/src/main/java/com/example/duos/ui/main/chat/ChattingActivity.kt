package com.example.duos.ui.main.chat

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.Looper
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
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.duos.FirebaseMessagingServiceUtil
import com.example.duos.R
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.entities.chat.PagingChatMessageRequestBody
import com.example.duos.data.entities.chat.sendMessageData
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.data.remote.chat.chat.*
import com.example.duos.data.remote.chat.chatList.ChatListService
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.appointment.AppointmentActivity
import com.example.duos.ui.main.appointment.AppointmentExistView
import com.example.duos.ui.main.appointment.AppointmentInfoActivity
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getUserIdx
import com.example.duos.utils.saveCurrentChatRoomIdx
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//import java.util.*


class ChattingActivity: BaseActivity<ActivityChattingBinding>(ActivityChattingBinding::inflate), SendMessageView, AppointmentExistView, PagingChatMessageView, ChatListView {
    lateinit var userId: String
    private var thisUserIdx = 0
    var partnerIdx: Int = 0 // initAfterBinding에서 ChatListFragment에서 partnerIdx 인텐트 넘겨받음
    var createdNewChatRoom : Boolean = false
    private var layoutManager: LayoutManager? = null
    lateinit var chatRoomIdx : String
    lateinit var chattingMessagesRVAdapter: ChattingMessagesRVAdapter
    lateinit var chattingRV: RecyclerView
    lateinit var chattingEt: EditText
    lateinit var chatRoomName: TextView
    lateinit var chatDB: ChatDatabase
    lateinit var dateTimeOfFirstMessageOfLastPage: LocalDateTime
    private var isNextPageAvailable = false // 다음 페이지 유무
    private var pageNum = 0     // 조회할 페이지 page
    private var listNum = 20     // 조회할 페이지 당 채팅 메세지 개수 limit
    lateinit var startForResult: ActivityResultLauncher<Intent>

    override fun onStart() {
        super.onStart()
        Log.d("ChattingActivity 생명주기","onStart")
        saveCurrentChatRoomIdx(chatRoomIdx)                     // 현재 chatRoomIdx를 SharedPreference에 저장

        startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                //val intent = result.data // Handle the Intent //do stuff here
                Log.d("약속버튼 업데이트", "startForResult")
                setAppointmentBtn()
            } else{
                Log.d("약속버튼 업데이트", "startForResult 실패")
            }
        }

        // 재설치 후 채팅방목록 본 적 없는 상태에서 푸시알림 받아서 눌렀을 때 채팅화면으로 이동하면 룸디비에 chatRoom 데이터가 없기때문에 여기서 넣어줌
        // 재설치 후 채팅방목록 본 적 없는 상태에서 채팅을 한 번도 주고받은 적이 없는 사용자에게 채팅메세지가 와서 푸시알림을 눌렀을 때도 룸디비에 chatRoom 데이터 없으므로 여기서 넣어줌
        if(chatDB.chatRoomDao().getChatRoomList().isEmpty()){
            ChatListService.chatList(this, getUserIdx()!!)
        }
    }

    override fun initAfterBinding() {
        Log.d("ChattingActivity 생명주기","onCreate(initAfterBinding)")
        chattingEt = binding.chattingEt
        chattingRV = binding.chattingMessagesRv
        chatRoomName = binding.chattingTitlePartnerIdTv
        var sendBtn: ImageView = binding.chattingSendBtn

        thisUserIdx = getUserIdx()!!
        Log.d("ChattingActivity - initAfterBinding - thisUserIdx", thisUserIdx.toString())

        val userDB = UserDatabase.getInstance(this)!!
        userId = userDB.userDao().getUserNickName(thisUserIdx)  // 내 인덱스로 내 닉네임 가져오기

        chatDB = ChatDatabase.getInstance(this, ChatDatabase.provideGson())!!

        val isFromChatList = intent.getBooleanExtra("isFromChatList", false)
        createdNewChatRoom = intent.getBooleanExtra("createdNewChatRoom", false)
        val isFromPlayerProfile = intent.getBooleanExtra("isFromPlayerProfile", false)

        if(isFromChatList){
            Log.d("ChattingActivity 생명주기", "onStart - isFromChatList "+isFromChatList)
            chatRoomIdx = intent.getStringExtra("chatRoomIdx")!!
            chatRoomName.text = intent.getStringExtra("senderId")!!
            partnerIdx = intent.getIntExtra("partnerIdx", 0)

        } else if (isFromPlayerProfile){
            Log.d("ChattingActivity 생명주기", "onStart - isFromPlayerProfile "+isFromPlayerProfile)
            chatRoomIdx = intent.getStringExtra("targetChatRoomIdx")!!
            chatRoomName.text = intent.getStringExtra("partnerNickName")!!
            partnerIdx = intent.getIntExtra("partnerIdx", 0)

        } else{ // 푸시알림을 '눌러서' 온 경우
            Log.d("ChattingActivity 생명주기", "onStart - getFCMIntent 호출")
            getFCMIntent()
        }

        chattingRV.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).reverseLayout = true
        (layoutManager as LinearLayoutManager).stackFromEnd = true
        chattingRV.setLayoutManager(layoutManager)
        chattingMessagesRVAdapter = ChattingMessagesRVAdapter(chatRoomIdx)
        chattingRV.setAdapter(chattingMessagesRVAdapter)

        // 재설치 후 채팅방목록 본 적 없는 상태에서 푸시알림 받아서 눌렀을 때 채팅화면으로 이동하면 룸디비에 chatRoom 데이터가 없기때문에 여기서 넣어줌
        if(chatDB.chatRoomDao().getChatRoomList().isEmpty() ){
            ChatListService.chatList(this, getUserIdx()!!)
        }

        initView(chatRoomIdx, partnerIdx)

        initScrollListener()

        binding.chattingMakePlanBtn.setOnClickListener {

            if (chatDB.chatRoomDao().getChatRoom(chatRoomIdx).isAppointmentExist) {
                // 약속현황 보기
                val intent = Intent(this, AppointmentInfoActivity::class.java)
                intent.putExtra("chatRoomIdx", chatRoomIdx)
                intent.putExtra("partnerIdx", partnerIdx)
                startForResult.launch(intent)
                //startActivity(intent)
            } else {
                // 약속 잡기
                val intent = Intent(this, AppointmentActivity::class.java)
                intent.putExtra("chatRoomIdx", chatRoomIdx)
                intent.putExtra("partnerIdx", partnerIdx)
                startForResult.launch(intent)
                //startActivity(intent)
            }
        }

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

        binding.chattingBackIv.setOnClickListener {
            finish()
        }
    }

    private fun initView(chatRoomIdx: String, partnerIdx: Int){
        pageNum = 0
        saveCurrentChatRoomIdx(chatRoomIdx)                     // 현재 chatRoomIdx를 SharedPreference에 저장

        chattingRV.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).reverseLayout = true
        (layoutManager as LinearLayoutManager).stackFromEnd = true
        chattingRV.setLayoutManager(layoutManager)
        chattingMessagesRVAdapter = ChattingMessagesRVAdapter(chatRoomIdx)
        chattingRV.setAdapter(chattingMessagesRVAdapter)

        // 약속 여부 받아오기 & 메세지 페이징 or 룸디비에서 load
        if(isNetworkAvailable(this)){   // 인터넷 연결 돼있을 때
            AppointmentService.isAppointmentExist(this, thisUserIdx, partnerIdx)
            loadMessages()
            Log.d("인터넷 연결 확인", "CONNECTED")
        }else{
            Log.d("인터넷 연결 확인", "DISCONNECTED")
            if (chatDB.chatRoomDao().getAppointmentExist(chatRoomIdx)){ // 잡혀있는 약속이 있을 때
                setAppointmentBtnExist()
            }else{
                setAppointmentBtnNotExist()
            }

            showToast("네트워크 상태를 확인해주세요.")

            val storedMessages = chatDB.chatMessageItemDao().getChatMessages(chatRoomIdx)
            if(storedMessages != null){
                chattingMessagesRVAdapter.addPagingMessages(storedMessages)
                chattingRV.scrollToPosition(0)
            } else{
                showToast("주고받은 채팅 메세지가 없습니다.")
                Log.d("ChattingActivity - initView","채팅방 생성 이후 아무 메세지도 주고받지 않음")
            }
        }
    }

    private fun setAppointmentBtn(){

        // 약속 여부 받아오기 & 메세지 페이징 or 룸디비에서 load
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

            showToast("네트워크 상태를 확인해주세요.")
        }
    }

    private fun addChatItem(chatMessageItemData: ChatMessageItem) {
        this.runOnUiThread {
            val type = chatMessageItemData.viewType
            if (type == ChatType.CENTER_MESSAGE) {    // 날짜일때 ex) "2021년 10월 28일"
                chattingMessagesRVAdapter.addItem(chatMessageItemData)  // 리사이클러뷰에 띄움
                // FirebaseMessagingServiceUtil에서 지난메세지 불러오는 API 호출 성공하면 룸DB에 저장되므로 여기서 저장 안해도 됨!
            } else {    // 받은 메세지일때
                chattingMessagesRVAdapter.addItem(chatMessageItemData)  // 리사이클러뷰에 띄움
                chattingRV.scrollToPosition(0)
                // FirebaseMessagingServiceUtil에서 지난메세지 불러오는 API 호출 성공하면 룸DB에 저장되므로 여기서 저장 안해도 됨!
            }
        }
    }

    private fun postSendMessage() {
        val messageData = sendMessageData(chatRoomIdx, "MESSAGE",
            thisUserIdx, partnerIdx, chattingEt.text.toString())

        Log.d("메세지 보내기", messageData.toString())

        ChatService.sendMessage(this, messageData.chatRoomIdx,messageData.type, messageData.senderIdx, messageData.receiverIdx,  messageData.message  )
    }

    fun sendMessage(chatMessageIdx: String){
        var sendTime = System.currentTimeMillis()

        var parsedChatMessageIdx = chatMessageIdx.split("@")
        var uuid = parsedChatMessageIdx[1]

        //Log.d("ChattingActvity - sendMessage 날짜변경선 추가 전 - lastAddedChatMessageId", lastAddedChatMessageId.toString())
        // 첫 메세지일때 날짜변경선 추가 ... 채팅방 자체의 첫 메세지 일 떄 말고, 자정 지나고 첫 메세지일때도 추가되도록 수정 필요
        if(chattingMessagesRVAdapter.itemCount == 0){  //지금 보내는 메세지가 채팅방의 처음 메세지일 때
            val parsedLocalDateTime = LocalDateTime.now()
            val date = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
            val dateItem = ChatMessageItem("date", date, "date", parsedLocalDateTime, ChatType.CENTER_MESSAGE, chatRoomIdx, "date"+ uuid)
            chattingMessagesRVAdapter.addItem(dateItem)
            chatDB.chatMessageItemDao().insert(dateItem)
//            Log.d("채팅보내기 - 날짜변경선 추가 후 - lastAddedChatMessageId", lastAddedChatMessageId.toString())
        }

        val chatMessageItem = ChatMessageItem(userId, chattingEt.text.toString(), toDate(sendTime), LocalDateTime.now(), ChatType.RIGHT_MESSAGE, chatRoomIdx, uuid)
        chattingMessagesRVAdapter.addItem(chatMessageItem)
        chattingRV.scrollToPosition(0)
        chattingEt.setText("")
        chatDB.chatMessageItemDao().insert(chatMessageItem)
//        Log.d("채팅보내기 - lastAddedChatMessageId", lastAddedChatMessageId.toString())
    }

    override fun onSendMessageLoading() {
        //progressON()
        Log.d("로딩중","채팅 메세지 보내기 api")
    }

    override fun onSendMessageSuccess(sendMessageResultData: SendMessageResultData) {
        Log.d("채팅 메세지 보내기 POST", "성공")

        sendMessage(sendMessageResultData.chatMessageIdx)
        //progressOFF()
    }

    override fun onSendMessageFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요.")
        //Toast.makeText(this,"code: $code, message: $message", Toast.LENGTH_LONG)
    }

    override fun onNewIntent(intent: Intent?) {
        // 아래 두줄 변경됨 -> 채팅화면에 있지만 다른 채팅방의 메세지를 받았을 때, 푸시알림을 눌렀을때 말고 받았을 때 onNewIntent는 호출되지 않는 것 확인. 언제고쳤지?
        // 채팅화면에 있지만 다른 채팅방의 메세지를 받았을 때 onNewIntent는 호출되지만, 아직 푸시알림을 누르지 않았기 때문에 isAlarmed=false이며 아무것도 안하고 끝나야 맞음.
        // 푸시알림을 누른 순간에 또 onNewIntent가 호출될 것임. 그 때 isAlarmed가 true가 됨
        if (intent == null){
            Log.d("채팅화면일때 인텐트","is null")
        }

        Log.d("채팅화면일때", "1")
        var bundle = intent?.extras
        if(bundle != null){
            Log.d("채팅화면일때", "2")
            //val senderId = bundle.getString("senderId")
            //val body = bundle.getString("body")
            var type = bundle.getString("type")
            var chatRoomIdxOfReceivcedMessage = bundle.getString("chatRoomIdx").toString()
            //partnerIdx = bundle.getString("partnerIdx")?.toInt() ?: 0
//            val sentAtString = bundle.getString("sentAt")
//            var sendTime = bundle.getString("sentAt")?.let { getFormattedDateTime(it) }!!
            //var currentTime = toDate(System.currentTimeMillis())
            val byPushAlarmClick = bundle.getBoolean("byPushAlarmClick", true)

            Log.d("onNewIntent - type", type.toString())
            Log.d("onNewIntent - byPushAlarmClick", byPushAlarmClick.toString())

            if (!type.isNullOrEmpty()) {
                // 채팅화면을 마지막으로 백그라운드로 전환했을 때 푸시알림을 누르면 onMessageReceived를 거치지 않고 onNewIntent가 호출되고 senderId가 null으로 와서
                    // .toString()을 통해 ""이 되어버리는 듯.. 그래서 senderId != null했을 때 true가 되어버림.. 그래서 isNullOrBlank()로 해서 false가 되도록 수정
                    // 즉 채팅화면을 마지막으로 백그라운드로 전환했을 때 푸시알림을 눌러도 addChatItem이 되지 않도록 함함
               Log.d("채팅화면일때", "3")
                //Log.d("발신자 아이디", senderId)
                if(chatRoomIdxOfReceivcedMessage.equals(chatRoomIdx)){
                    Log.d("ChattingActivivty 생명주기", "onNewIntent - 채팅화면이며, 받은 메세지가 현재 보고있는 채팅방일 때")
                    //Log.d("onNewIntent","채팅화면이며, 받은 메세지가 현재 보고있는 채팅방일 때")
                    if(type.equals("MESSAGE")){

                        val messageItem = bundle.getParcelable<ChatMessageItem>("messageItem")
                        if(messageItem != null){    // 포그라운드에서
                            Log.d("ChattingActivitiy - messageItem", "is not null")
                            // intent로 messageItem 객체 받아서 messageItems에 add하기
                            val chatMessageItem = bundle.getParcelable<ChatMessageItem>("messageItem")!!


                            val sentDateTime = chatMessageItem.sentAt

                            // 날짜가 바뀌었다면 날짜변경선 추가
                            if(chattingMessagesRVAdapter.itemCount != 0){
                                val lastDateTime = chattingMessagesRVAdapter.getItem(0)?.sentAt!!
                                if (isDateChanged(sentDateTime, lastDateTime)){
                                    val dateItem = ChatMessageItem(
                                        "date",
                                        getFormattedDate(sentDateTime.toString()),
                                        "date",
                                        sentDateTime,
                                        ChatType.CENTER_MESSAGE,
                                        chatRoomIdx,
                                        "date" + sentDateTime
                                    )
                                    chattingMessagesRVAdapter.addItem(dateItem)
                                    //messageItems.add(dateItem)
                                    Log.d("날짜변경선 추가 후", dateItem.toString())
                                }
                            } else {    // 첫 메세지면 무조건 날짜변경선 추가
                                val dateItem = ChatMessageItem(
                                    "date",
                                    getFormattedDate(sentDateTime.toString()),
                                    "date",
                                    sentDateTime,
                                    ChatType.CENTER_MESSAGE,
                                    chatRoomIdx,
                                    "date" + sentDateTime
                                )
                                chattingMessagesRVAdapter.addItem(dateItem)
                                //messageItems.add(dateItem)
                                Log.d("첫 메세지 - 날짜변경선 추가 후", dateItem.toString())
                            }

                            chattingMessagesRVAdapter.addItem(chatMessageItem)

                            chattingRV.scrollToPosition(0)
                        } else{
                            // 백그라운드에서 왔으므로 onMessageReceived를 거치지 않음(messageItem이 null) -> page 다시 load
                            initView(chatRoomIdx, partnerIdx)
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
                } else{
                    Log.d("onNewIntent","채팅화면이며, 받은 메세지가 다른 사용자와의 채팅방일 때")

                    // 해당 채팅방이 현재 룸디비에 존재하는지 확인 (채팅을 한번도 주고받은 적 없는 사용자에게 온 알림이라면, 아직 룸디비에 없을 수 있기 때문!)
                    val chatRoomList = chatDB.chatRoomDao().getChatRoomList()
                    var isChatRoomIdxOfReceivcedMessageExist = false
                    for(i: Int in 0..chatRoomList.size-1){
                        val _chatRoomIdx = chatDB.chatRoomDao().getChatRoomList().get(i).chatRoomIdx
                        if(_chatRoomIdx.equals(chatRoomIdxOfReceivcedMessage)){
                            isChatRoomIdxOfReceivcedMessageExist = true
                        }
                    }

                    if(!isChatRoomIdxOfReceivcedMessageExist){
                        // 재설치 후 채팅방 목록을 봤는지와는 상관없이 아래 경우에 다 작동해야 정상.. 테스트 필요
                            // 사용자 A와의 채팅화면에 있는데 채팅을 한 번도 주고받은 적이 없는 사용자 B에게 채팅메세지가 와서 푸시알림을 눌렀을 때
                            // 룸디비에 chatRoom 데이터가 없기때문에 여기서 넣어줌
                        if(chatDB.chatRoomDao().getChatRoomList().isEmpty()){
                            ChatListService.chatList(this, getUserIdx()!!)
                        }
                    }

                    chatRoomIdx = chatRoomIdxOfReceivcedMessage
                    chatRoomName.text = chatDB.chatRoomDao().getPartnerId(chatRoomIdx)
                    partnerIdx = chatDB.chatRoomDao().getChatRoom(chatRoomIdx).participantIdx!!
                    initView(chatRoomIdx, partnerIdx)
                }
            }else{  // 채팅화면을 마지막으로 백그라운드로 전환했다가 푸시알림을 통해 다시 왔을 때 여기로 옴, onStart에서 api 호출해줄 것이므로 비워두면 됨
                Log.d("채팅화면일때", "3 - null 존재")
                //Log.d("sendTime 확인", sendTime)
            }
        }else{
            Log.d("채팅화면일때", "2-error")
        }

        super.onNewIntent(intent)
    }

    fun getFCMIntent(){
        Log.d("ChattingActivivty 생명주기", "getFCMIntent - onStart에서 호출되도록 해놓음")
        Log.d("getFCMIntent 시작", "채팅화면 외 화면을 마지막으로 백그라운드 or 앱 종료 후 백그라운드에서 푸시알림 누른 경우")
        // 원래는 페이징으로 띄워주거나 update된 걸 저 아래 코드로 띄워줘야하는데, 일단 모든 채팅 메세지 가져오도록 함
        var bundle = intent?.extras

        if(bundle != null){
            val chatRoomIdxByFCM = bundle.getString("chatRoomIdx").toString()
//            val senderId = bundle.getString("senderId").toString()
//            partnerIdx = bundle.getString("partnerIdx")?.toInt() ?: 0
            Log.d("chatRoomIdxByFCM", chatRoomIdxByFCM)

            val chatRoomData = chatDB.chatRoomDao().getChatRoom(chatRoomIdxByFCM)
            Log.d("chatRoomData", chatRoomData.toString())
            val senderId = chatRoomData.chatRoomName
            partnerIdx = chatRoomData.participantIdx!!
            val type = bundle.getString("type").toString()
            Log.d("getFCMIntent", "푸시알림을 통해 채팅화면으로 옴")  // 페이징 처리해주니까 chatRoomIdx, partnerIdx, chatRoomName 세팅해주는거말고 할일 X
            Log.d("getFCMIntent - chatRoomIdx", chatRoomIdxByFCM)
            Log.d("getFCMIntent - senderId", senderId)
            Log.d("getFCMIntent - partnerIdx", partnerIdx.toString())
            Log.d("getFCMIntent - type", type)

            chatRoomIdx = chatRoomIdxByFCM
            chatRoomName.text = senderId
        }else{
            Log.d("onStart", "푸시알림을 통해 채팅화면으로 온 것이 아님 근데 채팅방에서 이동, 혹은 파트너세부화면의 채팅하기눌러서 이동한 경우는 위에서 다 처리해줌.. ")
            // 이미 initAfterBinding에서 intent로 chatRoomIdx를 받음
            // ChatListFragment에서 인텐트로 온 chatRoomIdx 값에 따라 지난 채팅 데이터 가져오는 api 호출
            // 포그라운드에서 온 경우 인텐트를 받음
        }
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private fun toDate(currentMiliis: Long): String {

        if(currentMiliis != null){

        }
        return SimpleDateFormat("a hh:mm").format(Date(currentMiliis))
    }

    fun getFormattedDate(dateTime: String):String {
        // 대상 날짜로 LocalDateTime 만들기
        //Log.d("날짜변경선 포매팅 1",dateTime)
        var parsedDateTimeArray = dateTime.split(".")
        var parsedDateTime = parsedDateTimeArray[0]
        //Log.d("날짜변경선 포매팅 2", parsedDateTime)

        val parsedLocalDateTime = LocalDateTime.parse(parsedDateTime)
        //Log.d("날짜변경선 포매팅 3", parsedLocalDateTime.toString())

        // LocalDateTime에서 필요한 내용 필요한 형식으로 뽑기
//        val yyyyMMdd = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
//        val yyyy = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy"))
//        val MM = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("MM"))
//        val dd = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("dd"))
        val time = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
//        println(yyyyMMdd)
//        println(yyyy)
//        println(MM)
//        println(dd)
        Log.d("날짜변경선 포매팅",time)

        return time
    }

    @Throws(Exception::class)
    fun getFormattedDateTime(dateTime: String):String {
        // 대상 날짜로 LocalDateTime 만들기
        //Log.d("채팅메세지수신시간포매팅 1",dateTime)
        var parsedDateTimeArray = dateTime.split(".")
        var parsedDateTime = parsedDateTimeArray[0]
        //Log.d("채팅메세지수신시간포매팅 2", parsedDateTime)

        val parsedLocalDateTime = LocalDateTime.parse(parsedDateTime)
        //Log.d("채팅메세지수신시간포매팅 3", parsedLocalDateTime.toString())

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
        //Log.d("채팅메세지수신시간포매팅 4",time)

        return time
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("ChattingActivity 생명주기","onDestroy")
        saveCurrentChatRoomIdx("")
        chatDB.chatMessageItemDao().deleteAll(chatRoomIdx)
        val itemCount = chattingMessagesRVAdapter.itemCount
        if(itemCount >= 50){  // 리사이클러뷰에 데이터가 50개보다 많거나 같으면 listNum(=50)만큼 룸디비에 저장
            for(i: Int in 0..listNum-1){
                chattingMessagesRVAdapter.getItem(i)?.let { chatDB.chatMessageItemDao().insert(it) }
            }
        } else{ // 50개보다 적으면 itemCount만큼 룸디비에 저장
            for(i: Int in 0..itemCount-1){
                chattingMessagesRVAdapter.getItem(i)?.let { chatDB.chatMessageItemDao().insert(it) }
            }
        }
    }

    override fun onAppointmentExistSuccess(isExisted : Boolean, appointmentIdx : Int) {
        Log.d("약속여부 성공", appointmentIdx.toString())
        if (appointmentIdx == -1){
            // 약속 없음
            setAppointmentBtnNotExist()
            chatDB.chatRoomDao().updateAppointmentExist(chatRoomIdx, false)
            chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx, null)
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

    override fun onPagingChatMessageSuccess(pagingChatMessageResult: PagingChatMessageResult) {
        val lastVisibleItemPosition = (layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
        val listSize = pagingChatMessageResult.currentItemCnt
        isNextPageAvailable = pagingChatMessageResult.isNextPageAvailable
        //var messageList : ArrayList<MessageListData> = ArrayList<MessageListData>()
        var messageList = pagingChatMessageResult.messageList
        var messageItems : ArrayList<ChatMessageItem> = ArrayList<ChatMessageItem>()

        for(i: Int in 0..listSize - 2){
            messageItems.add(convertMessageListDataToChatMessageItem(messageList[i]))
            if(isDateChanged(messageList[i].sentAt, messageList[i+1].sentAt)){
                val sentAtLocalDateTime = messageList[i].sentAt
                val dateItem = ChatMessageItem("date", getFormattedDate(sentAtLocalDateTime.toString()), "date", sentAtLocalDateTime, ChatType.CENTER_MESSAGE, chatRoomIdx, "date" + sentAtLocalDateTime)
                messageItems.add(dateItem)
            }
            //Log.d("for문 "+i, messageItems.toString())
        }

        messageItems.add(convertMessageListDataToChatMessageItem(messageList[listSize - 1]))

        if(!isNextPageAvailable){   // 제일 오래된 페이지(마지막 페이지)라면 맨 첫 메세지 위에 날짜변경선 추가
            val firstDateItem = ChatMessageItem("date", getFormattedDate(messageList[messageList.size - 1].sentAt.toString()), "date", messageList[0].sentAt, ChatType.CENTER_MESSAGE, chatRoomIdx, "date"+messageList[0].sentAt)
            messageItems.add(firstDateItem)
        }

        if(pageNum == 0){   // 제일 최근페이지일 때 페이지의 첫번째 메세지(오래된 메세지)의 dateTime 저장함. 제일 최근페이지이므로 마지막에 날짜변경선 추가되는지 확인 불필요
            dateTimeOfFirstMessageOfLastPage = messageList[messageList.size - 1].sentAt
            chattingMessagesRVAdapter.setPagingMessages(messageItems)
            chattingRV.scrollToPosition(0)
        } else {     // 불러온 페이지의 마지막 메세지의 날짜와 그 아래 이미 로드된 페이지의 첫 메세지의 날짜가 다르면 날짜변경선을 두 페이지 사이에 추가
            //Log.d("날짜변경선 추가 전", messageItems.toString())
            if (isDateChanged(messageList[0].sentAt, dateTimeOfFirstMessageOfLastPage)){
                val sentAt = messageList[0].sentAt
                val sentAtLocalDateTime = messageList[0].sentAt
                val dateItem = ChatMessageItem(
                    "date",
                    getFormattedDate(sentAtLocalDateTime.toString()),
                    "date",
                    sentAt,
                    ChatType.CENTER_MESSAGE,
                    chatRoomIdx,
                    "date" + sentAt
                )
                messageItems.add(0, dateItem)
                //Log.d("날짜변경선 추가 후", messageItems.toString())
            }
            dateTimeOfFirstMessageOfLastPage = pagingChatMessageResult.messageList[messageList.size - 1].sentAt
            // 위의 조건문에서 기존 dateTimeOfFirstMessageOfLastPage가 필요하므로 pageNum == 0일때랑은 다르게 분기처리해줌. 조건문 끝나고 첫메세지의 dateTime 저장
            chattingMessagesRVAdapter.run {
                setLoadingView(false)
                addPagingMessages(messageItems)
                chattingRV.smoothScrollToPosition(lastVisibleItemPosition - 1)  // -1하든 안하든, 아예 이 줄 생략하든 똑같은듯........
            }
        }

        pageNum++
    }

    override fun onPagingChatMessageFailure(code: Int, message: String) {
        showToast(message)
    }

    private fun loadMessages(){
        val pagingMessageRequestBody = PagingChatMessageRequestBody(thisUserIdx, chatRoomIdx, pageNum, listNum)
        Log.d("loadMessages() - pagingMessageRequestBody", pagingMessageRequestBody.toString())
        ChatService.pagingChatMessage(this, pagingMessageRequestBody)
    }

    private fun loadMoreMessages(){
        chattingMessagesRVAdapter.setLoadingView(true)
        val pagingMessageRequestBody = PagingChatMessageRequestBody(thisUserIdx, chatRoomIdx, pageNum, listNum)
        Log.d("loadMoreMessages() - pagingMessageRequestBody", pagingMessageRequestBody.toString())
        ChatService.pagingChatMessage(this, pagingMessageRequestBody)
//        Handler(Looper.getMainLooper()).postDelayed({
//            ChatService.pagingChatMessage(this, pagingMessageRequestBody)
//        }, 1000)  // loading ui 확인 위해 1초 delay
    }

    private fun initScrollListener(){
        chattingRV.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                    if(hasNextPage()){
                        val lastVisibleItem = (layoutManager as LinearLayoutManager)
                            .findLastCompletelyVisibleItemPosition()

                        //Log.d("initScrollListener - lastVisibleItem", (lastVisibleItem).toString())
                        if (dy < 0 && lastVisibleItem == chattingMessagesRVAdapter.itemCount - 1 && isNextPageAvailable) {
                            loadMoreMessages()
                            setHasNextPage(false)
                        }
//                        if ((layoutManager as LinearLayoutManager).itemCount <= lastVisibleItem + n) {
//                            loadMoreMessages()
//                            setHasNextPage(false)
//                        }
                    }
            }
        })
    }

    fun convertMessageListDataToChatMessageItem(messageListData: MessageListData): ChatMessageItem{
        val chatRoomIdx = messageListData.chatRoomIdx
        val senderIdx = messageListData.senderIdx

        val senderId = getSenderId(chatRoomIdx, senderIdx)
        val body = messageListData.message
        val sentAt = messageListData.sentAt
        val sentAtLocalDateTime = messageListData.sentAt
        //val sentAt = messageListData.sentAt
        val formattedSentAt = getFormattedDateTime(sentAtLocalDateTime.toString())
        val viewType = getViewType(senderIdx)
        val chatMessageIdx = messageListData.uuid
        return ChatMessageItem(senderId, body, formattedSentAt, sentAt, viewType, chatRoomIdx, chatMessageIdx)
    }

    fun getViewType(senderIdx: Int): Int{
        val viewType: Int
        if(senderIdx == getUserIdx()){  // 사용자 본인이 보낸 메세지
            viewType = 2
        }else{
            viewType = 0    // 사용자가 받은 메세지
        }
        return viewType
    }

    fun getSenderId(chatRoomIdx: String, senderIdx: Int): String{
        val senderId: String
        var userDB = UserDatabase.getInstance(this)!!
        if(senderIdx == getUserIdx()){
            senderId = getUserIdx()?.let { userDB.userDao().getUserNickName(it) }.toString()
        } else{
            senderId = chatDB.chatRoomDao().getPartnerId(chatRoomIdx)
        }
        return senderId
    }

    private fun hasNextPage(): Boolean {
        return isNextPageAvailable
    }

    private fun setHasNextPage(hasNextPage: Boolean) {
        isNextPageAvailable = hasNextPage
    }

    private fun isDateChanged(lastMessageDateTime: LocalDateTime, currentMessageDateTime: LocalDateTime): Boolean{
        val lastMessageDate = lastMessageDateTime.toLocalDate()
        val currentMessageDate = currentMessageDateTime.toLocalDate()
        //Log.d("isDateChanged - lastMessageDate", lastMessageDate.toString())
        //Log.d("isDateChanged - currentMessageDate", currentMessageDate.toString())
        return !lastMessageDate.isEqual(currentMessageDate) // 이전 날짜랑 다르면 true 반환
    }

    override fun onGetChatListSuccess(chatList: List<ChatRoom>) {
        var chatListGotten = chatList   // 서버에서 불러온 채팅방 목록

        Log.d("채팅방 확인", chatDB.chatRoomDao().getChatRoomList().toString())

        // 룸디비에 변경된/추가된 채팅방 저장
        var chatListStored = chatDB.chatRoomDao().getChatRoomList()     // 룸DB에 저장되어있는 채팅방 목록
        var chatListUpdated = chatListGotten.filterNot { it in chatListStored } //서버에서 불러온 채팅방 목록 중 룸DB에 저장되어있지 않은 채팅방들의 리스트

        if(!chatListUpdated.isEmpty()){
            Log.d("업데이트된 채팅방 확인", chatListUpdated.toString())
            for (i: Int in 0..chatListUpdated.size-1){    // 룸DB에 아직 업데이트되지 않은 채팅방을 모두 룸DB에 저장
                if(chatDB.chatRoomDao().getChatRoomIdx(chatListUpdated[i].chatRoomIdx).isNullOrEmpty()){    // 새로 생성된 채팅방일 때 ---- 이 부분은 채팅방 생성 fcm 구현 후 수정 필요할 듯
                    chatDB.chatRoomDao().insert(chatListUpdated[i]) // 새로 생성된 채팅방 룸DB에 추가
//                    if(chatListUpdated[i].lastAddedChatMessageId <= 0){
//                        chatDB.chatRoomDao().updateLastAddedChatMessageId(chatListUpdated[i].chatRoomIdx, -1)
//                    }
                }else{  // 기존 채팅방에 업데이트된 내용이 있을 때
                    chatDB.chatRoomDao().update(chatListUpdated[i]) // 룸DB에서 update()는 primary key를 기준으로 한다
                }
            }
            Log.d("chatDB에 저장된 chatRoomList", chatDB.chatRoomDao().getChatRoomList().toString())
        }else{
            Log.d("업데이트된 채팅방 확인", "없음")
            Log.d("chatDB에 저장된 chatRoomList", chatDB.chatRoomDao().getChatRoomList().toString())
        }
    }

    override fun onGetChatListFailure(code: Int, message: String) {
        Log.d("FirebaseMessagingServiceUtil", "onGetChatListFailure")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d("ChattingActivity 생명주기","onRestart")
    }

    override fun onStop() {
        super.onStop()
        Log.d("ChattingActivity 생명주기","onStop")
    }

    override fun onPause() {
        super.onPause()
        Log.d("ChattingActivity 생명주기","onPause")
    }

    override fun onResume() {
        super.onResume()
        Log.d("ChattingActivity 생명주기","onResume")
    }
}