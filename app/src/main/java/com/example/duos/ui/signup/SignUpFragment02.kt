package com.example.duos.ui.signup


import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
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
    lateinit var signupNextBtnListener: SignUpNextBtnInterface
    lateinit var mContext: SignUpActivity
    var savedState: Bundle? = null
    var birthTextView: TextView? = null
    lateinit var npYear: NumberPicker
    lateinit var npMonth: NumberPicker
    lateinit var npDay: NumberPicker
    lateinit var viewModel: SignUpInfoViewModel

    override fun onAttach(context: Context) {
        Log.d("ㅎㅇ","onAttach")
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
        Log.d("ㅎㅇ","onCreateView")
        binding = FragmentSignup02Binding.inflate(inflater, container, false)
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "02"
        birthNextBtnListener = mContext
        signupNextBtnListener = mContext
        viewModel = ViewModelProvider(requireActivity()).get(SignUpInfoViewModel::class.java)


        binding.signup02BirthEt.setOnClickListener {
            birthNextBtnListener.onNextBtnChanged(true)
            birthNumberPicker()
        }

        birthTextView = binding.signup02BirthEt
        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("savedState")
        }
        else {
            Log.d("텍스트",binding.signup02NickNameEt.text.toString())
        }
        if (savedState != null) {
            birthTextView!!.text = savedState!!.getCharSequence("birth");
        }
        savedState = null

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.d("ㅎㅇ","onviewcreated")
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        this.viewModel.nickName.observe(requireActivity(), {
            val pattern = "^[0-9|a-z|A-Z|ㄱ-ㅎ|ㅏ-ㅣ|가-힣]*$"
            if (it.isNotEmpty()) {
                binding.signup02NickNameLayout.isEndIconVisible = true
                if (!Pattern.matches(pattern, it.toString()) or (it.length < 2)) {
                    binding.signup02NickNameErrorTv.visibility = View.VISIBLE
                    binding.signup02NickNameCheckIconIv.visibility = View.VISIBLE
                    binding.signup02NickNameCheckIconIv.setImageResource(R.drawable.ic_signup_nickname_unable)
                    binding.signup02NickNameDuplicateBtn.isEnabled = false
                    binding.signup02NickNameDuplicateBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
                    binding.signup02NickNameDuplicateBtn.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.dark_gray_A
                        )
                    )
                } else {
                    binding.signup02NickNameErrorTv.visibility = View.INVISIBLE
                    binding.signup02NickNameCheckIconIv.visibility = View.VISIBLE
                    binding.signup02NickNameCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check_done)
                    binding.signup02NickNameDuplicateBtn.isEnabled = true
                    binding.signup02NickNameDuplicateBtn.setBackgroundResource(R.drawable.signup_phone_verifying_done_rectangular)
                    binding.signup02NickNameDuplicateBtn.setTextColor(
                        ContextCompat.getColor(
                            mContext,
                            R.color.primary
                        )
                    )
                }
            }
        })

        binding.signup02NickNameDuplicateBtn.setOnClickListener {
            SignUpService.signUpNickNameDuplicate(this, binding.signup02NickNameEt.text.toString())
        }

        this.viewModel.setNickname.observe(requireActivity(), {
            if (it) {
                this.viewModel.setGender.observe(requireActivity(), { it2 ->
                    if (it2) {
                        this.viewModel.setBirth.observe(requireActivity(), { it3 ->
                            if (it3) {
                                signupNextBtnListener.onNextBtnEnable()
                                viewModel.signUp02Avail.value = true
                            } else {signupNextBtnListener.onNextBtnUnable()
                                viewModel.signUp02Avail.value = false}
                        })
                    } else {signupNextBtnListener.onNextBtnUnable()
                        viewModel.signUp02Avail.value = false}
                })
            } else { signupNextBtnListener.onNextBtnUnable()
                viewModel.signUp02Avail.value = false}
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        savedState = saveState()
        birthTextView = null
    }

    private fun saveState(): Bundle { /* called either from onDestroyView() or onSaveInstanceState() */
        val state = Bundle()
        if (birthTextView?.text != null)
            state.putCharSequence("birth", birthTextView!!.text!!)

        return state
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(
            "savedState",
            if (savedState != null) savedState else saveState()
        )
    }

    private fun birthNumberPicker() {

        binding.signup02NumberPickerLinearLayoutLl.visibility = View.VISIBLE
        binding.signup02RootConstraintLayoutCl.setOnClickListener {
            binding.signup02NumberPickerLinearLayoutLl.visibility = View.GONE
            birthNextBtnListener.onNextBtnChanged(false)
        }
        binding.signup02NickNameEt.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.signup02NumberPickerLinearLayoutLl.visibility = View.GONE
                }
            }

        binding.signup02NickNameEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                signupNextBtnListener.onNextBtnUnable()
            }

        })

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

    fun setBirth() {
        viewModel.birthYear.value = 2022 - npYear.value + 1
        viewModel.birthMonth.value = npMonth.value
        viewModel.birthDay.value = npDay.value

        binding.signup02BirthEt.text =
            ((2022 - npYear.value + 1).toString() + "년 " + npMonth.value.toString() + "월 " + npDay.value.toString() + " 일 ")
        binding.signup02NumberPickerLinearLayoutLl.visibility = View.GONE

        viewModel.setBirth.value = true
    }

    fun setRadioButton(radioButton: String) {
        viewModel.gender.value = radioButton
        viewModel.setGender.value = true
    }

    override fun onSignUpNickNameSuccess() {
        viewModel.setNickname.value = true
        binding.signup02NickNameDuplicateBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
        binding.signup02NickNameDuplicateBtn.setTextColor(
            ContextCompat.getColor(
                mContext,
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
                mContext,
                R.color.dark_gray_A
            )
        )
    }

}