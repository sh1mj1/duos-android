package com.example.duos.ui.signup

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.util.Log

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup01Binding
import com.example.duos.utils.SignUpInfoViewModel

class SignUpFragment01() :  Fragment() {

    lateinit var binding : FragmentSignup01Binding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignup01Binding.inflate(inflater, container, false)

        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "01"

        binding.signup01PhoneNumberEt.addTextChangedListener(PhoneNumberFormattingTextWatcher())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)
        binding.viewmodel = viewModel
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}