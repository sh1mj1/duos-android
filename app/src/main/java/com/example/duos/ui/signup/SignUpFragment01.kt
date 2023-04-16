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
import com.example.duos.utils.ViewModel
import android.content.Context
import android.graphics.Bitmap

import android.text.Editable

import android.text.TextWatcher
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.example.duos.data.remote.signUp.SignUpService
import java.util.regex.Pattern
import kotlin.concurrent.timer

class SignUpFragment01() : Fragment(), SignUpCreateAuthNumView, SignUpVerifyAuthNumView {

    lateinit var binding: FragmentSignup01Binding
    var savedState: Bundle? = null
    lateinit var mContext: SignUpActivity
    lateinit var signupNextBtnListener: SignUpNextBtnInterface
    lateinit var onGoingNextListener: SignUpGoNextInterface
    lateinit var viewModel: ViewModel


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
        binding = FragmentSignup01Binding.inflate(inflater, container, false)
        signupNextBtnListener = mContext
        onGoingNextListener = mContext
        viewModel = ViewModelProvider(requireActivity()).get(ViewModel::class.java)
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "01"
        binding.signup01PhoneNumberEt.addTextChangedListener(PhoneNumberFormattingTextWatcher())


        if (savedInstanceState != null && savedState == null) {
            savedState = savedInstanceState.getBundle("savedState")
        }
        if (savedState != null) {
            binding.signup01PhoneNumberCheckIconIv.visibility = View.VISIBLE
            binding.signup01PhoneNumberCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check_done)
            binding.signup01PhoneNumberVerifyingBtn.visibility = View.GONE
            binding.signup01PhoneNumberLayout.isEndIconVisible = false
            binding.signup01PhoneVerifyingNoticeTv.visibility = View.INVISIBLE

            val param =
                binding.signup01ConstraintLayout01Cl.layoutParams as ViewGroup.MarginLayoutParams
            param.marginEnd = 20.toDp(requireContext())
            binding.signup01ConstraintLayout01Cl.layoutParams = param
        } else{
            Log.d("SignUp", "else")
            viewModel.phoneNumber.value = ""
            signupNextBtnListener.onNextBtnUnable()
        }
        savedState = null
