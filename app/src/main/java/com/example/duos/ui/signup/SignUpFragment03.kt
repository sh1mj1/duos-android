package com.example.duos.ui.signup

import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup03Binding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.signup.localSearch.LocationDialogFragment
import com.example.duos.utils.SignUpInfoViewModel
import com.google.android.material.tabs.TabLayoutMediator

class SignUpFragment03() : BaseFragment<FragmentSignup03Binding>(FragmentSignup03Binding::inflate) {
    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "03"

        binding.signup03LinearLayoutLl.setOnClickListener {
            val dialog = LocationDialogFragment()
            activity?.supportFragmentManager?.let {fragmentManager ->
                dialog.show(
                    fragmentManager,
                    "지역 선택"
                )
            }
        }

        val viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)
        viewModel.locationDialogShowing.observe(this, Observer {
            Log.d("ㅎㅇ","observe")
            if (it){
                binding.signup03LocationTextTv.text = viewModel.locationCateName.value + " " +
                        viewModel.locationName.value
            }
        })

    }

}