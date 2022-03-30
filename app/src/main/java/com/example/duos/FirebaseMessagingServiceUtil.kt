package com.example.duos

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
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
import com.example.duos.data.entities.ChatType
import com.example.duos.data.entities.chat.ChatMessageItem
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.entities.chat.MessageListData
import com.example.duos.utils.getCurrentChatRoomIdx
import com.example.duos.utils.getUserIdx
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter


class FirebaseMessagingServiceUtil : FirebaseMessagingService() {
    val mContext : Context = this
    lateinit var chatDB: ChatDatabase
    lateinit var chatRoomIdx:String
    var partnerIdx:Int = -1
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

            val chatRoomList : List<ChatRoom> = chatDB.chatRoomDao().getChatRoomList()
            Log.d("채팅방리스트", chatRoomList.toString())

            chatRoomIdx = messageData.get("chatRoomIdx").toString()
            partnerIdx = messageData.get("senderIdx")!!.toInt()

            //chatDB.chatMessageItemDao().deleteAll(chatRoomIdx)
            type = messageData.get("type").toString()
            if(type.equals("MESSAGE")){
                val body = messageData.get("body").toString()
                val sentAtString = messageData.get("sentAt").toString()
                Log.d("sentAtString - 1", sentAtString)
                val formattedSentAt = getFormattedDateTime(sentAtString)

                val parsedSentAtStringArray = sentAtString.split(".")
                var parsedSentAtString = parsedSentAtStringArray[0]
                val sentDateTime = LocalDateTime.parse(parsedSentAtString)
                Log.d("sentDateTime - 2", sentDateTime.toString())

                val chatIdx = messageData.get("dataIdx").toString()
                var parsedChatMessageIdx = chatIdx.split("@")
                var uuid = parsedChatMessageIdx[1]
                val senderId = messageData.get("title").toString()

                val chatMessageItem = ChatMessageItem(senderId, body, formattedSentAt, sentDateTime, ChatType.LEFT_MESSAGE, chatRoomIdx, uuid)

                setIntentByCurrentState(chatMessageItem)

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

    private fun sendMessageIntent(chatMessageItem: ChatMessageItem){
        val senderId = chatMessageItem.senderId
        val targetChatRoomIdx = chatMessageItem.chatRoomIdx
        val body = chatMessageItem.body
        val intent = Intent(mContext, ChattingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", type)
        intent.putExtra("chatRoomIdx", targetChatRoomIdx)
        intent.putExtra("senderId", senderId)
        intent.putExtra("byPushAlarmClick", true)
        intent.putExtra("partnerIdx", partnerIdx.toString())
        intent.putExtra("messageItem", chatMessageItem)

//        Log.d("fcmService - sendMessageIntent의 인텐트 - targetChatRoomIdx", targetChatRoomIdx)
//        Log.d("fcmService - sendMessageIntent의 인텐트 - senderId", senderId)
//        Log.d("fcmService - sendMessageIntent의 인텐트 - partnerIdx", partnerIdx.toString())

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
    
    private fun sendAppointmentIntent(body: String, senderId: String, targetChatRoomIdx: String){
        val intent = Intent(mContext, ChattingActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra("type", type)
        intent.putExtra("chatRoomIdx", targetChatRoomIdx)
        intent.putExtra("senderId", senderId)
        intent.putExtra("byPushAlarmClick", true)
        intent.putExtra("partnerIdx", partnerIdx.toString())

//        Log.d("fcmService - sendAppointmentIntent의 인텐트 - targetChatRoomIdx", targetChatRoomIdx)
//        Log.d("fcmService - sendAppointmentIntent의 인텐트 - senderId", senderId)
//        Log.d("fcmService - sendAppointmentIntent의 인텐트 - partnerIdx", partnerIdx.toString())

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

    fun setIntentByCurrentState(chatMessageItem: ChatMessageItem){  // 메세지
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val componentName: ComponentName?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            componentName = manager.appTasks[0].taskInfo.topActivity
        }else{
            componentName = manager.getRunningTasks(1)[0].topActivity
        }
        val ActivityName = componentName!!.shortClassName.substring(0)
        Log.d("현재액티비티이름", ActivityName)

        val body = chatMessageItem.body
        val senderId = chatMessageItem.senderId

        if(ActivityName.contains("ChattingActivity")){
            if(getCurrentChatRoomIdx().equals(chatRoomIdx)){  //
                Log.d("현재 채팅액티비티 & 현재 채팅방의 상대방에게 메세지가 옴","onNewIntent로 data payload로 온 data를 보냄, 푸시알림 X")

                val sentAt = chatMessageItem.sentAt.toString()
                val intent = Intent(this, ChattingActivity::class.java) // ChattingActivity의 onNewIntent로 감
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)      // Activity 밖에서 startActivity를 부를 때는 FLAG_ACTIVITY_NEW_TASK 로 세팅해주어야 한다. 안그러면 RuntimeException 발생.
                intent.putExtra("chatRoomIdx", chatRoomIdx)
                intent.putExtra("type", type)
                intent.putExtra("sentAt", sentAt)
                intent.putExtra("messageItem", chatMessageItem)
                startActivity(intent)
            } else{
                Log.d("현재 채팅액티비티이지만 현재 채팅방이 아닌 다른 채팅방의 상대방에게 메세지가 옴","푸시알림을 띄움")
                Log.d("현재 채팅액티비티이지만 현재 채팅방이 아닌 다른 채팅방의 상대방에게 메세지가 옴", chatRoomIdx)
                sendMessageIntent(chatMessageItem)
            }

        } else{
            Log.d("현재 채팅액티비티가 아닌 포그라운드", "푸시알림을 띄움")
            sendMessageIntent(chatMessageItem)
        }
    }

    fun setIntentByCurrentState(body: String, senderId: String, chatRoomIdx: String){   // 약속
        val manager = getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val componentName: ComponentName?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            componentName = manager.appTasks[0].taskInfo.topActivity
        }else{
            componentName = manager.getRunningTasks(1)[0].topActivity
        }
        val ActivityName = componentName!!.shortClassName.substring(0)
        Log.d("현재액티비티이름", ActivityName)

        if(ActivityName.contains("ChattingActivity")){
            if(getCurrentChatRoomIdx().equals(chatRoomIdx)){  //
                Log.d("현재 채팅액티비티 & 현재 채팅방의 상대방에게 약속 알림이 옴","onNewIntent로 data payload로 온 data를 보냄(버튼실시간 전환) & 푸시알림도 보냄")
                sendAppointmentIntent(body, senderId, chatRoomIdx)    //푸시알림 보내기

                val intent = Intent(this, ChattingActivity::class.java) // ChattingActivity의 onNewIntent로 감
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)      // Activity 밖에서 startActivity를 부를 때는 FLAG_ACTIVITY_NEW_TASK 로 세팅해주어야 한다. 안그러면 RuntimeException 발생.
                intent.putExtra("chatRoomIdx", chatRoomIdx)
                intent.putExtra("type", type)
                startActivity(intent)
            } else{
                Log.d("현재 채팅액티비티이지만 현재 채팅방이 아닌 다른 채팅방의 상대방에게","약속 알림이 옴 - 푸시알림을 띄움")
                Log.d("현재 채팅액티비티이지만 현재 채팅방이 아닌 다른 채팅방의 상대방에게", "약속 알림이 옴"+chatRoomIdx)
                sendAppointmentIntent(body, senderId, chatRoomIdx)
            }

        } else{
            Log.d("현재 채팅액티비티가 아닌 포그라운드", "푸시알림을 띄움")
            sendAppointmentIntent(body, senderId, chatRoomIdx)
        }
    }

    fun convertMessageListDataToChatMessageItem(messageListData: MessageListData): ChatMessageItem{

        val chatRoomIdx = messageListData.chatRoomIdx
        val senderIdx = messageListData.senderIdx

        val senderId = getSenderId(chatRoomIdx, senderIdx)
        val body = messageListData.msgContent
        val sentAt = messageListData.sentAt
        val formattedSentAt = getFormattedDateTime(sentAt.toString())
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
}
