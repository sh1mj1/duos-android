package com.example.duos.ui.main.chat

import android.content.Intent
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
import android.widget.Toast
import com.example.duos.R
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.entities.chat.sendMessageData
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.data.remote.chat.chat.appointment.AppointmentService
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.chat.appointment.AppointmentActivity
import com.example.duos.utils.getUserIdx
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

//import java.util.*


class ChattingActivity: BaseActivity<ActivityChattingBinding>(ActivityChattingBinding::inflate), SendMessageView {
    //lateinit var binding: ActivityChattingBinding
    private var chatListDatas = ArrayList<ChatRoom>()
    var roomIdx: Int = 0
    var userId: String = "tennis01"     // 룸디비에서 userIdx로 userId(=userNickname) 조회하도록 수정 필요
    var partnerId: String = "djeikd0620"    // 인텐트로 넘겨받은 partnerIdx로 룸디비에서 partnerID(=chatRoomName) 가져오도록 수정 필요
    var thisUserIdx = 76    // ChatListFragment에서 userIdx넘겨받거나, 룸디비에서 userIdx 가져오도록 수정 필요
    var targetUserIdx = 110 // ChatListFragment에서 partnerIdx 인텐트 넘겨받도록 수정 필요
    private var layoutManager: LayoutManager? = null
    lateinit var chattingMessagesRVAdapter: ChattingMessagesRVAdapter
    lateinit var chattingRV: RecyclerView
    lateinit var chattingEt: EditText

    // 리사이클러뷰에 채팅 추가
//    private fun addChat(data: MessageData) {
//        this.runOnUiThread {
//
//            if (data.type.equals("DATE")) {    //
//                chattingMessagesRVAdapter.addItem(
//                    MessageItem(
//                        userId,
//                        data.messageBody,
//                        toDate(data.sendTime),
//                        ChatType.CENTER_MESSAGE
//                    )
//                )
//                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
//            } else {
//                chattingMessagesRVAdapter.addItem(
//                    MessageItem(
//                        userId,
//                        data.messageBody,
//                        toDate(data.sendTime),
//                        ChatType.LEFT_MESSAGE
//                    )
//                )
//                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
//            }
//        }
//    }

    private fun addChatItem(senderId: String, body: String, sentAt:String, type: String) {
        this.runOnUiThread {
            if (type.equals("DATE")) {    //
                chattingMessagesRVAdapter.addItem(
                    ChatMessageItem(senderId, body, sentAt, ChatType.CENTER_MESSAGE)
                )
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
            } else {
                chattingMessagesRVAdapter.addItem(
                    ChatMessageItem(senderId, body, sentAt, ChatType.LEFT_MESSAGE)
                )
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
            }
        }
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private fun toDate(currentMiliis: Long): String {

        if(currentMiliis != null){

        }
        return SimpleDateFormat("a hh:mm").format(Date(currentMiliis))
    }

    override fun initAfterBinding() {
        chattingEt = binding.chattingEt
        chattingRV = binding.chattingMessagesRv

        var sendBtn: ImageView = binding.chattingSendBtn

        chattingRV.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        (layoutManager as LinearLayoutManager).setStackFromEnd(true)    //
        chattingRV.setLayoutManager(layoutManager)
        chattingRV.setItemAnimator(DefaultItemAnimator())
        chattingMessagesRVAdapter = ChattingMessagesRVAdapter()
        chattingRV.setAdapter(chattingMessagesRVAdapter)

        // chatting test code
        var currentTime = toDate(System.currentTimeMillis())

        addChatItem("userId", "2021년 01월 21일", currentTime, "DATE")
        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)

        addChatItem("senderId", "상대방이 보낸 메세지입니다.", currentTime, "MESSAGE")
        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)

         getFCMIntent()

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

//        chattingEt.setOnFocusChangeListener(object : View.OnFocusChangeListener {
//            override fun onFocusChange(view: View, hasFocus: Boolean) {
//                if (hasFocus || !chattingEt.text.equals("")) {
//                    sendBtn.setImageResource(R.drawable.ic_btn_airplane_send_blue)
//                } else {
//                    sendBtn.setImageResource(R.drawable.ic_btn_airplane_send_gray)
//                }
//            }
//        })
        sendBtn.setOnClickListener{
            postSendMessage()
            sendMessage()
        }

        binding.chattingMakePlanBtn.setOnClickListener ({
            val intent = Intent(this, AppointmentActivity::class.java)
            startActivity(intent)
        })

        binding.chattingBackIv.setOnClickListener {
            finish()
        }


    }

    private fun postSendMessage() {
        val messageData = sendMessageData("9af55ffe-17cc-45e9-bc28-a674e6a9785b", "MESSAGE",
            thisUserIdx, targetUserIdx, chattingEt.text.toString())

        ChatService.sendMessage(this, messageData.receiverIdx, messageData.senderIdx, messageData.message, messageData.type, messageData.chatRoomIdx)
    }

    fun sendMessage(){
        var sendTime = System.currentTimeMillis()
        Log.d(
            "MESSAGE", sendMessageData(
                "957cfc80-481c-4ae4-88a0-25a9599dd511",
                "MESSAGE",
                thisUserIdx, targetUserIdx,
                chattingEt.text.toString()
            ).toString()
        )

        chattingMessagesRVAdapter.addItem(
            ChatMessageItem(
                userId,
                chattingEt.text.toString(),
                toDate(sendTime),
                ChatType.RIGHT_MESSAGE
            )
        )
        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
        chattingEt.setText("")
    }

    override fun onSendMessageLoading() {
        progressON()
        Log.d("로딩중","채팅 메세지 보내기 api")
        Handler(Looper.getMainLooper()).postDelayed(Runnable { progressOFF() }, 3500)
    }

    override fun onSendMessageSuccess() {
        Log.d("채팅 메세지 보내기 POST", "성공")
    }

    override fun onSendMessageFailure(code: Int, message: String) {
        Toast.makeText(this,"code: $code, message: $message", Toast.LENGTH_LONG)
    }

