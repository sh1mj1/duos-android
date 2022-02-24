package com.example.duos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.main.chat.ChattingActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationManagerCompat

import android.app.ActivityManager
import android.content.ComponentName
import android.widget.Toast
import com.example.duos.data.entities.ChatType
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.data.remote.chat.chat.MessageListData
import com.example.duos.data.remote.chat.chat.SyncChatMessageData
import com.example.duos.data.remote.chat.chatList.ChatListService
import com.example.duos.ui.main.chat.ChatListView
import com.example.duos.ui.main.chat.ChatMessageView
import com.example.duos.utils.getCurrentChatRoomIdx
import com.example.duos.utils.getUserIdx
import okhttp3.internal.format
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat


class FirebaseMessagingServiceUtil : FirebaseMessagingService(), ChatListView, ChatMessageView{
    val mContext : Context = this
    lateinit var chatDB: ChatDatabase
    lateinit var chatRoomIdx:String
    lateinit var partnerIdx:String
    lateinit var type:String
    lateinit var messageData: Map<String, String>
    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.*/
    // 메시지를 수신하는 메서드
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        chatDB = ChatDatabase.getInstance(applicationContext, ChatDatabase.provideGson())!!
        // [START_EXCLUDE]

        // onMessageReceived는 백그라운드 상태일 때 데이터 페이로드가 포함되어있지 않고 알림 페이로드만 포함하고 있다면(알림 메세지라면) 호출되지 않음.
        // 백그라운드에서 수신된 알림 페이로드, 데이터 페이로드 둘 다 포함한 메세지의 경우도 호출되지 않음.
        // 이 경우 알림은 기기의 작업 표시줄로 전송되고 데이터 페이로드는 런처 활동의 인텐트 부가 정보로 전송된다고 써있는데 무슨말인지 완벽하게는 모르겠다..

        /*메시지 데이터 메시지와 알림 메시지에는 두 가지 유형
         데이터 메시지는 onMessageReceived에서 처리됨 앱이 포그라운드인지 백그라운드인지 여부를 나타냄.
         데이터 메시지는 전통적으로 GCM과 함께 사용되는 유형. 알림 메시지는 앱이 포그라운드에 있을 때 onMessageReceived에서만 수신됨.
         앱이 백그라운드에 있으면 자동으로 생성된 알림이 표시됩니다. 사용자가 알림을 누르면 앱으로 돌아갑니다.
         알림 및 데이터 페이로드가 모두 포함된 메시지는 알림 메시지로 간주됩니다. Firebase 콘솔은 항상 알림 메시지를 보냅니다.
         */
        // [END_EXCLUDE]

        if (remoteMessage.notification != null) {
//            ChatListService.chatList(this, 0) // get 가능 확인함

            val body = remoteMessage.notification!!.body
            Log.d(TAG, "Notification Body: $body")
            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(
                applicationContext
            )
                .setSmallIcon(R.mipmap.ic_duos_round) // 알림 영역에 노출 될 아이콘.
                .setContentTitle(getString(R.string.app_name)) // 알림 영역에 노출 될 타이틀
                .setContentText(body) // Firebase Console 에서 사용자가 전달한 메시지내용
            val notificationManagerCompat = NotificationManagerCompat.from(
                applicationContext
            )
            remoteMessage.notification!!.eventTime
            notificationManagerCompat.notify(0x1001, notificationBuilder.build())
        }

