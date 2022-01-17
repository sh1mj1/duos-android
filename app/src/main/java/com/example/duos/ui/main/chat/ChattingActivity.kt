package com.example.duos.ui.main.chat

import android.content.Intent
import android.view.View
import com.example.duos.databinding.ActivityChattingBinding
import com.example.duos.ui.BaseActivity
import com.prolificinteractive.materialcalendarview.CalendarDay

class ChattingActivity: BaseActivity<ActivityChattingBinding>(ActivityChattingBinding::inflate) {
    override fun initAfterBinding() {
        binding.chattingMakePlanBtn.setOnClickListener (View.OnClickListener {
            val intent = Intent(this, MakePlanActivity::class.java)
            startActivity(intent)
        })
    }
}