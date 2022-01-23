package com.example.duos.ui.signup

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.TextView
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup05Binding
import com.example.duos.ui.BaseFragment

class SignUpFragment05 () : BaseFragment<FragmentSignup05Binding>(FragmentSignup05Binding::inflate) {

    override fun initAfterBinding() {
        requireActivity().findViewById<TextView>(R.id.signup_process_tv).text = "05"

        binding.signup05ProfileImageBackgroundIv.setOnClickListener {

            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(intent, 100)

        }

    }

    // data : Intent 에 사진 정보
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            100 ->{
                if(resultCode == RESULT_OK){
                   val bitmap =  data?.getParcelableExtra<Bitmap>("data")   // Intent 안에 "data" 라는 이름으로 사진 정보가 들어가게 됨.
                    binding.signup05ProfileImageBackgroundIv.setImageBitmap(bitmap)


                }

            }

        }

    }

}