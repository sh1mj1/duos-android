//package com.example.duos
//
//import android.util.Log
//
//
//class FirebaseInstanceIdService : FirebaseInstanceIdService() {
//
//    private val TAG = "FirebaseInstanceIdService"
//
//
//    /**
//     * Called if the FCM registration token is updated. This may occur if the security of
//     * the previous token had been compromised. Note that this is called when the
//     * FCM registration token is initially generated so this is where you would retrieve the token.
//     */
//    override fun onNewToken(token: String) {
//        Log.d(TAG, "Refreshed token: $token")
//
//        // If you want to send messages to this application instance or
//        // manage this apps subscriptions on the server side, send the
//        // FCM registration token to your app server.
//        sendRegistrationToServer(token)
//    }
//
//
////    override fun onTokenRefresh() {
////        // Get updated InstanceID token.
////        val refreshedToken = FirebaseInstanceId.getInstance().token
////        Log.d(TAG, "Refreshed token: " + refreshedToken!!)
////        //여기서는 토큰을 확인하고 획득한 토큰으로 여타 작업 예를 들어 웹서버에 토큰을 저장하는등 개별 동작 가능
////        sendRegistrationToServer(refreshedToken)
////    }
////
////    private fun sendRegistrationToServer(token: String) {
////        //TODO : Implement this method to send token to your app server.
////        // 토큰을 서버로 보내 DB에 저장하는 동작을 추가하도록 하자
////        // 매번 실행되는 게 아니고, 최초 앱이 설치될 때 한번 수행되는 듯..
////        // 토큰은 한 기기에 하나만
////        // OKHttp 와 PHP, MYSQL
////
////
////    }
//
//
//}