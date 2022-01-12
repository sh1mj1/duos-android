package com.example.duos.ui.siginup


import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.NumberPicker
import androidx.appcompat.app.AlertDialog
import com.example.duos.R
import com.example.duos.databinding.FragmentSignup02Binding
import com.example.duos.ui.BaseFragment

class SignUpFragment02() : BaseFragment<FragmentSignup02Binding>(FragmentSignup02Binding::inflate) {

    override fun initAfterBinding() {
        binding.signup02BirthEt.setOnClickListener {
            binding.signup02NpLinearLayoutLl.visibility = View.VISIBLE
            val npYear : NumberPicker = binding.signupNumberPickerYear
            val npMonth : NumberPicker = binding.signupNumberPickerMonth
            val npDay : NumberPicker = binding.signupNumberPickerDay

            var yearList = ArrayList<String>()
            var monthList = ArrayList<String>()
            var dateList = ArrayList<String>()

            for (i: Int in 2022 downTo 1922)
                yearList.add(i.toString() + "년")

            for (i: Int in 12 downTo 1)
                monthList.add(i.toString() + "월")

            for (i: Int in 31 downTo 1)
                dateList.add(i.toString() + "일")

            npYear.run {
                minValue = 0
                maxValue = yearList.size - 1
                wrapSelectorWheel = false
                displayedValues = yearList.toTypedArray()
            }
            npMonth.run {
                minValue = 0
                maxValue = monthList.size - 1
                wrapSelectorWheel = false
                displayedValues = monthList.toTypedArray()

            }
            npDay.run {
                minValue = 0
                maxValue = dateList.size - 1
                wrapSelectorWheel = false
                displayedValues = dateList.toTypedArray()
            }

        }
    }

}