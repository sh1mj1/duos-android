package com.example.duos.ui.signup


import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Build
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.NumberPicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.data.remote.signUp.SignUpService
import com.example.duos.databinding.FragmentSignup02Binding
import com.example.duos.utils.SignUpInfoViewModel

import java.util.regex.Pattern


class SignUpFragment02() : Fragment(), SignUpNickNameView {

    lateinit var binding: FragmentSignup02Binding
    lateinit var birthNextBtnListener: SignUpBirthNextBtnInterface
    lateinit var mContext: SignUpActivity
    var savedState: Bundle? = null
    var birthTextView: TextView? = null
    lateinit var npYear: NumberPicker
    lateinit var npMonth: NumberPicker
    lateinit var npDay: NumberPicker
    lateinit var viewModel : SignUpInfoViewModel
    var isDuplicated : Boolean = true

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is SignUpActivity) {
            mContext = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSignup02Binding.inflate(inflater, container, false)
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "02"
        birthNextBtnListener = mContext
        viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)

        binding.signup02BirthEt.setOnClickListener {
            birthNextBtnListener.onNextBtnChanged(true)
            BirthNumberPicker()
        }

        birthTextView = binding.signup02BirthEt
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("savedState")
        }
        if (savedState != null) {
            birthTextView!!.setText(savedState!!.getCharSequence("birth"));
        }
        savedState = null

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

       this.viewModel.nickName.observe(requireActivity(), {
           val pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$"
           if (it.isNotEmpty()){
               binding.signup02NickNameLayout.isEndIconVisible = true
               if (!Pattern.matches(pattern, it.toString()) or (it.length<2)) {
                   binding.signup02NickNameErrorTv.visibility = View.VISIBLE
                   binding.signup02NickNameCheckIconIv.visibility = View.VISIBLE
                   binding.signup02NickNameCheckIconIv.setImageResource(R.drawable.ic_signup_nickname_unable)
                   binding.signup02NickNameDuplicateBtn.isEnabled = false
                   binding.signup02NickNameDuplicateBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
                   binding.signup02NickNameDuplicateBtn.setTextColor(
                       ContextCompat.getColor(
                           requireContext(),
                           R.color.dark_gray_A
                       )
                   )
               } else{
                   binding.signup02NickNameErrorTv.visibility = View.INVISIBLE
                   binding.signup02NickNameCheckIconIv.visibility = View.VISIBLE
                   binding.signup02NickNameCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check_done)
                   binding.signup02NickNameDuplicateBtn.isEnabled = true
                   binding.signup02NickNameDuplicateBtn.setBackgroundResource(R.drawable.signup_phone_verifying_done_rectangular)
                   binding.signup02NickNameDuplicateBtn.setTextColor(
                       ContextCompat.getColor(
                           requireContext(),
                           R.color.primary
                       )
                   )
               }
           }


        })
        binding.signup02NickNameDuplicateBtn.setOnClickListener {
            SignUpService.signUpNickNameDuplicate(this, binding.signup02NickNameEt.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        savedState = saveState()
        birthTextView = null
    }

    private fun saveState(): Bundle { /* called either from onDestroyView() or onSaveInstanceState() */
        val state = Bundle()
        if (birthTextView?.text != null)
            state.putCharSequence("birth", birthTextView!!.getText()!!)

        return state
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(
            "savedState",
            if (savedState != null) savedState else saveState()
        )
    }

    private fun BirthNumberPicker() {

        binding.signup02NumberPickerLinearLayoutLl.visibility = View.VISIBLE

        npYear = binding.signupNumberPickerYear
        npMonth = binding.signupNumberPickerMonth
        npDay = binding.signupNumberPickerDay

        var yearList = ArrayList<String>()
        var monthList = ArrayList<String>()
        var dateList = ArrayList<String>()

        for (i: Int in 2022 downTo 1922)
            yearList.add(i.toString() + "년")

        for (i: Int in 1..12)
            monthList.add(i.toString() + "월")

        for (i: Int in 1..31)
            dateList.add(i.toString() + "일")

        npYear.run {
            minValue = 1
            maxValue = 100
            wrapSelectorWheel = false
            value = 1
            displayedValues = yearList.toTypedArray()
        }
        npMonth.run {
            minValue = 1
            maxValue = 12
            wrapSelectorWheel = false
            value = 1
            displayedValues = monthList.toTypedArray()

        }
        npDay.run {
            minValue = 1
            maxValue = 31
            wrapSelectorWheel = false
            value = 1
            displayedValues = dateList.toTypedArray()
        }
    }

    fun setBirth(){
        viewModel.birthYear.value = 2022 - npYear.value + 1
        viewModel.birthMonth.value = npMonth.value
        viewModel.birthDay.value = npDay.value

        binding.signup02BirthEt.text = ((2022 - npYear.value + 1).toString() + "년 " + npMonth.value.toString() + "월 " + npDay.value.toString() + " 일 ")
        binding.signup02NumberPickerLinearLayoutLl.visibility = View.GONE
    }

    fun setRadioButton(radioButton: String) {
        viewModel.gender.value = radioButton
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onSignUpNickNameSuccess() {
        isDuplicated = false
        binding.signup02NickNameDuplicateBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
        binding.signup02NickNameDuplicateBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.dark_gray_A
            )
        )
        binding.signup02NickNameDuplicateBtn.isEnabled = false
        binding.signup02NickNameLayout.isEndIconVisible = false
    }

    override fun onSignUpNickNameFailure(code: Int, message: String) {
        binding.signup02NickNameErrorTv.visibility = View.VISIBLE
        binding.signup02NickNameErrorTv.text = message
        binding.signup02NickNameCheckIconIv.visibility = View.VISIBLE
        binding.signup02NickNameCheckIconIv.setImageResource(R.drawable.ic_signup_nickname_unable)
        binding.signup02NickNameDuplicateBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
        binding.signup02NickNameDuplicateBtn.setTextColor(
            ContextCompat.getColor(
                requireContext(),
                R.color.dark_gray_A
            )
        )
    }

}