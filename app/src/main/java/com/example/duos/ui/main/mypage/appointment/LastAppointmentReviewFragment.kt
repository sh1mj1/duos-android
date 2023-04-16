package com.example.duos.ui.main.mypage.appointment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.KeyEvent
import android.view.KeyEvent.KEYCODE_ALL_APPS
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.entities.appointment.AppointmentResDto
import com.example.duos.data.entities.review.PostReviewResDto
import com.example.duos.data.entities.review.ReviewListView
import com.example.duos.data.remote.reviews.ReviewResponse
import com.example.duos.data.remote.reviews.ReviewService
import com.example.duos.databinding.FragmentLastAppointmentReviewBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.ui.main.mypage.myprofile.frag.EveryReviewFragment
import com.example.duos.utils.getUserIdx
import com.google.gson.Gson
import java.time.LocalDateTime.now

class LastAppointmentReviewFragment : BaseFragment<FragmentLastAppointmentReviewBinding>
    (FragmentLastAppointmentReviewBinding::inflate), ReviewListView {
    val TAG: String = "AppointmentReviewFragment"
    lateinit var mContext: LastAppointmentActivity
    private var gson: Gson = Gson()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LastAppointmentActivity) {
            mContext = context
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initAfterBinding() {
        (context as LastAppointmentActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setImageResource(R.drawable.ic_btn_close_iv    )

        val profileData = arguments?.getString("profile")
        val profile = gson.fromJson(profileData, AppointmentResDto::class.java)

        // 상단 회원 프로필 설정
        setInitProfile(profile)

        // Rating 과 EditText 에 입력되는 내용을 입력이 되면 작성 완료 활성화
        val editText = binding.contentReviewEt
        initEditText(editText)

        // Frag BackStack 유무
        val fragmentTransaction: FragmentManager = requireActivity().supportFragmentManager
        (context as LastAppointmentActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            if (fragmentTransaction.backStackEntryCount >= 1) {   /* 백 스택 있으면 pop */
                fragmentTransaction.popBackStack()
            } else {
                requireActivity().finish()
            }
        }

        // 작성 완료 클릭 리스너
        binding.activatingCompleteBtn.setOnClickListener {
            val writerIdx = getUserIdx()
            val revieweeIdx = profile.userIdx!!.toInt()
            val rating = binding.reviewRatingRb.rating
            val reviewContent = binding.contentReviewEt.text.toString()
            val createdAt = now().toString()
            val appointmentIdx = profile.appointmentIdx!!

            ReviewService.postReview(this, writerIdx, revieweeIdx, rating, reviewContent, createdAt, appointmentIdx, writerIdx)
        }

        // 상단 뒤로 가기 버튼 클릭
        (context as LastAppointmentActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
                (context as LastAppointmentActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.previous_game_into_fragment_container_fc, LastAppointmentFragment()).commitAllowingStateLoss()
            (context as LastAppointmentActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setImageResource(R.drawable.ic_back_30)
        }
    }

    override fun onPostReviewSuccess(reviewResponse: ReviewResponse) {
        Log.d(TAG, "onPostReviewSuccess")
        (context as LastAppointmentActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.previous_game_into_fragment_container_fc, LastAppointmentFragment())
            .commitAllowingStateLoss()
        showToast("리뷰 작성 완료")
    }

    override fun onPostReviewFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요")
    }

    private fun initEditText(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    when {
                        s.toString().length in 1..300 -> {
                            // EditText 에 글이 1 ~ 299 자면 파란 작성완료 버튼
                            binding.activatingCompleteBtn.isEnabled = true
                            binding.activatingCompleteBtn.setBackgroundResource(R.drawable.next_btn_done_rectangular)
                            binding.activatingCompleteBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                            binding.countTextLengthTv.text = ""
                            binding.contentReviewEt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                        }
                        s.toString().length >= 301 -> {
                            // EditText 에 글이 400 자보다 길면
                            binding.activatingCompleteBtn.isEnabled = false
                            binding.activatingCompleteBtn.setBackgroundResource(R.drawable.next_btn_inactivitate_rectangular)
                            binding.activatingCompleteBtn.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_B0))
                            binding.countTextLengthTv.text = "리뷰는 최대 300자까지 입력 가능합니다."
                            showToast("리뷰는 최대 300자까지 입력 가능합니다.")

                        }
                        else -> {
                            // EditText 에 글이 없으면 회색 작성완료 버튼
                            binding.activatingCompleteBtn.isEnabled = false
                            binding.activatingCompleteBtn.setBackgroundResource(R.drawable.next_btn_inactivitate_rectangular)
                            binding.activatingCompleteBtn.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_B0))
                            binding.countTextLengthTv.text = "내용을 입력해주세요."

                        }
                    }
                }
            }

        })

    }

    // 상단 회원 프로필 설정
    private fun setInitProfile(profile: AppointmentResDto) {
        Glide.with(binding.imgWriteReviewPlayerIv.context)
            .load(profile.profilePhotoUrl)
            .into(binding.imgWriteReviewPlayerIv)
        binding.writeReviewPlayerNicknameTv.text = profile.nickname
        startPostponedEnterTransition()
    }


}