        if(remoteMessage.data != null){
            Log.d("데이터메세지", remoteMessage.data.toString())
            messageData = remoteMessage.data

            if(chatDB.chatRoomDao().getChatRoomList().isEmpty()){
                ChatListService.chatList(this, getUserIdx()!!)
            }

            val chatRoom : List<ChatRoom> = chatDB.chatRoomDao().getChatRoomList()
            Log.d("채팅방리스트", chatRoom.toString())

            chatRoomIdx = messageData.get("chatRoomIdx").toString()
            partnerIdx = messageData.get("senderIdx").toString()
            //chatDB.chatMessageItemDao().deleteAll(chatRoomIdx)
            type = messageData.get("type").toString()
            if(type.equals("MESSAGE")){
                val chatMessageIdx: String
                val lastChatMessageIdx: String
                val lastMessageData = chatDB.chatMessageItemDao().getLastMessageData(chatRoomIdx)
                if(lastMessageData != null){
                    lastChatMessageIdx = lastMessageData.chatRoomIdx + "@" + lastMessageData.chatMessageIdx
                    Log.d("마지막 채팅메세지인덱스 존재", "request body에 담아 api 호출"+lastMessageData)
                    Log.d("onMessageReceived 채팅 동기화api 호출 전 - 룸디비 확인", chatDB.chatMessageItemDao().getChatMessages(chatRoomIdx).toString())
                    ChatService.syncChatMessage(this, lastChatMessageIdx, chatRoomIdx)
                } else{  //지금 받은 메세지가 채팅방의 처음 메세지일 때
                    // 메세지를 룸디비에 저장하기 전 lastAddedChatMessageId 업데이트
                    lastChatMessageIdx = messageData.get("dataIdx").toString()
                    Log.d("chatMessageIdx is null or blank", "채팅메세지를 주고받은 적 없음 - " + lastChatMessageIdx)

                    val body = messageData.get("body").toString()
                    val partnerIdx = messageData.get("senderIdx").toString()
                    val sentAtString = messageData.get("sentAt").toString()
                    val formattedSentAt = getFormattedDateTime(sentAtString)

                    val parsedSentAtStringArray = sentAtString.split(".")
                    var parsedSentAtString = parsedSentAtStringArray[0]
                    val sentEpochMilli = LocalDateTime.parse(parsedSentAtString).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

                    val chatIdx = messageData.get("dataIdx").toString()
                    var parsedChatMessageIdx = chatIdx.split("@")
                    var uuid = parsedChatMessageIdx[1]
                    val senderId = chatDB.chatRoomDao().getPartnerId(chatRoomIdx)       // data payload로 title은 받지 않아도 될 듯.. 나중에 말씀드리자

                    // 날짜변경선 무조건 추가
                    Log.d("날짜변경선 무조건 추가 - 시간포매팅 1",sentAtString)
                    var parsedDateTimeArray = sentAtString.split(".")
                    var parsedDateTime = parsedDateTimeArray[0]
                    Log.d("날짜변경선 무조건 추가 - 시간포매팅 2", parsedDateTime)

                    val parsedLocalDateTime = LocalDateTime.parse(parsedDateTime)
                    Log.d("날짜변경선 무조건 추가 - 시간포매팅 3", parsedLocalDateTime.toString())

                    val date = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
                    val dateItem = ChatMessageItem("date", date, "date", sentEpochMilli, ChatType.CENTER_MESSAGE, chatRoomIdx, "date"+ uuid)
                    chatDB.chatMessageItemDao().insert(dateItem)

                     //여기서 받은 메세지를 룸디비에 저장
                    val chatMessageItem = ChatMessageItem(senderId, body, formattedSentAt, sentEpochMilli, ChatType.LEFT_MESSAGE, chatRoomIdx, uuid)
                    chatDB.chatMessageItemDao().insert(chatMessageItem)
                    setIntentByCurrentState(messageData.get("body").toString(), messageData.get("title").toString(), messageData.get("chatRoomIdx").toString())
                }
//                ChatService.syncChatMessage(this, lastChatMessageIdx, chatRoomIdx)
            } else{ // 약속 관련 fcm 일 때
                if(type.equals("CREATE_APPOINTMENT")){
                    Log.d("fcm data payload - type","약속 생성")
                    // DB에 isAppointmentExist와 appointmentIdx update
                    chatDB.chatRoomDao().updateAppointmentExist(chatRoomIdx, true)
                    val appointmentIdx = messageData.get("dataIdx")?.toInt()
                    chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx, appointmentIdx)
                } else if(type.equals("DELETE_APPOINTMENT")){
                    Log.d("fcm data payload - type","약속 삭제")
                    // DB에 isAppointmentExist와 appointmentIdx update
                    chatDB.chatRoomDao().updateAppointmentExist(chatRoomIdx, false)
                    chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx, null)
                } else if(type.equals("UPDATE_APPOINTMENT")){
                    Log.d("fcm data payload - type","약속 수정")
                    // DB에 isAppointmentExist와 appointmentIdx update
                    val appointmentIdx = messageData.get("dataIdx")?.toInt()
                    chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx, appointmentIdx)
                } else{
                    Log.d("fcm data payload - type", "type을 제대로 받아오지 못함")
                }
                setIntentByCurrentState(messageData.get("body").toString(), messageData.get("title").toString(), messageData.get("chatRoomIdx").toString())
            }

        }else{
            Log.d("데이터메세지", "is null")
        }

        Log.d(
            "Application.APPTAG",
            "myFirebaseMessagingService - onMessageReceived - message: $remoteMessage"
        )
        // 또한 수신된 FCM 메시지의 결과로 사용자 자신의 알림을 생성하려면 여기서 시작해야 합니다. 아래 sendNotification 방법을 참조하십시오.

    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     * 수신된 FCM 메시지가 포함된 간단한 알림을 만들고 표시
     * @param messageBody FCM message body received.
     * FCM 메시지 본문이 수신되었습니다.
     */

    private fun sendMessageData(body: String, senderId: String, targetChatRoomIdx: String){
        val intent = Intent(mContext, ChattingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", type)
        intent.putExtra("chatRoomIdx", targetChatRoomIdx)
        intent.putExtra("senderId", senderId)
        intent.putExtra("byPushAlarmClick", true)
        intent.putExtra("partnerIdx", partnerIdx)


//        val bundle = Bundle()
//        bundle.putString("type", type)
//        bundle.putString("chatRoomIdx", chatRoomIdx)
//        bundle.putString("senderId", senderId)
//        bundle.putString("partnerIdx", partnerIdx)
//        bundle.putBoolean("isAlarmed", true)

        Log.d("fcmService - sendMessageData의 인텐트 - targetChatRoomIdx", targetChatRoomIdx)
        Log.d("fcmService - sendMessageData의 인텐트 - senderId", senderId)
        Log.d("fcmService - sendMessageData의 인텐트 - partnerIdx", partnerIdx)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.firebase_notification_channel_id_testS)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.splash_duos_logo)
            .setContentTitle(senderId)
            .setContentText(body)
            .setAutoCancel(true)
