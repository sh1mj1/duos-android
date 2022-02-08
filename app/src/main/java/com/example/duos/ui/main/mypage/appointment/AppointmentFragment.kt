package com.example.duos.ui.main.mypage.appointment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.appointment.AppointmentListView
import com.example.duos.data.entities.appointment.AppointmentResDto
import com.example.duos.data.remote.appointment.AppointmentResponse
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.databinding.FragmentAppointmentGameBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.temporal.ChronoUnit


class AppointmentFragment : BaseFragment<FragmentAppointmentGameBinding>(FragmentAppointmentGameBinding::inflate), AppointmentListView {
    val TAG = "AppointmentFragment"
    private var previousPlayerData = ArrayList<AppointmentResDto>()
    private var morePreviousPlayerDatas = ArrayList<AppointmentResDto>()

    override fun initAfterBinding() {
        AppointmentService.getAppointmentList(this, 1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetAppointmentSuccess(appointmentResponse: AppointmentResponse) {
        previousPlayerData.clear()
        morePreviousPlayerDatas.clear()

        var i = 0

        // 날짜 비교 반복문
        while (appointmentResponse.result.size > i) {
            var xx = appointmentResponse.result[i].appointmentTime
            if (ChronoUnit.DAYS.between(LocalDateTime.parse(xx, ISO_DATE_TIME), now()) < 35) {
                /* 일주일 이내이면 previousPlayerDatas 에 넣기 - 이제 오늘이거나 어제이면 text 바꿔줘야 해 */
                Log.d(TAG, "7일 이내 ${i} 번째 ${ChronoUnit.DAYS.between(LocalDateTime.parse(xx, ISO_DATE_TIME), now())}?")
                previousPlayerData.add(appointmentResponse.result[i])
            } else { /* 일주일 이상이면 previousPlayerDatas 에 넣기 */
                Log.d(TAG, "7일 이상 ${i} 번째 ${ChronoUnit.DAYS.between(LocalDateTime.parse(xx, ISO_DATE_TIME), now())}?")
                morePreviousPlayerDatas.add(appointmentResponse.result[i])
            }
            i++
        }

        // Set Previous RV Adapter
        val previousGameReviewRVAdapter = initPreviousRecyclerView()
        // Set More Previous RV Adapter
        val morePreviousGameReviewRVAdapter = initMorePreviousRecyclerView()

        // Set Previous RV ClickListener
        previousGameClickListener(previousGameReviewRVAdapter)
        // Set More Previous RV ClickListener
        morePreviousGameClickListener(morePreviousGameReviewRVAdapter)

    }

    override fun onGetAppointmentFailure(code: Int, message: String) {
        Toast.makeText(context, "sdf", Toast.LENGTH_LONG).show()
    }

    // Set Previous RV
    private fun initPreviousRecyclerView(): PreviousGameReviewRVAdapter {
        val previousGameReviewRVAdapter = PreviousGameReviewRVAdapter(previousPlayerData)
        binding.notYetWriteReviewPlayerlistRv.adapter = previousGameReviewRVAdapter
        binding.notYetWriteReviewPlayerlistRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return previousGameReviewRVAdapter
    }

    // Set More Previous RV
    private fun initMorePreviousRecyclerView(): MorePreviousGameReviewRVAdapter {
        val morePreviousGameReviewRVAdapter =
            MorePreviousGameReviewRVAdapter(morePreviousPlayerDatas)
        binding.alreadyWriteReviewPlayerlistRv.adapter = morePreviousGameReviewRVAdapter
        binding.alreadyWriteReviewPlayerlistRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return morePreviousGameReviewRVAdapter
    }

    // Set Previous RV ClickListener
    private fun previousGameClickListener(previousGameReviewRVAdapter: PreviousGameReviewRVAdapter) {
        previousGameReviewRVAdapter.previousReviewItemClickListener(object :
            PreviousGameReviewRVAdapter.PreviousPlayerItemClickListener {
            override fun onProfileClick(appointmentItem: AppointmentResDto) {
                //TODO : 해당 회원의 프로필로 이동
                var intent = Intent(activity, MyProfileActivity::class.java)
                intent.apply {
                    this.putExtra("isFromAppointment", true)
                    this.putExtra("partnerUserIdx", appointmentItem.userIdx)
                }
                startActivity(intent)

            }

            // Click WriteReview
            override fun onWriteBtnClick(appointmentItem: AppointmentResDto) {  /* 후기 작성 클릭!*/
                val fragmentTransaction: FragmentTransaction =
                    (context as AppointmentActivity).supportFragmentManager.beginTransaction().replace(
                        R.id.previous_game_into_fragment_container_fc,
                        AppointmentReviewFragment().apply {
                            arguments = Bundle().apply {
                                val gson = Gson()
                                val profileJson = gson.toJson(appointmentItem)
                                putString("profile", profileJson)
                            }
                        })
                fragmentTransaction.addToBackStack(null)
                fragmentTransaction.commit()
                (context as AppointmentActivity).findViewById<TextView>(R.id.top_previous_game_tv).text = "후기 작성"
            }
        })
    }

    // Set More Previous RV ClickListener
    private fun morePreviousGameClickListener(morePreviousGameReviewRVAdapter: MorePreviousGameReviewRVAdapter) {
        morePreviousGameReviewRVAdapter.morePreviousItemClickListener(object :
            MorePreviousGameReviewRVAdapter.MorePreiousPlayerItemclickListener {
            override fun onProfileCLick(appointmentItem: AppointmentResDto) {

                var intent = Intent(activity, MyProfileActivity::class.java)
                intent.apply {
                    this.putExtra("isFromAppointment", true)
                    this.putExtra("partnerUserIdx", appointmentItem.userIdx)
                }
                startActivity(intent)

            }

        })
    }

}