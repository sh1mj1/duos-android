package com.example.duos.ui.main.mypage.appointment

import com.example.duos.R
import com.example.duos.databinding.ActivityLastAppointmentBinding
import com.example.duos.ui.BaseActivity

class LastAppointmentActivity :
    BaseActivity<ActivityLastAppointmentBinding>(ActivityLastAppointmentBinding::inflate) {

    override fun initAfterBinding() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.previous_game_into_fragment_container_fc, LastAppointmentFragment())
            .commitAllowingStateLoss()

//        binding.signupNextBtn.visibility = View.GONE

    }


}