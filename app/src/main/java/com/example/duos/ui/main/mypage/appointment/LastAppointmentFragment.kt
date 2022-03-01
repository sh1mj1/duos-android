package com.example.duos.ui.main.mypage.appointment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.duos.R
import com.example.duos.data.entities.appointment.AppointmentListView
import com.example.duos.data.entities.appointment.AppointmentResDto
import com.example.duos.data.remote.appointment.GetAppointmentListResponse
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.databinding.FragmentLastAppointmentGameBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.utils.getUserIdx
import com.google.gson.Gson
import java.time.LocalDateTime
import java.time.LocalDateTime.now
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.temporal.ChronoUnit


class LastAppointmentFragment : BaseFragment<FragmentLastAppointmentGameBinding>(FragmentLastAppointmentGameBinding::inflate),
    AppointmentListView {
    val TAG = "AppointmentFragment"
    private var previousPlayerData = ArrayList<AppointmentResDto>()
    private var morePreviousPlayerDatas = ArrayList<AppointmentResDto>()
    val userIdx = getUserIdx()!!  // sharedPreference 에 있는 내 userIdx

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
    }

    override fun initAfterBinding() {
        AppointmentService.getAppointmentList(this, userIdx)

        val fragmentTransaction: FragmentManager = requireActivity().supportFragmentManager
        (context as LastAppointmentActivity).findViewById<ImageView>(R.id.top_left_arrow_iv)
            .setImageResource(R.drawable.ic_my_page_back_btn)
        (context as LastAppointmentActivity).findViewById<TextView>(R.id.top_previous_game_tv).text = "지난 약속"
        (context as LastAppointmentActivity).findViewById<ImageView>(R.id.top_left_arrow_iv).setOnClickListener {
            if (fragmentTransaction.backStackEntryCount >= 1) {   /* 백 스택 있으면 pop */
                fragmentTransaction.popBackStack()
            } else {  /* 없으면 finish() */
                requireActivity().finish()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onGetAppointmentSuccess(appointmentResponse: GetAppointmentListResponse) {
        previousPlayerData.clear()
        morePreviousPlayerDatas.clear()

        var i = 0

        // 날짜 비교 반복문
        while (appointmentResponse.result.size > i) {
            val xx = appointmentResponse.result[i].appointmentTime

            if (ChronoUnit.DAYS.between(LocalDateTime.parse(xx, ISO_DATE_TIME), now()) < 8) {
                /* 일주일 이내이면 previousPlayerDatas 에 넣기 - 이제 오늘이거나 어제이면 text 바꿔줘야 해 */
                Log.d(TAG, "7일 이내 ${i} 번째 ${ChronoUnit.DAYS.between(LocalDateTime.parse(xx, ISO_DATE_TIME), now())} 일 전 약속?")
                previousPlayerData.add(appointmentResponse.result[i])

            } else { /* 일주일 이상이면 previousPlayerDatas 에 넣기 */
                Log.d(TAG, "7일 이상 ${i} 번째 ${ChronoUnit.DAYS.between(LocalDateTime.parse(xx, ISO_DATE_TIME), now())} 일 전 약속")
                morePreviousPlayerDatas.add(appointmentResponse.result[i])
            }
            i++
        }
        startPostponedEnterTransition()
        // 어댑터 설정
        val previousGameReviewRVAdapter = initPreviousRecyclerView()
        val morePreviousGameReviewRVAdapter = initMorePreviousRecyclerView()
        previousGameClickListener(previousGameReviewRVAdapter)
        morePreviousGameClickListener(morePreviousGameReviewRVAdapter)

    }

    override fun onGetAppointmentFailure(code: Int, message: String) {
        startPostponedEnterTransition()
        Toast.makeText(context, "네트워크 상태 확인 후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
    }

    private fun initPreviousRecyclerView(): PreviousGameReviewRVAdapter {
        val previousGameReviewRVAdapter = PreviousGameReviewRVAdapter(previousPlayerData)
        binding.notYetWriteReviewPlayerlistRv.adapter = previousGameReviewRVAdapter
        binding.notYetWriteReviewPlayerlistRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return previousGameReviewRVAdapter
    }

    private fun initMorePreviousRecyclerView(): MorePreviousGameReviewRVAdapter {
        val morePreviousGameReviewRVAdapter =
            MorePreviousGameReviewRVAdapter(morePreviousPlayerDatas)
        binding.alreadyWriteReviewPlayerlistRv.adapter = morePreviousGameReviewRVAdapter
        binding.alreadyWriteReviewPlayerlistRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return morePreviousGameReviewRVAdapter
    }

    private fun previousGameClickListener(previousGameReviewRVAdapter: PreviousGameReviewRVAdapter) {
        previousGameReviewRVAdapter.previousReviewItemClickListener(object :
            PreviousGameReviewRVAdapter.PreviousPlayerItemClickListener {
            override fun onProfileClick(appointmentItem: AppointmentResDto) {
                val intent = Intent(activity, MyProfileActivity::class.java)
                intent.apply {
                    this.putExtra("isFromAppointment", true)
                    this.putExtra("partnerUserIdx", appointmentItem.userIdx)
                }
                startActivity(intent)

            }

            override fun onWriteBtnClick(appointmentItem: AppointmentResDto) {  /* 후기 작성 클릭!*/
                val fragmentTransaction: FragmentTransaction =
                    (context as LastAppointmentActivity).supportFragmentManager.beginTransaction()
                fragmentTransaction.apply {
                    setReorderingAllowed(true)
                    setCustomAnimations(R.anim.enter_from_bottom_anim, R.anim.fade_out, R.anim.fade_in, R.anim.exit_to_bottom)
                    replace(
                        R.id.previous_game_into_fragment_container_fc,
                        LastAppointmentReviewFragment().apply {
                            arguments = Bundle().apply {
                                val gson = Gson()
                                val profileJson = gson.toJson(appointmentItem)
                                putString("profile", profileJson)
                            }
                        })
                    addToBackStack(null)
                    commit()
                }
                (context as LastAppointmentActivity).findViewById<TextView>(R.id.top_previous_game_tv).text = "후기 작성"
            }
        })
    }

    private fun morePreviousGameClickListener(morePreviousGameReviewRVAdapter: MorePreviousGameReviewRVAdapter) {
        morePreviousGameReviewRVAdapter.morePreviousItemClickListener(object :
            MorePreviousGameReviewRVAdapter.MorePreiousPlayerItemclickListener {
            override fun onProfileCLick(appointmentItem: AppointmentResDto) {

                val intent = Intent(activity, MyProfileActivity::class.java)
                intent.apply {
                    this.putExtra("isFromAppointment", true)
                    this.putExtra("partnerUserIdx", appointmentItem.userIdx)
                }
                startActivity(intent)

            }

        })
    }


}