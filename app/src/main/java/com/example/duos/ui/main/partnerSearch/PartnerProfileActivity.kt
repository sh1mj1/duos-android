package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import com.example.duos.R
import com.example.duos.databinding.ActivityPartnerProfileBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.chat.ChattingActivity

class PartnerProfileActivity: BaseActivity<ActivityPartnerProfileBinding>(ActivityPartnerProfileBinding::inflate) {
    override fun initAfterBinding() {
        supportFragmentManager.beginTransaction().replace(R.id.partner_profile_fragment_container_fc, PartnerProfileFragment())
            .commitAllowingStateLoss()

        binding.partnerProfileChattingBtn.setOnClickListener {

            var intent = Intent(this, ChattingActivity::class.java)
            startActivity(intent)
        }
    }
}