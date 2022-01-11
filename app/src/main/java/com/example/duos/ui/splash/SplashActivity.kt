package com.example.duos.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.duos.data.remote.auth.AuthService
import com.example.duos.databinding.ActivitySplashBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.login.LoginActivity
import com.example.duos.ui.main.MainActivity

class SplashActivity: AppCompatActivity(), SplashView {

    lateinit var binding: ActivitySplashBinding
    var handler: Handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        handler.postDelayed(Runnable{
            setContentView(binding.root)
            initViewpager()
        }, 1000)
    }

    private fun initViewpager() {
        val viewpagerAdapter = SplashViewpagerAdapter(this)
        viewpagerAdapter.addFragment(SplashViewpagerFragament01())
        viewpagerAdapter.addFragment(SplashViewpagerFragment02())
        viewpagerAdapter.addFragment(SplashViewpagerFragment03())

        binding.splashViewpagerVp.adapter = viewpagerAdapter
        binding.splashViewpagerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val child = binding.splashViewpagerVp.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
        binding.splashCircleIndicatorCi.setViewPager(binding.splashViewpagerVp)
    }

    private fun autoLogin() {
//        AuthService.autoLogin(this)
    }

    override fun onAutoLoginLoading() {

    }

    override fun onAutoLoginSuccess() {
//        startActivityWithClear(MainActivity::class.java)
    }

    override fun onAutoLoginFailure(code: Int, message: String) {
//        startActivityWithClear(LoginActivity::class.java)
    }
}