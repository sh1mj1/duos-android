package com.example.duos.ui.login

import android.content.Context
import android.content.Intent
import android.telephony.PhoneNumberFormattingTextWatcher
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.data.local.UserDatabase
import com.example.duos.data.remote.auth.Auth
import com.example.duos.data.remote.login.LoginService
import com.example.duos.data.remote.login.LoginVerifyAuthNumResultData
import com.example.duos.data.remote.signUp.SignUpService
import com.example.duos.databinding.ActivityLoginBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.splash.SplashActivity
import com.example.duos.utils.ViewModel
import java.util.regex.Pattern
import kotlin.concurrent.timer
import android.content.SharedPreferences
import com.example.duos.utils.saveJwt
import com.example.duos.utils.saveRefreshJwt


class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate),
    LoginCreateAuthNumView, LoginVerifyAuthNumView {

    lateinit var viewModel: ViewModel

    override fun initAfterBinding() {
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModel::class.java)
        binding.viewmodel = viewModel
        binding.loginPhoneNumberVerifyingBtn.setOnClickListener { verifyingBtnOnClick() }
        binding.loginNextBtn.setOnClickListener { verifyAuthNum() }
        binding.loginPhoneNumberEt.addTextChangedListener(PhoneNumberFormattingTextWatcher())
        binding.loginBackArrowIv.setOnClickListener {
            val intent = Intent(this, SplashActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

         viewModel.loginPhoneNumber.observe(this, {
            val pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
            val matcher = pattern.matcher(it);
            if (it!!.length == 13) {
                if (matcher.matches()) {
                    this.viewModel.loginPhoneNumberVerifying.observe(this, { it2 ->
                        if (it2.length == 6) {
                            binding.loginNextBtn.isEnabled = true
                        } else binding.loginNextBtn.isEnabled = false

                    })
                } else binding.loginNextBtn.isEnabled = false
            } else binding.loginNextBtn.isEnabled = false
        })

        binding.loginPhoneNumberEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                if (binding.loginPhoneNumberEt.text?.length!! < 13) {
                    binding.loginPhoneNumberCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check)
                    val param =
                        binding.loginConstraintLayout01Cl.layoutParams as ViewGroup.MarginLayoutParams
                    param.marginEnd = 100.toDp(applicationContext)
                    binding.loginConstraintLayout01Cl.layoutParams = param
                    binding.loginPhoneNumberLayout.isEndIconVisible = true
                    binding.loginPhoneNumberVerifyingBtn.visibility = View.VISIBLE
                    binding.loginPhoneNumberVerifyingBtn.isEnabled = false
                    binding.loginPhoneVerifyingAgainTv.visibility = View.INVISIBLE
                    binding.loginPhoneNumberVerifyingBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
                    binding.loginPhoneNumberVerifyingBtn.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.dark_gray_A
                        )
                    )
                }
            }

            override fun afterTextChanged(s: Editable?) {
                if (binding.loginPhoneNumberEt.text?.length!! == 13) {
                    val pattern = Pattern.compile("\\d{3}-\\d{4}-\\d{4}");
                    val matcher = pattern.matcher(s);
                    if (matcher.matches()) {
                        binding.loginPhoneNumberCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check_done)
                        binding.loginPhoneNumberVerifyingBtn.isEnabled = true
                        binding.loginPhoneVerifyingNoticeTv.visibility = View.INVISIBLE
                        binding.loginPhoneNumberVerifyingBtn.setBackgroundResource(R.drawable.signup_phone_verifying_done_rectangular)
                        binding.loginPhoneNumberVerifyingBtn.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.primary
                            )
                        )
                    }
                } else {
                    binding.loginPhoneNumberCheckIconIv.setImageResource(R.drawable.ic_signup_phone_verifying_check)
                    binding.loginPhoneNumberVerifyingBtn.isEnabled = false
                    binding.loginPhoneNumberVerifyingBtn.setBackgroundResource(R.drawable.signup_phone_verifying_rectangular)
                    binding.loginPhoneNumberVerifyingBtn.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.dark_gray_A
                        )
                    )
                }
            }
        })

        binding.loginPhoneNumberEt.onFocusChangeListener =
            View.OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    if (binding.loginPhoneNumberEt.text?.length == 13) {
                        binding.loginPhoneVerifyingNoticeTv.visibility = View.INVISIBLE
                    } else {
                        binding.loginConstraintLayout01Cl.setBackgroundResource(R.drawable.edt_underline_selected)
                        binding.loginPhoneNumberTv.setTextColor(
                            ContextCompat.getColor(
                                applicationContext,
                                R.color.nero
                            )
                        )
                        binding.loginPhoneNumberCheckIconIv.visibility = View.VISIBLE
                        binding.loginPhoneVerifyingNoticeTv.visibility = View.VISIBLE
                        binding.loginPhoneNumberErrorTv.visibility = View.INVISIBLE
                    }
                } else {
                    binding.loginConstraintLayout01Cl.setBackgroundResource(R.drawable.edt_underline_unselected)
                    binding.loginPhoneNumberTv.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.dark_gray_B4
                        )
                    )
                }
            }

    }

    private fun verifyingBtnOnClick() {
        val regex = Regex("[^A-Za-z0-9]")//Regex를 활용하여 특수문자 없애주기
        val phoneNumber = regex.replace(binding.loginPhoneNumberEt.text.toString(), "")
        Log.d("휴대전화", phoneNumber)
        LoginService.loginCreateAuthNum(this, phoneNumber)
    }

    fun verifyAuthNum() {
        val regex = Regex("[^A-Za-z0-9]")//Regex를 활용하여 특수문자 없애주기
        val phoneNumber = regex.replace(this.binding.loginPhoneNumberEt.text.toString(), "")
        val phoneNumVerifying = binding.loginPhoneNumberVerifyingEt.text.toString()
        LoginService.loginVerifyAuthNum(this, phoneNumber, phoneNumVerifying)
    }

    override fun onBackPressed() {
        val intent = Intent(this, SplashActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onLoginCreateAuthNumSuccess() {
        Log.d("인증번호", "요청성공")
        binding.loginPhoneNumberErrorTv.visibility = View.INVISIBLE
        binding.loginPhoneNumberVerifyingBtn.visibility = View.GONE
        binding.loginPhoneNumberLayout.isEndIconVisible = false
        binding.loginPhoneVerifyingNoticeTv.visibility = View.INVISIBLE

        val param =
            binding.loginConstraintLayout01Cl.layoutParams as ViewGroup.MarginLayoutParams
        param.marginEnd = 20.toDp(applicationContext)
        binding.loginConstraintLayout01Cl.layoutParams = param

        // 5분 타이머 및 인증번호 다시 받기 글자 활성화
        binding.loginPhoneVerifyingAgainTv.visibility = View.VISIBLE
        setTimer()
    }

    override fun onLoginCreateAuthNumFailure(code: Int, message: String) {

        binding.loginPhoneVerifyingNoticeTv.visibility = View.INVISIBLE
        binding.loginPhoneVerifyingErrorTv.visibility = View.VISIBLE
        binding.loginPhoneVerifyingErrorTv.text = message

    }

    private fun setTimer() {
        var second = 0
        var minute = 0
        val timeTick = 300 // 5분->300초
        second = timeTick % 60
        minute = timeTick / 60
        binding.loginPhoneVerifyingTimeTv.visibility = View.VISIBLE
        binding.loginPhoneVerifyingTimeTv.text = String.format("0$minute : %02d", second)
        timer(period = 1000, initialDelay = 1000) {
            runOnUiThread {
                binding.loginPhoneVerifyingTimeTv.text =
                    String.format("0$minute : %02d", second)
                binding.loginPhoneVerifyingAgainTv.setOnClickListener {
                    Log.d("다시", "보내기")
                    this.cancel()
                    binding.loginPhoneVerifyingTimeTv.visibility = View.INVISIBLE
                    verifyingBtnOnClick()

                }
                binding.loginPhoneNumberVerifyingBtn.setOnClickListener {
                    this.cancel()
                    binding.loginPhoneVerifyingTimeTv.visibility = View.INVISIBLE
                    verifyingBtnOnClick()
                }
            }
            if (second == 0 && minute == 0) {
                cancel()
                runOnUiThread {
                    binding.loginPhoneVerifyingTimeTv.text = String.format("00 : 00")
                    binding.loginPhoneVerifyingTimeTv.visibility = View.INVISIBLE
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

    fun Int.toDp(context: Context): Int = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics
    ).toInt()

    override fun onLoginVerifyAuthNumSuccess(result : LoginVerifyAuthNumResultData) {
        // 인증번호 확인 성공!
        // 사용자 정보 및 jwt토큰 저장
        val db = UserDatabase.getInstance(applicationContext)
        val user = db!!.userDao().getUser(result.nickname)
        user.userIdx = result.userIdx
        Log.d("user",user.toString())
        db!!.userDao().update(user)

        saveJwt(result.jwtAccessToken)
        saveRefreshJwt(result.jwtRefreshToken)

        // MainActivity 로 이동
        val intent = Intent(this, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun onLoginVerifyAuthNumFailure(code: Int, message: String) {
        // 인증번호 확인 실패!
        binding.loginPhoneVerifyingErrorTv.visibility = View.VISIBLE
        binding.loginPhoneVerifyingErrorTv.text = message
    }

}