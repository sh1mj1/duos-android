package com.example.duos.ui.signup

import android.content.Context
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.duos.databinding.FragmentSignup00Binding
import com.example.duos.ui.BaseFragment
import com.example.duos.utils.ViewModel

class SignUpFragment00 : BaseFragment<FragmentSignup00Binding>(FragmentSignup00Binding::inflate) {
    lateinit var signupNextBtnListener: SignUpNextBtnInterface
    lateinit var viewModel: ViewModel
    lateinit var mContext: SignUpActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignUpActivity) {
            mContext = context
        }
    }

    override fun initAfterBinding() {
        signupNextBtnListener = mContext
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)

        viewModel.agreementEssential01.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.agreementEssential02.observe(viewLifecycleOwner, Observer { it2 ->
                    if (it2){
                        signupNextBtnListener.onNextBtnEnable()
                    } else signupNextBtnListener.onNextBtnUnable()
                })
            }else signupNextBtnListener.onNextBtnUnable()
        })

        binding.signup00Checkbox01Cb.setOnClickListener { setCheckBox() }
        binding.signup00Checkbox02Cb.setOnClickListener { viewModel.agreementEssential01.value = (viewModel.agreementEssential01.value)?.not() }
        binding.signup00Checkbox03Cb.setOnClickListener { viewModel.agreementEssential02.value = (viewModel.agreementEssential02.value)?.not() }
    }

    fun setCheckBox(){
        // 체크
        if (binding.signup00Checkbox01Cb.isChecked){
            Log.d("ㅎㅇ","1")
            binding.signup00Checkbox02Cb.isChecked = true
            binding.signup00Checkbox03Cb.isChecked = true
            viewModel.agreementEssential01.value = true
            viewModel.agreementEssential02.value = true
        }
        // 체크해제
        else{
            Log.d("ㅎㅇ","2")
            binding.signup00Checkbox02Cb.isChecked = false
            binding.signup00Checkbox03Cb.isChecked = false
            viewModel.agreementEssential01.value = false
            viewModel.agreementEssential02.value = false

        }
    }
}