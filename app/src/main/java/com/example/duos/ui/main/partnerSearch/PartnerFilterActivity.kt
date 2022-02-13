package com.example.duos.ui.main.partnerSearch

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.duos.R
import com.example.duos.ToggleButtonInterface
import com.example.duos.data.entities.RecommendedPartner
import com.example.duos.data.local.RecommendedPartnerDatabase
import com.example.duos.data.remote.partnerProfile.PartnerFilterService
import com.example.duos.data.remote.partnerSearch.PartnerSearchService
import com.example.duos.databinding.ActivityPartnerFilterBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.MainActivity
import com.example.duos.ui.main.friendList.FriendListDialogFragment
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
    val myUserIdx = getUserIdx()!!
    val TAG = "PartnerFilterActivity"
    private var recommendedPartnerDatas = ArrayList<RecommendedPartner>()

    @SuppressLint("SetTextI18n")
    override fun initAfterBinding() {

        val ageRangeSeekbar = binding.partnerFilterAgeRangeSb
        val ballCapacityRangeSeekBar = binding.partnerFilterBallCapabilityRangeSb
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(ViewModel::class.java)


        ageRangeSeekbar.setProgress(10f, 60f)
        ballCapacityRangeSeekBar.setProgress(0f, 10f)

        // 연령 선택
        ageRangeSeekbar.setOnRangeChangedListener(object : OnRangeChangedListener {
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

        // 구력 선택
        ballCapacityRangeSeekBar.setOnRangeChangedListener(object : OnRangeChangedListener {
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

        // 적용하기 버튼 클릭 시
        binding.partnerFilterApplyTv.setOnClickListener {
//            val gender: Int = viewModel.partnerGender.value!!
//            val ageMin: Int = binding.partnerFilterAgeMinTv.text.toString().toInt()
//            val ageMax: Int = binding.partnerFilterAgeMaxTv.text.toString().toInt()
//            val ballCapacityMin: Int =
//                binding.partnerFilterBallCapabilityMinTv.text.toString().toInt()
//            val ballCapacityMax: Int =
//                binding.partnerFilterBallCapabilityMaxTv.text.toString().toInt()
//            val location: Int = viewModel.partnerLocation.value!!
            Log.d(TAG, "적용하기 버튼 클릭")
            PartnerSearchService.partnerSearchFilterCount(this, myUserIdx)
            Log.d(TAG, "파트너 필터 적용 가능 횟수 부르는 API 호출")
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
        }

        binding.partnerFilterInitiateBtn.setOnClickListener {
            // 초기화 버튼 시 각 항목 입력값 초기화
            initiateAllFilter(ageRangeSeekbar, ballCapacityRangeSeekBar)
        }

        binding.partnerFilterBackIv.setOnClickListener {
            finish()
        }


        binding.partnerFilterLocationSelectLayout.setOnClickListener {  /* 지역 선택 다이얼로그 띄우기 */
            val dialog = LocationDialogFragment()
            supportFragmentManager.let { fragmentManager -> dialog.show(fragmentManager, "지역 선택") }

        }

        /* 지역 선택 한 것을 관측하면서 해당 뷰에 선택한 요소 띄우기 */
        viewModel.partnerLocationDialogShowing.observe(this, Observer {
            if (it) {
                binding.partnerFilterLocationTextTv.text =
                    viewModel.partnerLocationCateName.value + " " + viewModel.partnerLocationName.value

            }
        })

    }

    private fun initiateAllFilter(ageRangeSeekbar: RangeSeekBar, ballCapacityRangeSeekBar: RangeSeekBar) {
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

    private fun setDialog(count: Int) { /* 필터 팝업 설정*/
        PartnerFilterDialog.Builder(this)// 만약 액티비티에서 사용한다면 requireContext() 가 아닌 context를 사용하면 됨.
            .setCount(count.toString()) // Dialog 텍스트 설정하기
            .setRightButton(object : PartnerFilterDialog.PartnerFilterDialogCallbackRight {
                override fun onClick(dialog: PartnerFilterDialog) {
                    /* 적용하기 선택 -> 다이얼로그 dismiss-> viewModel의 값 가져오기 -> API 호출 ->*/
                    Log.d(TAG, "count 이게 얼마나 필터 추천 받을 수 있는지 남은 수 : $count")
                    val gender: Int = viewModel.partnerGender.value!!
                    val ageMin: Int = binding.partnerFilterAgeMinTv.text.toString().toInt()
                    val ageMax: Int = binding.partnerFilterAgeMaxTv.text.toString().toInt()

                    val ballCapacityMinStr = binding.partnerFilterBallCapabilityMinTv.text.toString()
                    val ballCapacityMin : Int = strToInt(ballCapacityMinStr)
//                    val ballCapacityMin: Int = binding.partnerFilterBallCapabilityMinTv.text.toString().toInt()
                    val ballCapacityMaxStr = binding.partnerFilterBallCapabilityMaxTv.text.toString()
                    val ballCapacityMax: Int = strToInt(ballCapacityMaxStr)
//                        binding.partnerFilterBallCapabilityMaxTv.text.toString().toInt()
                    val location: Int = viewModel.partnerLocation.value!!
                    PartnerFilterService.showPartnersByFilter(
                        this,
                        myUserIdx,
                        gender,
                        ageMin,
                        ageMax,
                        ballCapacityMin,
                        ballCapacityMax,
                        location
                    )
                    Log.d(TAG, "검색한 기준 : $myUserIdx, $gender, $ageMin, $ageMax, $ballCapacityMin, $ballCapacityMax, $location")
                    dialog.dismiss()

                }

                override fun onGetPartnerFilterSuccess(recommendedPartner: List<RecommendedPartner>) {
                    /////////////////////////////// 희주님이 잘라내기 해서 옮기라고 하신 부분//////////////////////////////////////////////////////////////////////////////////////////////////
                    // 필터 적용 한 적이 한 번이라도 있는지 체크하고, 없으면 sharedPreference 변수인 checkUserAppliedPartnerFilterMoreThanOnce = true로 바꾸기
                    if (!getCheckUserAppliedPartnerFilterMoreThanOnce()) {
                        Log.d("PartnerFilterActivity", "이 사용자는 가입 후 파트너 추천 필터 기능을 사용한 적이 없음")
                        saveCheckUserAppliedPartnerFilterMoreThanOnce(true)
                    } else {
                        Log.d("PartnerFilterActivity", "이 사용자는 가입 후 파트너 추천 필터 기능을 적어도 한 번 이상 사용함")

                    }
/////////////////////////////// 희주님이 잘라내기 해서 옮기라고 하신 부분//////////////////////////////////////////////////////////////////////////////////////////////////
                    /* 이제 여기서 RoomDB에 데이터를 저장해 주어야 해*/

                    val recommendedPartnerDB =
                        RecommendedPartnerDatabase.getInstance(applicationContext)!! //룸 초기화 -> applicationContext
                    recommendedPartnerDB.recommendedPartnerDao().deleteAll()    // 파트너 추천하는 DB 삭제

                    for (i: Int in 0..recommendedPartner.size - 1) {
                        recommendedPartnerDB.recommendedPartnerDao().insert(recommendedPartner[i])   // insert 파라미터로 api로받은 리스트[i]
                    }
                    Log.d(
                        "PartnerFilterActivity:",
                        "필터적용 후 파트너 추천 api로 받은 리스트 룸DB에 잘 저장되었는지 확인" + recommendedPartnerDB.recommendedPartnerDao()
                            .getRecommendedPartnerList()
                    )

                    val intent = Intent(this@PartnerFilterActivity, MainActivity::class.java)
                    startActivity(intent)
                }

                override fun onGetPartnerFilterFailure(code: Int, message: String) {
                    Toast.makeText(applicationContext, message, Toast.LENGTH_LONG).show()
                }
            })
            .setLeftButton(object : PartnerFilterDialog.PartnerFilterDialogCallbackLeft {
                override fun onClick(dialog: PartnerFilterDialog) {

                    dialog.dismiss()
                    Log.d(TAG,"취소 버튼을 누름")
                }
            })
            .show()
    }

    override fun setRadiobutton(tag: String) {
        viewModel.partnerGender.value = tag.toInt()
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setApplyBtnEnable() {
        binding.partnerFilterApplyTv.isEnabled = true
        binding.partnerFilterApplyTv.background = getDrawable(R.color.primary)
        binding.partnerFilterApplyTv.setTextColor(getColor(R.color.white))
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    fun setApplyBtnUnable() {   /* 필터 적용하기 버튼 비활성화 */
        binding.partnerFilterApplyTv.isEnabled = false
        binding.partnerFilterApplyTv.background = getDrawable(R.color.white_smoke_E)
        binding.partnerFilterApplyTv.setTextColor(getColor(R.color.dark_gray_B0))
    }



    override fun onPartnerSearchFilterCountSuccess(searchCount: Int) {
        if (searchCount > 0) {  /* 파트너 추천할 수 있는 횟수가 있으면 다이얼로그 띄우기*/
            Log.d("필터남은횟수", searchCount.toString())
            if (searchCount > 0) {
                setDialog(searchCount)
            } else {
                PartnerFilterUnableDialogFragment().show(
                    supportFragmentManager, "파트너필터적용못함"
                )
            }

        }
    }

    override fun onPartnerSearchFilterCountFailure(code: Int, message: String) {
        showToast(code.toString() + " : " + message)
    }

//    override fun onGetPartnerFilterSuccess(recommendedPartner: List<RecommendedPartner>) {
//        /* 파트너 추천 필터 적용 성공*/
//    }
//
//    override fun onGetPartnerFilterFailure(code: Int, message: String) {
//        TODO("Not yet implemented")
//    }


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