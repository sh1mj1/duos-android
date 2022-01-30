package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import android.view.View
import com.example.duos.databinding.ActivityPartnerFilterBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.MainActivity
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar

class PartnerFilterActivity: BaseActivity<ActivityPartnerFilterBinding>(ActivityPartnerFilterBinding::inflate) {
    override fun initAfterBinding() {

        val ageRangeSeekbar = binding.partnerFilterAgeRangeSb
        val ballCapacityRangeSeekBar = binding.partnerFilterBallCapabilityRangeSb

        ageRangeSeekbar.setProgress(20f,60f)
        ballCapacityRangeSeekBar.setProgress(0f, 10f)

        ageRangeSeekbar.setOnRangeChangedListener(object: OnRangeChangedListener {
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {
                if(leftValue.toInt() % 10 == 0){
                    binding.partnerFilterAgeMinTv.text = leftValue.toInt().toString()
                }
                if(rightValue.toInt() % 10 == 0){
                    binding.partnerFilterAgeMaxTv.text = rightValue.toInt().toString()
                }
//                changeSeekBarIndicator(rangeSeekbar.leftSeekBar, leftValue)
//                changeSeekBarIndicator(rangeSeekbar.rightSeekBar, rightValue)
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })

        ballCapacityRangeSeekBar.setOnRangeChangedListener(object: OnRangeChangedListener{
            override fun onRangeChanged(view: RangeSeekBar?, leftValue: Float, rightValue: Float, isFromUser: Boolean) {

                if(leftValue.toInt() % 1 == 0){
                    var leftUnit = "년"
                    if(leftValue < 1f){     // == 0f로 하면 0과 1 사이에서 0개월이 아닌 0년으로 뜨는 구간이 생김
                        leftUnit = "개월"
                    }
                    binding.partnerFilterBallCapabilityMinTv.text = leftValue.toInt().toString() + leftUnit
                }

                if(rightValue.toInt() % 1 == 0){
                    var rightUnit = "년"
                    if(rightValue < 1f){
                        rightUnit = "개월"
                    }
                    binding.partnerFilterBallCapabilityMaxTv.text = rightValue.toInt().toString() + rightUnit
                }
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })

        binding.partnerFilterApplyTv.setOnClickListener ({
            var location = "지역 받아와~"
            var gender = "성별 받아와~"
            var ageMin = binding.partnerFilterAgeMinTv.text
            var ageMax = binding.partnerFilterAgeMaxTv.text
            var ballCapacityMin = binding.partnerFilterBallCapabilityMinTv.text
            var ballCapacityMax = binding.partnerFilterBallCapabilityMaxTv.text



            // 파트너 찾기 - 매칭 화면으로 이동
            //val intent = Intent(this, MakePlanActivity::class.java)
            startActivity(Intent(this, MainActivity::class.java))
        })

        binding.partnerFilterInitiateBtn.setOnClickListener {
            // 초기화 버튼 시 각 항목 입력값 초기화

            ageRangeSeekbar.setProgress(20f,60f)
            ballCapacityRangeSeekBar.setProgress(0f, 10f)
        }

        binding.partnerFilterBackIv.setOnClickListener{
            finish()
        }
    }

// indicator(10의 단위 위에 위치할때마다 위에 표시해줌)
//    private fun changeSeekBarIndicator(seekbar: SeekBar, value: Float){
//        seekbar.showIndicator(true)
//        if (Utils.compareFloat(value, 20f, 4) == 0){
//            seekbar.setIndicatorText("20대")
//        }else if (Utils.compareFloat(value, 30f, 4) == 0){
//            seekbar.setIndicatorText("30대")
//        }else if (Utils.compareFloat(value, 40f, 4) == 0){
//            seekbar.setIndicatorText("40대")
//        }else if (Utils.compareFloat(value, 50f, 4) == 0){
//            seekbar.setIndicatorText("50대")
//        }else if (Utils.compareFloat(value, 60f, 4) == 0){
//            seekbar.setIndicatorText("60대")
//        }else {
//            seekbar.showIndicator(false)
//        }
//    }
}