package com.example.duos.ui.main.mypage.appointment

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
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

    //    val postReqData = Array<ReviewsReqDto>()
    val responseReviewData = ArrayList<PostReviewResDto>()

//    var partnerUserIdx: Int = 0            // Appointment Frag 에서 나와 약속을 가진 partner의 Idx

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is LastAppointmentActivity) {
            mContext = context
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initAfterBinding() {
        Log.d(TAG, "Start_AppointmentReviewFragment")
//        partnerUserIdx = requireArguments().getInt("partnerUserIdx")  /* From MyProfile OR PlayerProfile? thisIdx*/

        /* TODO : 해당 화면에 회원 프로필 사진, 회원 nickname, binding 하기 */

        val profileData =
            arguments?.getString("profile")   /* From player's userIdx From AppointFrag */
        val profile = gson.fromJson(profileData, AppointmentResDto::class.java)

        // 이전 AppointmentFragment 에서 넘어온 프로필 이미지 회원 nickname binding
        setInitProfile(profile)

        /* TODO Rating과 EditText에 입력되는 내용을 입력이 되면 작성 완료를 할 수 있도록 만들기 */
        val editText = binding.contentReviewEt
        initEditText(editText)


        /* TODO : Rating과 EditText 에 입력되는 내용이 ReviewReqDto 에 들어가도록 만들기 */
        binding.activatingCompleteBtn.setOnClickListener {

            val writerIdx = getUserIdx()!!
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
            (context as LastAppointmentActivity).findViewById<TextView>(R.id.top_previous_game_tv).text = "지난 약속"
        }
    }

    override fun onPostReviewSuccess(reviewResponse: ReviewResponse) {
        Log.d(TAG, "onPostReviewSuccess")

        (context as LastAppointmentActivity).supportFragmentManager.beginTransaction()
            .replace(R.id.previous_game_into_fragment_container_fc, LastAppointmentFragment())
            .commitAllowingStateLoss()
        Toast.makeText(context, "리뷰 작성 완료", Toast.LENGTH_LONG).show()
        // 리뷰 작성했음을 Bundle로 넘겨줘야 하는가...?

    }

    override fun onPostReviewFailure(code: Int, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
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
                    if (s.toString().length > 0 && s.toString().length < 300) { /* EditText 에 글이 1 ~ 400 자면 파란 작성완료 버튼*/
                        binding.activatingCompleteBtn.isEnabled = true
                        binding.activatingCompleteBtn.setBackgroundResource(R.drawable.next_btn_done_rectangular)
                        binding.activatingCompleteBtn.setTextColor(ContextCompat.getColor(mContext, R.color.white))
                        binding.countTextLengthTv.text = ""
                        binding.contentReviewEt.setTextColor(ContextCompat.getColor(mContext, R.color.black))
                    } else if (s.toString().length >= 300) {    /* EditText 에 글이 400 자보다 길면 */

                        binding.activatingCompleteBtn.isEnabled = false
                        binding.activatingCompleteBtn.setBackgroundResource(R.drawable.next_btn_inactivitate_rectangular)
                        binding.activatingCompleteBtn.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_B0))
                        binding.countTextLengthTv.text = "후기는 400자 이하로만 입력할 수 있습니다."
                        Toast.makeText(context, "후기는 400자 이하로만 입력할 수 있습니다.", Toast.LENGTH_LONG)
                            .show()

                    } else { /* EditText 에 글이 없으면 회색 작성완료 버튼*/
                        binding.activatingCompleteBtn.isEnabled = false
                        binding.activatingCompleteBtn.setBackgroundResource(R.drawable.next_btn_inactivitate_rectangular)
                        binding.activatingCompleteBtn.setTextColor(ContextCompat.getColor(mContext, R.color.dark_gray_B0))
                        binding.countTextLengthTv.text = "내용을 입력해주세요."

//                        }
                    }
                }
            }

        })
    }

    private fun setInitProfile(profile: AppointmentResDto) {
        Glide.with(binding.imgWriteReviewPlayerIv.context)
            .load(profile.profilePhotoUrl)
            .into(binding.imgWriteReviewPlayerIv)
        binding.writeReviewPlayerNicknameTv.text = profile.nickname
    }


}
