package com.example.duos.ui.signup

import android.widget.TextView
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup03Binding
import com.example.duos.ui.BaseFragment

class SignUpFragment03() : BaseFragment<FragmentSignup03Binding>(FragmentSignup03Binding::inflate) {
    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.login_process_tv).text = "03"

        binding.signup03LinearLayoutLl.setOnClickListener {
            activity?.supportFragmentManager?.let {fragmentManager ->
                SignUpLocalDialog().show(
                    fragmentManager,
                    "지역 선택"
                )
            }
        }
    }
}