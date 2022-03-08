package com.example.duos.ui.main

import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.duos.R
import com.example.duos.databinding.ActivityMainBinding
import com.example.duos.ui.BaseActivity


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navHostFragment: NavHostFragment

    override fun initAfterBinding() {
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController: NavController = navHostFragment.findNavController()

        binding.mainBottomNavigation.setupWithNavController(navController)
        binding.mainBottomNavigation.itemIconTintList = null

        val intent = getIntent()
        val fromWhere = intent.getStringExtra("FromDailyMatchingDetail")
        if (fromWhere != null && fromWhere.contentEquals("1")){
            val navGraph = navController.navInflater.inflate(R.navigation.navigation)
            navGraph.startDestination = R.id.menu_chatFragment
            navController.graph = navGraph
            binding.mainBottomNavigation.setupWithNavController(navController)
        }
    }

//    companion object {
//
//        private const val TAG = "MyFirebaseMsgService"
//    }
//
//    private fun getFCMToken(): String? {
//        var token: String? = null
//        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
//            if (!task.isSuccessful) {
//                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
//                return@OnCompleteListener
//            }
//
//            // Get new FCM registration token
//            val token = task.result
//
//            // Log and toast
////            val msg = getString(R.string.msg_token_fmt , token)
//            val msg = getString(R.string.msg_token_fmt)
//            Log.d(TAG, msg)
//
//            // 생성된 토큰 -> 닉네임 연결되게?
//            Log.d(TAG, token)
//            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
//
//        })
//        return token
//    }

}