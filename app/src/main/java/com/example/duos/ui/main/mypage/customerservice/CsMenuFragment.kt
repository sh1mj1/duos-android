package com.example.duos.ui.main.mypage.customerservice

import android.annotation.SuppressLint
import android.content.Intent
import android.widget.ImageView
import com.example.duos.R
import com.example.duos.databinding.FragmentCsMenuBinding
import com.example.duos.ui.BaseFragment

class CsMenuFragment : BaseFragment<FragmentCsMenuBinding>(FragmentCsMenuBinding::inflate) {

    @SuppressLint("QueryPermissionsNeeded")
    override fun initAfterBinding() {

        (context as CustomerServiceActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            requireActivity().finish()
        }

        binding.btnSendEmailToCsIv.setOnClickListener {
//            val intent = Intent(Intent.ACTION_SEND)
//            intent.type
//            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("duos_customercenter@duos.com"))
//            intent.putExtra(Intent.EXTRA_SUBJECT, "고객센터로 이메일 보내기")
//            intent.putExtra(Intent.EXTRA_TEXT, "Email Text")
//            startActivity(intent)

            val emailIntent = Intent(Intent.ACTION_SEND)

            try {
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("duos_customercenter@duos.com"))
                emailIntent.type = "text/html"
                emailIntent.setPackage("com.google.android.gm")
                if (emailIntent.resolveActivity((context as CustomerServiceActivity).packageManager) != null) startActivity(
                    emailIntent
                )
                startActivity(emailIntent)
            } catch (e: Exception) {
                e.printStackTrace()
                emailIntent.type = "text/html"
                emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("duos_customercenter@duos.com"))
                startActivity(Intent.createChooser(emailIntent, "Send Email"))
            }
        }

    }

}
