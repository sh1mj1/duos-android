package com.example.duos.ui.main.mypage.lastpromise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.duos.databinding.FragmentPreviousGameReviewBinding

class PreviousGameReviewFragment : Fragment() {

    private var _binding: FragmentPreviousGameReviewBinding? = null
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPreviousGameReviewBinding.inflate(inflater, container, false)

        val playerNickname = arguments?.getString("playerNickname")
        val playerProfileImg = arguments?.getInt("playerProfileImg")


        return binding!!.root
    }

}