//            .setExtras(bundle)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 오레오 알림 채널이 필요.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    override fun onSyncChatMessageSuccess(syncChatMessageData: SyncChatMessageData) {

        Log.d("onSyncChatMessageSuccess 시작 - 룸디비 확인", chatDB.chatMessageItemDao().getChatMessages(chatRoomIdx).toString())
        val listSize = syncChatMessageData.listSize
        if(listSize != 0){
            for(i: Int in 0.. listSize-1){
                Log.d("for문","으아")
                val messageItem = convertMessageListDataToChatMessageItem(syncChatMessageData.messageList[i])
                val sentAt = messageItem.sentAt
                val sentAtLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(messageItem.sentAt), ZoneId.systemDefault())
                val chatRoomIdx = messageItem.chatRoomIdx
                val lastSentAt = chatDB.chatMessageItemDao().getLastMessageData(chatRoomIdx).sentAt
                val lastSentAtLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(chatDB.chatMessageItemDao().getLastMessageData(chatRoomIdx).sentAt), ZoneId.systemDefault())

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if(!sentAtLocalDate.toLocalDate().isEqual(lastSentAtLocalDate.toLocalDate())){    // 같은 날짜가 아닐 때 (날짜 변경선 roomDB에 insert)
                        //val dateString = sentAt.dayOfYear.toString() + "년 " + (sentAt.dayOfMonth+1).toString()
                        val dateString = sentAtLocalDate.toString()
                        Log.d("날짜 변경선 포매팅 1",dateString)
                        var parsedDateTimeArray = dateString.split(".")
                        var parsedDateTime = parsedDateTimeArray[0]
                        Log.d("날짜 변경선 포매팅 2", parsedDateTime)

                        val parsedLocalDateTime = LocalDateTime.parse(parsedDateTime)
                        Log.d("날짜 변경선 포매팅 3", parsedLocalDateTime.toString())

                        val date = parsedLocalDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"))
                        val chatMessageIdx = messageItem.chatMessageIdx
                        val dateItem = ChatMessageItem("date", date, "date", sentAt, ChatType.CENTER_MESSAGE, chatRoomIdx, "date"+chatMessageIdx)
                        chatDB.chatMessageItemDao().insert(dateItem)
                    }
                } else {
                    Log.d("isSameDate 확인을 위한 format", "실패")
                }
                Log.d("messageItem 확인", messageItem.toString())
                chatDB.chatMessageItemDao().insert(messageItem) // 채팅 메세지 RoomDB에 insert
            }
            Log.d("채팅 동기화 완료", chatDB.chatMessageItemDao().getChatMessages(syncChatMessageData.messageList[0].chatRoomIdx).toString())
        }

        setIntentByCurrentState(messageData.get("body").toString(), messageData.get("title").toString(), messageData.get("chatRoomIdx").toString())
    }

    override fun onSyncChatMessageFailure(code: Int, message: String) {
        Toast.makeText(this,"code: $code, message: $message", Toast.LENGTH_LONG)
    }

    fun setIntentByCurrentState(body: String, senderId: String, chatRoomIdx: String){
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val componentName: ComponentName?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            componentName = manager.appTasks[0].taskInfo.topActivity
        }else{
            componentName = manager.getRunningTasks(1)[0].topActivity
        }
        val ActivityName = componentName!!.shortClassName.substring(0)
        Log.d("현재액티비티이름", ActivityName)


