package com.example.duos.ui.signup

import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R

import com.example.duos.databinding.ActivitySignupBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.MainActivity
import com.example.duos.ToggleButtonInterface
import com.example.duos.data.entities.User
import com.example.duos.data.local.UserDatabase
import com.example.duos.utils.SignUpInfoViewModel


class SignUpActivity: BaseActivity<ActivitySignupBinding>(ActivitySignupBinding::inflate), SignUpView, SignUpNextBtnInterface ,
    ToggleButtonInterface {

    var checkBtn : Boolean = true
    lateinit var myFragment : SignUpFragment02
    override fun initAfterBinding() {

        supportFragmentManager.beginTransaction().replace(R.id.signup_fragment_container_fc, SignUpFragment01())
            .commitAllowingStateLoss()

        myFragment = SignUpFragment02()

        binding.signupNextBtn.setOnClickListener {
            if (checkBtn){
                initNavController()
            } else{
                onNextBtnChanged(false)
                myFragment.setBirth()
            }
        }
        binding.signupBackArrowIv.setOnClickListener {
            if (supportFragmentManager.findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01){
                finish()
            }
            else{
                this.getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().getFragments().get(0)).commit()
                this.getSupportFragmentManager().popBackStack();
            }

        }
    }

    private fun initNavController(){

        supportFragmentManager.run {
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment01){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, myFragment)
                    .addToBackStack(null)
                    .commit()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment02){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment03())
                    .addToBackStack(null)
                    .commit()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment03){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment04())
                    .addToBackStack(null)
                    .commit()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment04){
                beginTransaction()
                    .replace(R.id.signup_fragment_container_fc, SignUpFragment05())
                    .addToBackStack(null)
                    .commit()
            }
            if (findFragmentById(R.id.signup_fragment_container_fc) is SignUpFragment05){
                val intent: Intent = Intent(
                    findFragmentById(R.id.signup_fragment_container_fc)?.requireContext(),
                    MainActivity::class.java)
                findFragmentById(R.id.signup_fragment_container_fc)?.requireContext()?.startActivity(intent)
            }
        }
    }

    private fun signUp() {
//        if (binding.signUpIdEt.text.toString()
//                .isEmpty() || binding.signUpDirectInputEt.text.toString().isEmpty()
//        ) {
//            Toast.makeText(this, "이메일 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (binding.signUpNameEt.text.toString().isEmpty()) {
//            Toast.makeText(this, "이름 형식이 잘못되었습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        if (binding.signUpPasswordEt.text.toString() != binding.signUpPasswordCheckEt.text.toString()) {
//            Toast.makeText(this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show()
//            return
//        }
//
//        AuthService.signUp(this, getUser())
    }

    override fun onSignUpLoading() {
//        binding.signUpLoadingPb.visibility = View.VISIBLE
    }

    override fun onSignUpSuccess() {
//        binding.signUpLoadingPb.visibility = View.GONE
//
//        finish()
    }

    override fun onSignUpFailure(code: Int, message: String) {
//        binding.signUpLoadingPb.visibility = View.GONE
//
//        when(code) {
//            2016, 2017 -> {
//                binding.signUpEmailErrorTv.visibility = View.VISIBLE
//                binding.signUpEmailErrorTv.text = message
//            }
//        }
    }

    override fun onNextBtnChanged(boolean: Boolean){

        // 다음 -> 완료로 다시 바꾸기
        if (boolean){
            binding.signupNextBtn.setText(getText(R.string.signup_next_btn_02))
            binding.signupNextBtn.setTextColor(getColor(R.color.white))
            binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_done_rectangular)
            checkBtn = false
        }
        // 완료 -> 다음으로 바꾸기
        else{
            binding.signupNextBtn.setText(getText(R.string.signup_next_btn))
            binding.signupNextBtn.background = getDrawable(R.drawable.signup_next_btn_rectangular)
            binding.signupNextBtn.setTextColor(getColor(R.color.dark_gray_B0))
            checkBtn = true
        }
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