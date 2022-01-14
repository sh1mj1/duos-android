package com.example.duos.ui.signup

import android.widget.TextView
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup05Binding
import com.example.duos.ui.BaseFragment

class SignUpFragment05 () : BaseFragment<FragmentSignup05Binding>(FragmentSignup05Binding::inflate) {

    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.login_process_tv).text = "05"

    }
}