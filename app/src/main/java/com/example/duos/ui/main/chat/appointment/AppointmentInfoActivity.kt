package com.example.duos.ui.main.chat.appointment

import android.content.Intent
import com.example.duos.databinding.ActivityAppointmentInfoBinding
import com.example.duos.ui.BaseActivity

class AppointmentInfoActivity: BaseActivity<ActivityAppointmentInfoBinding>(ActivityAppointmentInfoBinding::inflate) {
    override fun initAfterBinding() {

        binding.planInfoReviseTv.setOnClickListener {
            val intent = Intent(this, AppointmentEditActivity::class.java)
            startActivity(intent)
        }
    }

}