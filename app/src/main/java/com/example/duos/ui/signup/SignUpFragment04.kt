package com.example.duos.ui.siginup

import android.R
import android.widget.RadioButton
import com.example.duos.databinding.FragmentSignup04Binding
import com.example.duos.ui.BaseFragment

class SignUpFragment04() : BaseFragment<FragmentSignup04Binding>(FragmentSignup04Binding::inflate) {
    override fun initAfterBinding() {

        for (i in 1 .. 14){

            var btnId : Int = resources.getIdentifier("signup_04_radio_" + i.toString() + "_btn", "string",requireActivity().packageName)
            var radioButton : RadioButton = requireView().findViewById(btnId)
            val num : String = i.toString()
            radioButton.setText(resources.getString(resources.getIdentifier("signup_length_of_play_$num", "string", requireActivity().packageName)))
        }
    }
}