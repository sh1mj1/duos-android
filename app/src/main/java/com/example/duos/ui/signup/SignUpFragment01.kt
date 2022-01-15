package com.example.duos.ui.signup

import android.os.Build
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup01Binding
import com.example.duos.ui.BaseFragment

class SignUpFragment01() : BaseFragment<FragmentSignup01Binding>(FragmentSignup01Binding::inflate) {

    @RequiresApi(Build.VERSION_CODES.R)
    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.login_process_tv).text = "01"
    }



}