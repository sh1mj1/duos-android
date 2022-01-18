package com.example.duos.ui.signup

import android.widget.TextView
import com.example.duos.R
import com.example.duos.data.remote.auth.AuthService
import com.example.duos.databinding.FragmentSignup03Binding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.signup.localSearch.LocalSearchDialogFragment

class SignUpFragment03() : BaseFragment<FragmentSignup03Binding>(FragmentSignup03Binding::inflate) {
    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "03"

        binding.signup03LinearLayoutLl.setOnClickListener {
            activity?.supportFragmentManager?.let {fragmentManager ->
                LocalSearchDialogFragment().show(
                    fragmentManager,
                    "지역 선택"
                )

            }
        }
    }

}