package com.example.duos.ui.main.mypage.myprofile.editprofile

import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.ToggleButtonInterface
import com.example.duos.databinding.ActivityEditProfileBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.utils.ViewModel

class EditProfileActivity :
    BaseActivity<ActivityEditProfileBinding>(ActivityEditProfileBinding::inflate),
    ToggleButtonInterface {

    lateinit var viewModel: ViewModel

    override fun initAfterBinding() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.edit_profile_into_fragment_container_fc, EditProfileFragment())
            .commitAllowingStateLoss()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModel::class.java)
    }

    override fun setRadiobutton(tag: String) {
        val fragment = supportFragmentManager.findFragmentById(R.id.edit_profile_into_fragment_container_fc)
        if (fragment is EditProfileFragment) {
            fragment.setRadioButton(tag.toInt())
        }
    }


}