package com.example.duos.ui.main.mypage.appointment

import com.example.duos.R
import com.example.duos.databinding.ActivityAppointmentBinding
import com.example.duos.ui.BaseActivity

class AppointmentActivity :
    BaseActivity<ActivityAppointmentBinding>(ActivityAppointmentBinding::inflate) {

    override fun initAfterBinding() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.previous_game_into_fragment_container_fc, AppointmentFragment())
            .commitAllowingStateLoss()

//        binding.signupNextBtn.visibility = View.GONE

    }


}