//    override fun onNewIntent(intent: Intent?) {
////        getFCMIntent()
//        if (intent == null){
//            Log.d("인텐트","is null")
//        }
//
//        var bundle = intent?.extras
//        val chatRoomIdx = (bundle?.getString("chatRoomIdx")?:"null").toString()
//        val type= (bundle?.getString("type")?:"MESSAGE").toString()
//        val body = bundle?.getString("body").toString()
//        val senderIdx = bundle?.getString("senderIdx").toString()
//        var sentAt = bundle?.getString("sentAt").toString()
//        val senderId = bundle?.getString("senderId").toString()
//        sentAt = getFormattedDateTime(sentAt)
//
////        val chatRoomIdx = intent?.getStringExtra("chatRoomIdx")?:"null"
////        val type = intent?.getStringExtra("type")?:"MESSAGE"
////        val body = intent?.getStringExtra("body")?:"null"
////        val senderIdx = (intent?.getStringExtra("senderIdx")?:"null").toInt()
////        var sentAt = intent?.getStringExtra("sentAt")?:"null"
////        val senderId = intent?.getStringExtra("senderId")?:"null"
////        sentAt = getFormattedDateTime(sentAt)
//
//        Log.d("포그라운드-알림페이로드"," 1")
//        addChatItem(senderId, body, sentAt, type)
//
//        super.onNewIntent(intent)
//    }

    override fun onNewIntent(intent: Intent?) {
        if (intent == null){
            Log.d("채팅화면일때 인텐트","is null")
        }

        Log.d("채팅화면일때", "1")
        var bundle = intent?.extras
        if(bundle != null){
            Log.d("채팅화면일때", "2")
            val senderId = bundle.getString("senderId").toString()
            val body = bundle.getString("body")
            var senderIdx = bundle.getString("senderIdx")?.toInt()
            var chatRoomIdx = bundle.getString("chatRoomIdx")
            var type = bundle.getString("type")
            //var sendTime = bundle.getString("sentAt")?.let { getFormattedDateTime(it) }
            var currentTime = toDate(System.currentTimeMillis())

            if (senderId != null && body != null && currentTime != null && type != null) {
                Log.d("채팅화면일때", "3")
                addChatItem(senderId, body, currentTime, "MESSAGE")
            }else{
                Log.d("채팅화면일때", "3 - null 존재")
            }
//            if (chatRoomIdx != null && senderIdx != null && body != null && sendTime != null) {
//                Log.d("채팅액티비티", "3")
//                updateMessage(chatRoomIdx, "MESSAGE", senderIdx, thisUserIdx, body, sendTime)
//            }else{
//                Log.d("채팅액티비티", "3 - null 존재")
//            }
        }else{
            Log.d("채팅화면일때", "2-error")
        }

        super.onNewIntent(intent)
    }

//    fun updateMessage(senderId: String, body: String, sentAt: String, type: String) {
//        Log.d("채팅액티비티", "updateMessage")
//        addChatItem(senderId, body, sentAt, type)
//        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
//    }

    fun getFCMIntent(){
        // chatRoomIdx만 받도록 수정 (여기서 데이터 받을필요 없이 chatRoomIdx로 이전 채팅 데이터 api 호출하게 하면 됨)
        Log.d("FCM인텐트", "1")
        var bundle = intent?.extras
        if(bundle != null){
            Log.d("FCM인텐트", "2")
            val senderId = bundle.getString("senderId").toString()
            val body = bundle.getString("body")
            var senderIdx = bundle.getString("senderIdx")?.toInt()
            var chatRoomIdx = bundle.getString("chatRoomIdx")
            //var sendTime = bundle.getString("sentAt")?.let { getFormattedDateTime(it) }
            var currentTime = toDate(System.currentTimeMillis())

            if (senderId != null && body != null && currentTime != null) {
                Log.d("FCM인텐트", "3")
                addChatItem(senderId, body, currentTime, "MESSAGE")
            }else{
                Log.d("FCM인텐트", "3 - null 존재")
            }
//            if (chatRoomIdx != null && senderIdx != null && body != null && sendTime != null) {
//                Log.d("채팅액티비티", "3")
//                updateMessage(chatRoomIdx, "MESSAGE", senderIdx, thisUserIdx, body, sendTime)
//            }else{
//                Log.d("채팅액티비티", "3 - null 존재")
//            }
        }else{
            Log.d("FCM인텐트", "받아올 번들 없음")
        }
    }

    @Throws(Exception::class)
    fun getFormattedDateTime(dateTime: String):String {
        // 대상 날짜로 LocalDateTime 만들기
        var parsedDateTimeArray = dateTime.split(".")
        var parsedDateTime = parsedDateTimeArray[0]

        val parsedLocalDateTime = LocalDateTime.parse(parsedDateTime)

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
        println(time)

        return time
    }
}