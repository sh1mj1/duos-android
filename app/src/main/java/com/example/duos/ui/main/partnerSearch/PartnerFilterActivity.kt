package com.example.duos.ui.main.partnerSearch

import com.example.duos.ToggleButtonGroupTableLayout
import com.example.duos.databinding.ActivityPartnerFilterBinding
import com.example.duos.ui.BaseActivity
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener

import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar

import android.R
import android.content.Context
import android.os.VibrationEffect
import android.os.Vibrator

import android.widget.TextView

class PartnerFilterActivity: BaseActivity<ActivityPartnerFilterBinding>(ActivityPartnerFilterBinding::inflate) {
    override fun initAfterBinding() {

        val rangeSeekbar = binding.partnerFilterAgeRangeSb

        rangeSeekbar
//            .setCornerRadius(10f)
//            .setBarColor(Color.parseColor("#93F9B5"))
//            .setBarHighlightColor(Color.parseColor("#16E059"))
            .setMinValue(20F)
            .setMaxValue(60F)
            .setSteps(10F)
//            .setLeftThumbDrawable(R.drawable.thumb_android)
//            .setLeftThumbHighlightDrawable(R.drawable.thumb_android_pressed)
//            .setRightThumbDrawable(R.drawable.thumb_android)
//            .setRightThumbHighlightDrawable(R.drawable.thumb_android_pressed)
            .setDataType(CrystalRangeSeekbar.DataType.INTEGER)
            .apply()

        rangeSeekbar.setOnRangeSeekbarChangeListener { minValue, maxValue ->
            binding.partnerFilterAgeMinTv.text = minValue.toString()
            binding.partnerFilterAgeMaxTv.text = maxValue.toString()
        }
    }
}