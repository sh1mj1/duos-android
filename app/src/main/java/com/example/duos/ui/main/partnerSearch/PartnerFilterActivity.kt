package com.example.duos.ui.main.partnerSearch

import com.example.duos.databinding.ActivityPartnerFilterBinding
import com.example.duos.ui.BaseActivity
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar

class PartnerFilterActivity: BaseActivity<ActivityPartnerFilterBinding>(ActivityPartnerFilterBinding::inflate) {
    override fun initAfterBinding() {

        val rangeSeekbar = binding.partnerFilterAgeRangeSb
        rangeSeekbar.steps = 10

        rangeSeekbar.setOnRangeChangedListener(object: OnRangeChangedListener {
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                binding.partnerFilterAgeMinTv.text = leftValue.toInt().toString()
                binding.partnerFilterAgeMaxTv.text = rightValue.toInt().toString()
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })
    }
}