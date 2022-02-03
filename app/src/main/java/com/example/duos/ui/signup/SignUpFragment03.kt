package com.example.duos.ui.signup

import android.content.Context
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup03Binding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.signup.localSearch.LocationDialogFragment
import com.example.duos.utils.SignUpInfoViewModel

class SignUpFragment03() : BaseFragment<FragmentSignup03Binding>(FragmentSignup03Binding::inflate) {
    lateinit var signupNextBtnListener: SignUpNextBtnInterface
    lateinit var mContext: SignUpActivity
    lateinit var viewModel: SignUpInfoViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignUpActivity) {
            mContext = context
        }
    }

    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "03"
        signupNextBtnListener = mContext
        val viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)

        binding.signup03LinearLayoutLl.setOnClickListener {
            val dialog = LocationDialogFragment()
            activity?.supportFragmentManager?.let {fragmentManager ->
                dialog.show(
                    fragmentManager,
                    "지역 선택"
                )
            }
        }


        viewModel.locationDialogShowing.observe(this, Observer {
            if (it){
                binding.signup03LocationTextTv.text = viewModel.locationCateName.value + " " +
                        viewModel.locationName.value
            }
        })

        viewModel.locationName.observe(this, Observer {
            if (it.isNotEmpty()){
                signupNextBtnListener.onNextBtnEnable()
                viewModel.signUp03Avail.value = true
            } else {signupNextBtnListener.onNextBtnUnable()
                viewModel.signUp03Avail.value = false}
            })

    }

}