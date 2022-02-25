package com.example.duos.ui.main

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.duos.R
import com.example.duos.databinding.ActivityMainBinding
import com.example.duos.ui.BaseActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging


class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {
    private lateinit var navHostFragment: NavHostFragment


    override fun initAfterBinding() {
        overridePendingTransition(R.anim.horizon_enter, R.anim.none)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment
        val navController: NavController = navHostFragment.findNavController()

        binding.mainBottomNavigation.setupWithNavController(navController)
        binding.mainBottomNavigation.itemIconTintList = null

        geFCMToken()

    }


    companion object {

        private const val TAG = "MyFirebaseMsgService"
    }

    private fun geFCMToken(): String? {
        var token: String? = null
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result

            // Log and toast
//            val msg = getString(R.string.msg_token_fmt , token)
            val msg = getString(R.string.msg_token_fmt)
            Log.d(TAG, msg)

            // 생성된 토큰 -> 닉네임 연결되게?
            Log.d(TAG, token)
            //Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()

        })
        return token
    }

}