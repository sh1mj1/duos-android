package com.example.duos.ui.main.mypage.appointment

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.example.duos.data.entities.ReviewResDto
import com.example.duos.data.entities.appointment.AppointmentResDto
import com.example.duos.data.entities.review.PostReviewResDto
import com.example.duos.data.entities.review.ReviewListView
import com.example.duos.data.entities.review.ReviewsReqDto
import com.example.duos.data.remote.reviews.ReviewResponse
import com.example.duos.data.remote.reviews.ReviewService
import com.example.duos.databinding.FragmentAppointmentReviewBinding
import com.example.duos.ui.BaseFragment
import com.google.gson.Gson
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class AppointmentReviewFragment : BaseFragment<FragmentAppointmentReviewBinding>
    (FragmentAppointmentReviewBinding::inflate), ReviewListView {
    val TAG: String = "AppointmentReviewFragment"
    private var gson: Gson = Gson()

    //    val postReqData = Array<ReviewsReqDto>()
    val responseReviewData = ArrayList<PostReviewResDto>()

    var thisIdx: Int = 0            // Appointment Frag 에서 나와 약속을 가진 partner의 Idx

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initAfterBinding() {
        Log.d(TAG, "Start_AppointmentReviewFragment")


        /* TODO : 해당 화면에 회원 프로필 사진, 회원 nickname, binding 하기 */

        val profileData = arguments?.getString("profile")   /* From player's userIdx From AppointFrag */
        val profile = gson.fromJson(profileData, AppointmentResDto::class.java)

        // 이전 AppointmentFragment 에서 넘어온 프로필 이미지 회원 nickname binding
        setInitProfile(profile)

        /* TODO Rating과 EditText에 입력되는 내용을 입력이 되면 작성 완료를 할 수 있도록 만들기 */
        val editText = binding.contentReviewEt
        initEditText(editText)

        /* TODO : Rating과 EditText 에 입력되는 내용이 ReviewReqDto 에 들어가도록 만들기 */
        binding.activatingCompleteBtn.setOnClickListener {
            val postJson = JSONObject()
            // postJson 이라는 Json 객체에 서버로 post할 정보 담기 (작성완료 버튼 클릭 시)
            postJson.put("writerIdx", 1)        // TODO : 1에는 RoomDB에 있는 사용자의 Idx로
            postJson.put("revieweeIdx", profile.userIdx)
            postJson.put("rating", binding.reviewRatingRb.rating)
            postJson.put("reviewContent", binding.contentReviewEt.text)
            postJson.put("createdAt", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
            postJson.put("appointmentIdx", profile.appointmentIdx)


            ReviewService.postReview(this, postJson, 1)

        }

    }

    override fun onPostReviewSuccess(reviewResponse: ReviewResponse) {
        Log.d(TAG, "onPostReviewSuccess")
    }

    override fun onPostReviewFailure(code: Int, message: String) {
        Toast.makeText(context, "onPostReviewFailure", Toast.LENGTH_LONG).show()
    }

    private fun initEditText(editText: EditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.length > 0 && s.length <= 400) { /* EditText 에 글이 1 ~ 400 자면 파란 작성완료 버튼*/
                        binding.activatingCompleteBtn.visibility = View.VISIBLE
                        binding.inactivatingCompleteBtn.visibility = View.GONE
                    } else if (s.length > 400) {    /* EditText 에 글이 400 자보다 길면 */
                        binding.activatingCompleteBtn.visibility = View.VISIBLE
                        binding.inactivatingCompleteBtn.visibility = View.GONE
                        Toast.makeText(context, "후기는 400자 이하로만 입력할 수 있습니다.", Toast.LENGTH_LONG).show()

                    } else { /* EditText 에 글이 없으면 회색 작성완료 버튼*/
                        binding.activatingCompleteBtn.visibility = View.GONE
                        binding.inactivatingCompleteBtn.visibility = View.VISIBLE
                        binding.inactivatingCompleteBtn.setOnClickListener {
                            Toast.makeText(context, "후기를 입력해주세요", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
            //            override fun afterTextChanged(s: Editable?) {
            //                if (s != null) {
            //                    if (s.length > 0 ){ /* EditText 에 글이 있으면 파란 작성완료 버튼*/
            //                        binding.activatingCompleteBtn.visibility = View.VISIBLE
            //                        binding.inactivatingCompleteBtn.visibility = View.GONE
            //
            //                    }else{ /* EditText 에 글이 없으면 회색 작성완료 버튼*/
            //                        binding.activatingCompleteBtn.visibility = View.GONE
            //                        binding.inactivatingCompleteBtn.visibility = View.VISIBLE
            //                    }
            //                }
            //            }
        })
    }

    private fun setInitProfile(profile: AppointmentResDto) {
        Glide.with(binding.imgWriteReviewPlayerIv.context)
            .load(profile.profilePhotoUrl)
            .into(binding.imgWriteReviewPlayerIv)
        binding.writeReviewPlayerNicknameTv.text = profile.nickname
    }


}