//        val body = messageData.get("body").toString()
//        val partnerIdx = messageData.get("senderIdx").toString()
//        val sentAtString = messageData.get("sentAt").toString()
//        val senderId = chatDB.chatRoomDao().getPartnerId(chatRoomIdx)       // data payload로 title은 받지 않아도 될 듯.. 나중에 말씀드리자

        // 여기서 받은 메세지를 룸디비에 저장
//            val formattedSentAt = getFormattedDateTime(sentAtString)
//            val parsedSentAtStringArray = sentAtString.split(".")
//            var parsedSentAtString = parsedSentAtStringArray[0]
//            val sentAt = LocalDateTime.parse(parsedSentAtString)
//
//            val chatMessageIdx = messageData.get("dataIdx").toString()
//            var parsedChatMessageIdx = chatMessageIdx.split("@")
//            var uuid = parsedChatMessageIdx[1]
//            val chatMessageItem = ChatMessageItem(senderId, body, formattedSentAt, sentAt, ChatType.LEFT_MESSAGE, chatRoomIdx, uuid)
//            chatDB.chatMessageItemDao().insert(chatMessageItem)


        if(ActivityName.contains("ChattingActivity")){
            if(getCurrentChatRoomIdx().equals(chatRoomIdx)){  //
                Log.d("현재 채팅액티비티 & 현재 채팅방의 상대방에게 메세지가 옴","onNewIntent로 data payload로 온 data를 보냄, 푸시알림 X")

                val intent = Intent(this, ChattingActivity::class.java) // ChattingActivity의 onNewIntent로 감
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)      // Activity 밖에서 startActivity를 부를 때는 FLAG_ACTIVITY_NEW_TASK 로 세팅해주어야 한다. 안그러면 RuntimeException 발생.
                intent.putExtra("chatRoomIdx", chatRoomIdx)
                intent.putExtra("type", type)
                startActivity(intent)

                //intent.putExtra("body", body)
                //intent.putExtra("partnerIdx", partnerIdx)
                //intent.putExtra("sentAt", sentAt)
                //intent.putExtra("senderId",  senderId)
                //Log.d("발신자", senderId)


            } else{
                Log.d("현재 채팅액티비티이지만 현재 채팅방이 아닌 다른 채팅방의 상대방에게 메세지가 옴","푸시알림을 띄움")
                Log.d("현재 채팅액티비티이지만 현재 채팅방이 아닌 다른 채팅방의 상대방에게 메세지가 옴", chatRoomIdx)
                sendMessageData(body, senderId, chatRoomIdx)
            }

        } else{
            Log.d("현재 채팅액티비티가 아닌 포그라운드", "푸시알림을 띄움")
            sendMessageData(body, senderId, chatRoomIdx)
        }
    }

    fun convertMessageListDataToChatMessageItem(messageListData: MessageListData): ChatMessageItem{

        val chatRoomIdx = messageListData.chatRoomIdx
        val senderIdx = messageListData.senderIdx

        val senderId = getSenderId(chatRoomIdx, senderIdx)
        val body = messageListData.message
        val sentAt = messageListData.sentAt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val sentAtLocalDateTime  = messageListData.sentAt
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






    // 지훈님부분 or 안쓴부분


    // [START on_new_token]
    /* FCM 등록 토큰이 업데이트되면 호출됩니다. 이전 토큰의 보안이 손상된 경우 이 문제가 발생할 수 있습니다.
     * FCM 등록 토큰이 처음 생성될 때 호출되므로 토큰을 검색할 수 있습니다. */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        //이 응용 프로그램 인스턴스로 메시지를 보내거나 서버 측에서 이 응용 프로그램 "구독" -> 알림을 관리하려면 앱 서버로 FCM 등록 토큰을 보내십시오.

        // 토큰 정보는 사용자 정보와 함께 서버에 보내야 하므로
        // 토큰이 업데이트됐을 때 바로가 아니라
        // onNewToken에서는 sharedPreference에 토큰값이 있는지 확인하고,
        // 있으면 토큰이 바꼈다는 말이므로 토큰을 sharedPreference에 저장해뒀다가 로그인액티비티에서 회원정보 보낼 때 서버에 전달.. 회원정보 보내는 api와는 별개여야 좋을듯?
        // 없으면

        //sendRegistrationToServer(token)
    }
    // [END on_new_token]

    // WorkManager를 사용하여 비동기 작업을 예약
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java).build()
        WorkManager.getInstance(this).beginWith(work).enqueue()
        // [END dispatch_job]
    }

    /* Handle time allotted to BroadcastReceivers. 브로드캣트 수신기에 할당된 시간을 처리 */
    private fun handleNow() {
        Log.d(TAG, "Short lived task is done.")
    }

    /**
     * Persist token to third-party servers.
     * Modify this method to associate the user's FCM registration token with any server-side account
     * 사용자의 FCM 등록 토큰을 서버측 계정과 연결하도록 이 방법 수정
     * maintained by your application. 응용 프로그램에 의해 유지 관리됩니다.
     * @param token The new token.
     */
    private fun sendRegistrationToServer(token: String?) {

        // TODO: Implement this method to send token to your app server.
        Log.d(TAG, "sendRegistrationTokenToServer($token)")
    }


    fun isForeground(context: Context): Boolean {

        // Get the Activity Manager
        val manager = context.getSystemService(ACTIVITY_SERVICE) as ActivityManager

        // Get a list of running tasks, we are only interested in the last one,
        // the top most so we give a 1 as parameter so we only get the topmost.
        val task = manager.runningAppProcesses

        // Get the info we need for comparison.
        val componentInfo = task[0].importanceReasonComponent

        // Check if it matches our package name. // If not then our app is not on the foreground.
        return if (componentInfo.packageName == context.packageName) true else false
    }

    private fun sendMessageData(chatRoomIdx: String, type: String, body: String, senderIdx: String, sentAt: String, senderId: String){
        Log.d("노티", body)
        //

        val intent = Intent(this, ChattingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.firebase_notification_channel_id_testS)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.splash_duos_logo)
            .setContentTitle(senderId)
            .setContentText(body)
