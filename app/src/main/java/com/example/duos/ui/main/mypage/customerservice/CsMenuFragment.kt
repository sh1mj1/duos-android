package com.example.duos.ui.main.mypage.customerservice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.duos.R
import com.example.duos.databinding.FragmentCsMenuBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.notion.NotionActivity

class CsMenuFragment : BaseFragment<FragmentCsMenuBinding>(FragmentCsMenuBinding::inflate) {

    override fun initAfterBinding() {

        (context as CustomerServiceActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            requireActivity().finish()
        }

    }

}
