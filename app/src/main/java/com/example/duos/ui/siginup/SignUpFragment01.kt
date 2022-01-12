package com.example.duos.ui.siginup

import android.util.Log
import com.example.duos.databinding.FragmentSignup01Binding
import com.example.duos.ui.BaseFragment

class SignUpFragment01() : BaseFragment<FragmentSignup01Binding>(FragmentSignup01Binding::inflate) {

    override fun initAfterBinding() {
        binding.signup01PhoneNumberVerifyingBtn.setOnClickListener {
            Log.d("1", "클릭")
        }
    }

}