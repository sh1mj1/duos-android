package com.example.duos.ui.splash

import android.os.Handler
import android.os.Looper
import com.example.duos.data.remote.auth.AuthService
import com.example.duos.databinding.ActivitySplashBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.login.LoginActivity
import com.example.duos.ui.main.MainActivity

class SplashActivity: BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate), SplashView {

    override fun initAfterBinding() {
        Handler(Looper.getMainLooper()).postDelayed({
//            autoLogin()
            startActivityWithClear(MainActivity::class.java)
        }, 2000)
    }

    private fun autoLogin() {
//        AuthService.autoLogin(this)
    }

    override fun onAutoLoginLoading() {

    }

    override fun onAutoLoginSuccess() {
        startActivityWithClear(MainActivity::class.java)
    }

    override fun onAutoLoginFailure(code: Int, message: String) {
        startActivityWithClear(LoginActivity::class.java)
    }
}