//
//        // skip 테스트 버튼
//        binding.signup01SkipBtn.setOnClickListener { onGoingNextListener.onGoingNext() }

        //val bitmap = data?.getParcelableExtra<Bitmap>("data")
        //profileBitmap = bitmap!!

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewmodel = viewModel

        if (viewModel.signUp01Avail.value == true){
            signupNextBtnListener.onNextBtnEnable()
        }

        editTextOnChanged()
        editTextOnFocus()

        binding.signup01PhoneNumberVerifyingBtn.setOnClickListener { verifyingBtnOnClick() }

        this.viewModel.phoneNumber.observe(viewLifecycleOwner, {
            Log.d("폰","observe")
            val pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
            val matcher = pattern.matcher(it);
            if (it!!.length == 13) {
                if (matcher.matches()) {
                    this.viewModel.phoneNumberVerifying.observe(requireActivity(), { it2 ->
                        Log.d("폰인증","observe")
                        if (it2.length == 6) {
                            signupNextBtnListener.onNextBtnEnable()
                            viewModel.signUp01Avail.value = true
                        } else{ signupNextBtnListener.onNextBtnUnable()
                            viewModel.signUp01Avail.value = false}
                    })
                } else {signupNextBtnListener.onNextBtnUnable()
                    viewModel.signUp01Avail.value = false}
            } else {signupNextBtnListener.onNextBtnUnable()
                viewModel.signUp01Avail.value = false}
        })
    }

    private fun editTextOnChanged() {
        binding.signup01PhoneNumberEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (binding.signup01PhoneNumberEt.text?.length!! < 13) {
                    binding.signup01PhoneNumberCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check)
                    val param =
                        binding.signup01ConstraintLayout01Cl.layoutParams as ViewGroup.MarginLayoutParams
                    param.marginEnd = 100.toDp(requireContext())
                    binding.signup01ConstraintLayout01Cl.layoutParams = param
                    binding.signup01PhoneNumberLayout.isEndIconVisible = true
                    binding.signup01PhoneNumberVerifyingBtn.visibility = View.VISIBLE
                    binding.signup01PhoneNumberVerifyingBtn.isEnabled = false
                    binding.signup01PhoneVerifyingAgainTv.visibility = View.INVISIBLE
                    binding.signup01PhoneNumberVerifyingBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
                    binding.signup01PhoneNumberVerifyingBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_gray_A
                        )
                    )
                }
            }

            override fun afterTextChanged(editable: Editable) {
                if (binding.signup01PhoneNumberEt.text?.length!! == 13) {
                    val pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
                    val matcher = pattern.matcher(editable);
                    if (matcher.matches()) {
                        binding.signup01PhoneNumberCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check_done)
                        binding.signup01PhoneNumberVerifyingBtn.isEnabled = true
                        binding.signup01PhoneVerifyingNoticeTv.visibility = View.INVISIBLE
                        binding.signup01PhoneNumberVerifyingBtn.setBackgroundResource(R.drawable.signup_phone_verifying_done_rectangular)
                        binding.signup01PhoneNumberVerifyingBtn.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.primary
                            )
                        )
                    }
                } else {
                    binding.signup01PhoneNumberCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check)
                    binding.signup01PhoneNumberVerifyingBtn.isEnabled = false
                    binding.signup01PhoneNumberVerifyingBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
                    binding.signup01PhoneNumberVerifyingBtn.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_gray_A
                        )
                    )
                }
            }
        })
    }

    private fun editTextOnFocus() {
        binding.signup01PhoneNumberEt.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    if (binding.signup01PhoneNumberEt.text?.length == 13) {
                        binding.signup01PhoneVerifyingNoticeTv.visibility = View.INVISIBLE
                    } else {
                        binding.signup01ConstraintLayout01Cl.setBackgroundResource(R.drawable.edt_underline_selected)
                        binding.signup01PhoneNumberTv.setTextColor(
                            ContextCompat.getColor(
                                requireContext(),
                                R.color.nero
                            )
                        )
                        binding.signup01PhoneNumberCheckIconIv.visibility = View.VISIBLE
                        binding.signup01PhoneVerifyingNoticeTv.visibility = View.VISIBLE
                        binding.signup01PhoneVerifyingErrorTv.visibility = View.INVISIBLE
                    }
                } else {
                    binding.signup01ConstraintLayout01Cl.setBackgroundResource(R.drawable.edt_underline_unselected)
                    binding.signup01PhoneNumberTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_gray_B4
                        )
                    )
                }
            }

        binding.signup01PhoneNumberVerifyingEt.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.signup01ConstraintLayout02Cl.setBackgroundResource(R.drawable.edt_underline_selected)
                    binding.signup01PhoneVerifyingTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.nero
                        )
                    )
                    binding.signup01PhoneVerifyingNoticeTv.visibility = View.INVISIBLE

                } else {
                    binding.signup01ConstraintLayout02Cl.setBackgroundResource(R.drawable.edt_underline_unselected)
                    binding.signup01PhoneVerifyingTv.setTextColor(
                        ContextCompat.getColor(
                            requireContext(),
                            R.color.dark_gray_B4
                        )
                    )
                }
            }
    }

    private fun verifyingBtnOnClick() {
        val regex = Regex("[^A-Za-z0-9]")//Regex를 활용하여 특수문자 없애주기
        val phoneNumber = regex.replace(binding.signup01PhoneNumberEt.text.toString(), "")
        Log.d("휴대전화", phoneNumber)
        SignUpService.signUpCreateAuthNum(this, phoneNumber)
    }

    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

    override fun onSignUpCreateAuthNumSuccess() {
        Log.d("인증번호", "요청성공")
        binding.signup01PhoneNumberErrorTv.visibility = View.INVISIBLE
        binding.signup01PhoneNumberVerifyingBtn.visibility = View.GONE
        binding.signup01PhoneNumberLayout.isEndIconVisible = false
        binding.signup01PhoneVerifyingNoticeTv.visibility = View.INVISIBLE

        val param =
            binding.signup01ConstraintLayout01Cl.layoutParams as ViewGroup.MarginLayoutParams
        param.marginEnd = 20.toDp(requireContext())
        binding.signup01ConstraintLayout01Cl.layoutParams = param


        // 5분 타이머 및 인증번호 다시 받기 글자 활성화
        binding.signup01PhoneVerifyingAgainTv.visibility = View.VISIBLE
        setTimer()
    }

    override fun onSignUpCreateAuthNumFailure(code: Int, message: String) {
        when (code) {
            5002, 5006, 5013 -> {
                binding.signup01PhoneNumberErrorTv.visibility = View.VISIBLE
                binding.signup01PhoneNumberErrorTv.text = message
            }
            else -> {
                binding.signup01PhoneVerifyingNoticeTv.visibility = View.INVISIBLE
                binding.signup01PhoneVerifyingErrorTv.visibility = View.VISIBLE
                binding.signup01PhoneVerifyingErrorTv.text = message
            }
        }
    }

    override fun onSignUpVerifyAuthNumSuccess() {
        // 인증번호 확인 완료
        onGoingNextListener.onGoingNext()
    }

    override fun onSignUpVerifyAuthNumFailure(code: Int, message: String) {
        // 인증번호 확인 실패
        binding.signup01PhoneVerifyingErrorTv.visibility = View.VISIBLE
        binding.signup01PhoneVerifyingErrorTv.text = message
    }

    fun verifyAuthNum() {
        val regex = Regex("[^A-Za-z0-9]")//Regex를 활용하여 특수문자 없애주기
        val phoneNumber = regex.replace(this.binding.signup01PhoneNumberEt.text.toString(), "")
        val phoneNumVerifying = binding.signup01PhoneNumberVerifyingEt.text.toString()
        SignUpService.signUpVerifyAuthNum(this, phoneNumber, phoneNumVerifying)
    }

    private fun setTimer() {
        var second = 0
        var minute = 0
        val timeTick = 300 // 5분->300초
        second = timeTick % 60
        minute = timeTick / 60
        binding.signup01PhoneVerifyingTimeTv.visibility = View.VISIBLE
        binding.signup01PhoneVerifyingTimeTv.text = String.format("0$minute : %02d", second)
        timer(period = 1000, initialDelay = 1000) {
            activity?.runOnUiThread {
                binding.signup01PhoneVerifyingTimeTv.text =
                    String.format("0$minute : %02d", second)
                binding.signup01PhoneVerifyingAgainTv.setOnClickListener {
                    Log.d("다시", "보내기")
                    this.cancel()
                    binding.signup01PhoneVerifyingTimeTv.visibility = View.INVISIBLE
                    verifyingBtnOnClick()

                }
                binding.signup01PhoneNumberVerifyingBtn.setOnClickListener {
                    this.cancel()
                    binding.signup01PhoneVerifyingTimeTv.visibility = View.INVISIBLE
                    verifyingBtnOnClick()
                }
            }
            if (second == 0 && minute == 0) {
                cancel()
                activity?.runOnUiThread {
                    binding.signup01PhoneVerifyingTimeTv.text = String.format("00 : 00")
                    binding.signup01PhoneVerifyingTimeTv.visibility = View.INVISIBLE
                }
                return@timer
            }
            if (second == 0) {
                minute--
                second = 60
            }
            second--
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        savedState = saveState() /* vstup defined here for sure */
    }

    private fun saveState(): Bundle { /* called either from onDestroyView() or onSaveInstanceState() */
        val state = Bundle()
        state.putBoolean("state", true)
        return state
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBundle(
            "savedState",
            if (savedState != null) savedState else saveState()
        )
    }


}