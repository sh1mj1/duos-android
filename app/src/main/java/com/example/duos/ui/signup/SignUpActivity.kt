package com.example.duos.ui.signup

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R

import com.example.duos.databinding.ActivitySignupBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.MainActivity
import com.example.duos.ToggleButtonInterface
import com.example.duos.data.entities.User
import com.example.duos.data.local.UserDatabase
import com.example.duos.ui.login.LoginActivity
import com.example.duos.utils.SignUpInfoViewModel


class SignUpActivity : BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate),
    SignUpBirthNextBtnInterface,
    ToggleButtonInterface, SignUpNextBtnInterface, SignUpGoNextInterface {

    private var checkBtn: Boolean = true

    override fun initAfterBinding() {

        supportFragmentManager.beginTransaction()
            .replace(R.id.signup_fragment_container_fc, SignUpFragment01())
            .commitAllowingStateLoss()

        binding.signupNextBtn.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01) {
                // 인증번호 인증하기
                Log.d("ㅎㅇ","6")
                (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) as SignUpFragment01).verifyAuthNum()
            }

            else if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment02) {
                if (checkBtn) {
                    Log.d("ㅎㅇ","1")
                    initNavController()
                } else {
                    Log.d("ㅎㅇ","2")
                    onNextBtnChanged(false)
                    (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) as SignUpFragment02).setBirth()
                }
            }
            else if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment05){
                // 회원가입 전체 정보 post 하기
                (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) as SignUpFragment05).signUpPost()
            }
            else {
                Log.d("ㅎㅇ","5")
                initNavController()
            }

        }
        binding.signupBackArrowIv.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01) {
                finish()
            } else {
                onBackPressed()
            }

        }
    }

    private fun initNavController() {

        supportFragmentManager.run {
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01) {
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment02())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment02) {
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment03())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment03) {
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment04())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment04) {
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment05())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment05) {
                val intent = Intent(
                    findFragmentById(R.id.signup_fragment_container_fc)?.requireContext(),
                    LoginActivity::class.java
                )
                findFragmentById(R.id.signup_fragment_container_fc)?.requireContext()
                    ?.startActivity(intent)

            }
        }
    }

    override fun onNextBtnChanged(boolean: Boolean) {
        // 다음 -> 완료로 다시 바꾸기
        if (boolean) {
            Log.d("ㅎㅇ","4")
            binding.signupNextBtn.isEnabled = true
            binding.signupNextBtn.text = getText(R.string.signup_next_btn_02)
            binding.signupNextBtn.setTextColor(getColor(R.color.white))
            binding.signupNextBtn.background =
                getDrawable(R.drawable.signup_next_btn_done_rectangular)
            checkBtn = false
        }
        // 완료 -> 다음으로 바꾸기
        else {
            Log.d("ㅎㅇ","3")
            binding.signupNextBtn.isEnabled = false
            binding.signupNextBtn.text = getText(R.string.signup_next_btn)
            binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_rectangular)
            binding.signupNextBtn.setTextColor(getColor(R.color.dark_gray_B0))
            checkBtn = true
        }
    }

    override fun onNextBtnEnable() {
        binding.signupNextBtn.isEnabled = true
        binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_done_rectangular)
        binding.signupNextBtn.setTextColor(getColor(R.color.white))
    }

    override fun onNextBtnUnable() {
        binding.signupNextBtn.isEnabled = false
        binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_rectangular)
        binding.signupNextBtn.setTextColor(getColor(R.color.dark_gray_B0))
    }

    override fun setRadiobutton(tag : String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc)
        if (fragment is SignUpFragment02) {
            fragment.setRadioButton(tag.toInt())
        } else if (fragment is SignUpFragment04) {
            fragment.setRadioButton(tag.toInt())
        }
    }

    override fun onGoingNext() {
        initNavController()
    }

    override fun onStop() {
        super.onStop()
//         roomDB 에 회원가입 정보 모두 저장
        val viewModel = ViewModelProvider(this).get(SignUpInfoViewModel::class.java)

        val user : User
        user = User(viewModel.phoneNumber.value,
        viewModel.nickName.value!!,
        viewModel.gender.value,
        viewModel.birthYear.value,
        viewModel.birthMonth.value,
        viewModel.birthDay.value,
        viewModel.locationCate.value,
        viewModel.location.value,
        viewModel.experience.value,
        viewModel.profileImg.value,
        viewModel.introduce.value)

        val roomDB = UserDatabase.getInstance(this)!!
        if (viewModel.nickName.value?.let { roomDB.userDao().getUser(it) } == null){
            roomDB.userDao().insert(user)
        } else{
            roomDB.userDao().update(user)
        }
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.run {
                supportFragmentManager.popBackStack()
            }
        }else
            super.onBackPressed()
        onNextBtnEnable()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        Log.d("onsave","액티비티")
        super.onSaveInstanceState(outState)
    }



}