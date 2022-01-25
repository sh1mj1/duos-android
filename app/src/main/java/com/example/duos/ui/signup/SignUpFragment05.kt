package com.example.duos.ui.signup

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup05Binding
import com.example.duos.ui.BaseFragment
import com.example.duos.utils.SignUpInfoViewModel

class SignUpFragment05 () : BaseFragment<FragmentSignup05Binding>(FragmentSignup05Binding::inflate) {

    lateinit var viewModel: SignUpInfoViewModel

    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "05"
        viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)
        binding.viewmodel = viewModel
    }

    override fun onPause() {
        super.onPause()
        viewModel.introduce.value = binding.signup05IntroduceEt.text.toString()
    }
}