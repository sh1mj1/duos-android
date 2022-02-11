package com.example.duos.ui.main.partnerSearch

import android.content.Intent
import android.util.Log
import android.view.View
import com.example.duos.data.local.RecommendedPartnerDatabase
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.duos.CustomDialog
import com.example.duos.R
import com.example.duos.ToggleButtonInterface
import com.example.duos.data.remote.partnerSearch.PartnerSearchService
import com.example.duos.databinding.ActivityPartnerFilterBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.MainActivity
import com.example.duos.utils.getCheckUserAppliedPartnerFilterMoreThanOnce
import com.example.duos.utils.saveCheckUserAppliedPartnerFilterMoreThanOnce
import com.example.duos.ui.signup.localSearch.LocationDialogFragment
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getUserIdx
import com.jaygoo.widget.OnRangeChangedListener
import com.jaygoo.widget.RangeSeekBar

class PartnerFilterActivity :
    BaseActivity<ActivityPartnerFilterBinding>(ActivityPartnerFilterBinding::inflate),
    ToggleButtonInterface, PartnerSearchFilterCountView {

    lateinit var viewModel: ViewModel
    override fun initAfterBinding() {

        val ageRangeSeekbar = binding.partnerFilterAgeRangeSb
        val ballCapacityRangeSeekBar = binding.partnerFilterBallCapabilityRangeSb
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(ViewModel::class.java)


        ageRangeSeekbar.setProgress(10f, 60f)
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

                if (rightValue.toInt() % 1 == 0) {
                    var rightUnit = "년"
                    if (rightValue < 1f) {
                        rightUnit = "개월"
                    }
                    binding.partnerFilterBallCapabilityMaxTv.text =
                        rightValue.toInt().toString() + rightUnit
                }
            }

            override fun onStartTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }

            override fun onStopTrackingTouch(view: RangeSeekBar?, isLeft: Boolean) {

            }
        })

        binding.partnerFilterApplyTv.setOnClickListener ({
            //            var location: Int = viewModel.partnerLocation.value!!
//            var gender: Int = viewModel.partnerGender.value!!
//            var ageMin: Int = binding.partnerFilterAgeMinTv.text.toString().toInt()
//            var ageMax: Int = binding.partnerFilterAgeMaxTv.text.toString().toInt()
//            var ballCapacityMin: Int =
//                binding.partnerFilterBallCapabilityMinTv.text.toString().toInt()
//            var ballCapacityMax: Int =
//                binding.partnerFilterBallCapabilityMaxTv.text.toString().toInt()

            // 필터 적용 한 적이 한 번이라도 있는지 체크하고, 없으면 sharedPreference 변수인 checkUserAppliedPartnerFilterMoreThanOnce = true로 바꾸기
            if(!getCheckUserAppliedPartnerFilterMoreThanOnce()){
                Log.d("PartnerFilterActivity", "이 사용자는 가입 후 파트너 추천 필터 기능을 사용한 적이 없음")
                saveCheckUserAppliedPartnerFilterMoreThanOnce(true)
            }else{
                Log.d("PartnerFilterActivity", "이 사용자는 가입 후 파트너 추천 필터 기능을 적어도 한 번 이상 사용함")
            }

            // 필터 적용 파트너 추천 api 호출 - 해야돼...


            //수

            //정

            //필

            //요

            // api 데이터를 룸디비에 저장 - 수정 필요

//            val recommendedPartnerDB = RecommendedPartnerDatabase.getInstance(this)!!
            //수
//            recommendedPartnerDB.recommendedPartnerDao().deleteAll()
            //정
//            for(i: Int in 0..api로받은리스트크기-1)
            //필
//            recommendedPartnerDB.recommendedPartnerDao().insert()   // insert 파라미터로 api로받은 리스트[i]
            //요
//            Log.d("PartnerFilterActivity:", "필터적용 후 파트너 추천 api로 받은 리스트 룸DB에 잘 저장되었는지 확인"+recommendedPartnerDB.recommendedPartnerDao().getRecommendedPartnerList())


            PartnerSearchService.partnerSearchFilterCount(this, getUserIdx()!!)



        })

        binding.partnerFilterInitiateBtn.setOnClickListener {
            // 초기화 버튼 시 각 항목 입력값 초기화
            binding.partnerFilterManBtn.isChecked = false
            binding.partnerFilterWomanBtn.isChecked = false
            binding.partnerFilterCarelessBtn.isChecked = false
            binding.partnerFilterLocationTextTv.text = getString(R.string.signup_local_set)
            viewModel.partnerGender.value = null
            viewModel.partnerLocation.value = null
            viewModel.partnerGender.value = null
            ageRangeSeekbar.setProgress(10f, 60f)
            ballCapacityRangeSeekBar.setProgress(0f, 10f)
        }

        binding.partnerFilterBackIv.setOnClickListener {
            finish()
        }

        binding.partnerFilterLocationSelectLayout.setOnClickListener {
            val dialog = LocationDialogFragment()
            supportFragmentManager?.let { fragmentManager ->
                dialog.show(
                    fragmentManager,
                    "지역 선택"
                )
            }
        }

        viewModel.partnerLocationDialogShowing.observe(this, Observer {
            if (it) {
                binding.partnerFilterLocationTextTv.text =
                    viewModel.partnerLocationCateName.value + " " +
                            viewModel.partnerLocationName.value
            }
        })

    }

    private fun setDialog(count : Int) {

       PartnerFilterDialog.Builder(this)// 만약 액티비티에서 사용한다면 requireContext() 가 아닌 context를 사용하면 됨.
            .setCount(count.toString()) // Dialog 텍스트 설정하기
            .setRightButton(object : PartnerFilterDialog.PartnerFilterDialogCallback{
                override fun onClick(dialog: PartnerFilterDialog) {
                    // 그냥 취소하기
                    dialog.dismiss()
                }
            })
            .setLeftButton(object : PartnerFilterDialog.PartnerFilterDialogCallback{
                override fun onClick(dialog: PartnerFilterDialog) {
                    // 필터 적용하고 main 으로 가기
                    // 파트너 찾기 - 매칭 화면으로 이동
//                    startActivity(Intent(this, MainActivity::class.java))
                    dialog.dismiss()
                }
            })
            .show()
    }

    override fun setRadiobutton(tag: String) {
        viewModel.partnerGender.value = tag.toInt()
    }

    fun setApplyBtnEnable(){
        binding.partnerFilterApplyTv.isEnabled = true
        binding.partnerFilterApplyTv.background = getDrawable(R.color.primary)
        binding.partnerFilterApplyTv.setTextColor(getColor(R.color.white))
    }

    fun setApplyBtnUnable(){
        binding.partnerFilterApplyTv.isEnabled = false
        binding.partnerFilterApplyTv.background = getDrawable(R.color.white_smoke_E)
        binding.partnerFilterApplyTv.setTextColor(getColor(R.color.dark_gray_B0))
    }



    override fun onPartnerSearchFilterCountSuccess(searchCount: Int) {
        if (searchCount > 0){
            setDialog(searchCount)
        }

    }

    override fun onPartnerSearchFilterCountFailure(code: Int, message: String) {
        showToast(code.toString() + " : " + message)
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