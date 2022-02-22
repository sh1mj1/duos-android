package com.example.duos.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.duos.data.local.UserDatabase
import com.example.duos.databinding.ActivitySplashBinding
import com.example.duos.ui.login.LoginActivity
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.signup.SignUpActivity
import com.example.duos.utils.getAccessToken
import com.example.duos.utils.getUserIdx
import com.example.duos.utils.saveJwt

class SplashActivity: AppCompatActivity() {

    lateinit var binding: ActivitySplashBinding
    var currentPage = 0
    lateinit var thread : Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        autoLogin()


    }

    val handler=Handler(Looper.getMainLooper()){
        setPage()
        true
    }

    //페이지 변경하기
    fun setPage(){
        if(currentPage == 4)
            currentPage = 0
        binding.splashViewpagerVp.setCurrentItem(currentPage, true)
        currentPage+=1
    }

    //2초 마다 페이지 넘기기
    inner class PagerRunnable:Runnable{
        override fun run() {
            while(true){
                try {
                    Thread.sleep(2000)
                    handler.sendEmptyMessage(0)
                } catch (e : InterruptedException){
                    Log.d("interupt", "interupt발생")
                }
            }
        }
    }


    private fun initViewpager() {
        val viewpagerAdapter = SplashViewpagerAdapter(this)
        viewpagerAdapter.addFragment(SplashViewpagerFragment01())
        viewpagerAdapter.addFragment(SplashViewpagerFragment02())
        viewpagerAdapter.addFragment(SplashViewpagerFragment03())

        binding.splashViewpagerVp.adapter = viewpagerAdapter
        binding.splashViewpagerVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        val child = binding.splashViewpagerVp.getChildAt(0)
        (child as? RecyclerView)?.overScrollMode = View.OVER_SCROLL_NEVER
        binding.splashCircleIndicatorCi.setViewPager(binding.splashViewpagerVp)
    }

    private fun initStatus(){
        binding.splashStartButtonBtn.setOnClickListener {
            thread.interrupt()
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.splashGotoLoginText02Tv.setOnClickListener {
            thread.interrupt()
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun autoLogin() {
        val userDB = UserDatabase.getInstance(this)
        if (getAccessToken() != null || userDB != null){
            Log.d("jwt", getAccessToken().toString())
            if(userDB?.userDao()?.getUser(getUserIdx()!!) != null){
                val intent = Intent(this, MainActivity::class.java)
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
            else initSet()

        } else initSet()
    }

    private fun initSet(){
        setContentView(binding.root)
        initViewpager()
        initStatus()
        thread = Thread(PagerRunnable())
        thread.start()
    }

}