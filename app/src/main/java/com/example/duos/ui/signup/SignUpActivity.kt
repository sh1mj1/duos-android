package com.example.duos.ui.signup

import android.content.Intent
import com.example.duos.R

import com.example.duos.databinding.ActivitySignupBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.MainActivity
import com.example.duos.ToggleButtonInterface


class SignUpActivity: BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate), SignUpBirthNextBtnInterface ,
    ToggleButtonInterface, SignUpNextBtnInterface, SignUpGoNextInterface {

    var checkBtn : Boolean = true

    override fun initAfterBinding() {

        supportFragmentManager.beginTransaction().replace(R.id.signup_fragment_container_fc, SignUpFragment01())
            .commitAllowingStateLoss()

        binding.signupNextBtn.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01){
                // 인증번호 인증하기
                (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) as SignUpFragment01).verifyAuthNum()
                //
            }

            if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment02){
                if (checkBtn){
                    initNavController()
                } else{
                    onNextBtnChanged(false)
                    (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) as SignUpFragment02).setBirth()
                }
            }

        }
        binding.signupBackArrowIv.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01){
                finish()
            }
            else{
                this.supportFragmentManager.beginTransaction().remove(supportFragmentManager.fragments[0]).commit()
                this.supportFragmentManager.popBackStack()
            }

        }
    }

    private fun initNavController(){

        supportFragmentManager.run {
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment02())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment02){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment03())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment03){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment04())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment04){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment05())
                    .addToBackStack(null)
                    .commit()
                onNextBtnUnable()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment05){
                val intent: Intent = Intent(
                    findFragmentById(R.id.signup_fragment_container_fc)?.requireContext(),
                    MainActivity::class.java)
                findFragmentById(R.id.signup_fragment_container_fc)?.requireContext()?.startActivity(intent)

            }
        }
    }

    override fun onNextBtnChanged(boolean: Boolean){
        // 다음 -> 완료로 다시 바꾸기
        if (boolean){
            binding.signupNextBtn.isEnabled = true
            binding.signupNextBtn.setText(getText(R.string.signup_next_btn_02))
            binding.signupNextBtn.setTextColor(getColor(R.color.white))
            binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_done_rectangular)
            checkBtn = false
        }
        // 완료 -> 다음으로 바꾸기
        else{
            binding.signupNextBtn.isEnabled = false
            binding.signupNextBtn.setText(getText(R.string.signup_next_btn))
            binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_rectangular)
            binding.signupNextBtn.setTextColor(getColor(R.color.dark_gray_B0))
            checkBtn = true
        }
    }

    override fun onNextBtnEnable(){
        binding.signupNextBtn.isEnabled = true
        binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_done_rectangular)
        binding.signupNextBtn.setTextColor(getColor(R.color.white))
    }

    override fun onNextBtnUnable() {
        binding.signupNextBtn.isEnabled = false
        binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_rectangular)
        binding.signupNextBtn.setTextColor(getColor(R.color.dark_gray_B0))
    }

    override fun setRadiobutton(radioButton: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc)
        if(fragment is SignUpFragment02){
            fragment.setRadioButton(radioButton)
        }
        else if (fragment is SignUpFragment04){
            fragment.setRadioButton(radioButton)
        }
    }

    override fun onGoingNext() {
        initNavController()
    }

//    override fun onStop() {
//        super.onStop()
////         roomDB 에 회원가입 정보 모두 저장
//        val viewModel = ViewModelProvider(this).get(SignUpInfoViewModel::class.java)
//
//        val user : User
//        user = User(viewModel.phoneNumber.value,
//        viewModel.nickName.value!!,
//        viewModel.gender.value,
//        viewModel.birthYear.value,
//        viewModel.birthMonth.value,
//        viewModel.birthDay.value,
//        viewModel.locationCate.value,
//        viewModel.location.value,
//        viewModel.experience.value,
//        null,
//        viewModel.introduce.value)
//
//        val roomDB = UserDatabase.getInstance(this)!!
//        if (viewModel.nickName.value?.let { roomDB.userDao().getUser(it) } == null){
//            roomDB.userDao().insert(user)
//        } else{
//            roomDB.userDao().update(user)
//        }
//    }


}