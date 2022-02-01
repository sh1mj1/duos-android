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
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.main.chat.ChattingActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import androidx.core.app.NotificationManagerCompat
import com.example.duos.data.entities.ChatListItem
import com.example.duos.data.remote.chat.chatList.ChatListService
import com.example.duos.ui.main.chat.ChatListView
import android.app.Application





class FirebaseMessagingServiceUtil : FirebaseMessagingService(){

    /**
     * Called when message is received.
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.*/
    // 메시지를 수신하는 메서드
    // [START receive_message]
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
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

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ

        Log.d(TAG, "메세지수신 From: ${remoteMessage.from}")

        // Check if message contains a data payload.
        if (remoteMessage.data.isNotEmpty()) {
            Log.d(TAG, "Message data payload: ${remoteMessage.data}")

            if (/* 장기 실행 작업으로 데이터를 처리해야 하는지 확인 */ true) {
                // 장기 실행 태스크(10초 이상)의 경우 WorkManager를 사용함.
                scheduleJob()
            } else {
                // 10초 이내에 메시지 처리
                handleNow()
            }
        }

        // 메시지에 notification payload가 포함되어있는지 확인
//        remoteMessage.notification?.let {
//            Log.d(TAG, "Message Notification Body: ${it.body}")
//            val title = it.title
//            val messageBody = it.body
//            val time = it.eventTime //null값을 받아옴..
//
//            Log.d("알림 확인", "1")
//            if (title != null && messageBody != null) {
//                Log.d("알림 확인", "2")
//                sendNotification("957cfc80-481c-4ae4-88a0-25a9599dd511", "MESSAGE", title, "tennis11", messageBody, System.currentTimeMillis())
//            }
//        }

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

        val body = remoteMessage.data.get("message")

        if(remoteMessage.data != null){
            Log.d("데이터메세지", remoteMessage.data.toString())
        }else{
            Log.d("데이터메세지", "is null")
        }

        Log.d(
            "Application.APPTAG",
            "myFirebaseMessagingService - onMessageReceived - message: $remoteMessage"
        )

//        val dialogIntent = Intent(this, ChattingActivity::class.java)
//        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        dialogIntent.putExtra("msg", remoteMessage)
//        startActivity(dialogIntent)
        // TODO
        // 또한 수신된 FCM 메시지의 결과로 사용자 자신의 알림을 생성하려면 여기서 시작해야 합니다. 아래 sendNotification 방법을 참조하십시오.

    }
    // [END receive_message]


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



    /**
     * Create and show a simple notification containing the received FCM message.
     * 수신된 FCM 메시지가 포함된 간단한 알림을 만들고 표시
     * @param messageBody FCM message body received.
     * FCM 메시지 본문이 수신되었습니다.
     */

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
            PendingIntent.FLAG_ONE_SHOT)

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

    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }
}

