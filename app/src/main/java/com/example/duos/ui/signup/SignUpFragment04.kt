package com.example.duos.ui.signup

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup04Binding
import com.example.duos.ui.BaseFragment
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.duos.utils.SignUpInfoViewModel


class SignUpFragment04() : BaseFragment<FragmentSignup04Binding>(FragmentSignup04Binding::inflate) {

    lateinit var viewModel: SignUpInfoViewModel

    override fun initAfterBinding() {

        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "04"

        for (i in 1..14) {
            var btnId: Int = resources.getIdentifier(
                "signup_04_table_" + i.toString() + "_btn",
                "id",
                requireActivity().packageName
            )
            var btn: Button = requireView().findViewById(btnId)
            val num: String = i.toString()
            btn.text = resources.getString(
                resources.getIdentifier(
                    "signup_length_of_play_$num",
                    "string",
                    requireActivity().packageName
                )
            )
        }


        binding.viewmodel = viewModel
    }

    fun setRadioButton(radioButton: String) {
        viewModel.experience.value = radioButton
    }
}