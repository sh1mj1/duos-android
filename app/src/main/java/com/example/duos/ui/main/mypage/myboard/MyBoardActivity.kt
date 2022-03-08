package com.example.duos.ui.main.mypage.myboard

import com.example.duos.R
import com.example.duos.databinding.ActivityMyBoardBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.mypage.notion.NotionFragment

class MyBoardActivity : BaseActivity<ActivityMyBoardBinding>(ActivityMyBoardBinding::inflate) {


    override fun initAfterBinding() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.myboard_into_fragment_container_fc, MyBoardFragment())
            .commitAllowingStateLoss()
    }

}