//            .setExtras(bundle)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 오레오 알림 채널이 필요.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    // 채팅 메세지를 디바이스에 알려주는 함수
    private fun sendNotification(chatRoomIdx: String, type: String, from: String, to:String, messageBody: String, sendTime: Long) {  // 보낸사람, 받는사람의 인덱스와 메세지 본문, 메세지 type, chatRoomIdx??
        Log.d("노티", messageBody)
        //

        val intent = Intent(this, ChattingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
//                or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED or Intent.FLAG_ACTIVITY_SINGLE_TOP)

//        val bundle = Bundle()
//        bundle.putString("type", type)
//        bundle.putString("from", from)
//        bundle.putString("to", to)
//        bundle.putString("messageBody", messageBody)
//        bundle.putLong("sendTime", sendTime)

        intent.putExtra("chatRoomIdx", chatRoomIdx)
        intent.putExtra("type", type)
        intent.putExtra("from", from)
        intent.putExtra("to", to)
        intent.putExtra("messageBody", messageBody)
        intent.putExtra("sendTime", sendTime)

        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = getString(R.string.firebase_notification_channel_id_testS)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.splash_duos_logo)
            .setContentTitle(from)
            .setContentText(messageBody)
//            .setExtras(bundle)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 오레오 알림 채널이 필요.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

    private fun sendNotification(messageBody: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(
            this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT
        )

        val channelId = getString(R.string.firebase_notification_channel_id_testS)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.splash_duos_logo)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // 안드로이드 오레오 알림 채널이 필요.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
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
}

