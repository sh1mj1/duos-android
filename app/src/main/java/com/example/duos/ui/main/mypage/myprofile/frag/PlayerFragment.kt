package com.example.duos.ui.main.mypage.myprofile.frag

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.duos.R
import com.example.duos.data.entities.PartnerResDto
import com.example.duos.data.entities.ReviewResDto
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.data.remote.chat.chat.CreateChatRoomResultData
import com.example.duos.data.remote.myFriendList.FriendListService
import com.example.duos.data.remote.partnerProfile.PartnerProfileService
import com.example.duos.databinding.FragmentPlayerBinding
import com.example.duos.ui.BaseFragment
import com.example.duos.ui.main.chat.ChattingActivity
import com.example.duos.ui.main.chat.CreateChatRoomView
import com.example.duos.ui.main.friendList.AddStarredFriendView
import com.example.duos.ui.main.friendList.DeleteStarredFriendView
import com.example.duos.ui.main.mypage.myprofile.MyProfileActivity
import com.example.duos.data.entities.profile.PartnerProfileListView
import com.example.duos.ui.adapter.PartnerProfileReviewRVAdapter
import com.example.duos.utils.getUserIdx
import com.google.gson.Gson
import androidx.fragment.app.FragmentManager
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.ui.main.appointment.AppointmentExistView

class PlayerFragment : BaseFragment<FragmentPlayerBinding>(FragmentPlayerBinding::inflate),
    PartnerProfileListView, AddStarredFriendView, DeleteStarredFriendView, CreateChatRoomView,
    AppointmentExistView {
    val TAG: String = "PlayerFragment"
    lateinit var chatDB: ChatDatabase

    private var partnerProfileReviewDatas = ArrayList<ReviewResDto>()

    val myUserIdx = getUserIdx()
    private var isStarred: Boolean = false
    private var partnerUserIdx: Int = 0
    private var todayChatAvaillCnt: Int = 0
    private var hasChatRoomAlready: Boolean = false

    private var createdNewChatRoom: Boolean = false
    private var targetChatRoomIdx: String = ""
    private var participantList: List<Int> = emptyList<Int>()

    private var partnerImgUrl: String = ""
    private var partnerNickname: String = ""

    private var appointmentIsExisted: Boolean = false
    private var appointmentIdx: Int = -1


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
    }

    override fun initAfterBinding() {
        partnerUserIdx = requireArguments().getInt("partnerUserIdx")

        PartnerProfileService.partnerProfileInfo(this, myUserIdx, partnerUserIdx)

        (context as MyProfileActivity).apply {
            findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility =
                View.VISIBLE
            findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
            findViewById<TextView>(R.id.top_myProfile_tv).text = "프로필"
            findViewById<ImageView>(R.id.player_is_starred_iv)
                .setOnClickListener { deleteStarredFriend()   /* 친구 찜 취소 */ }
            findViewById<ImageView>(R.id.player_is_not_starred_iv)
                .setOnClickListener { addStarredFriend()  /* 친구 찜하기 */ }
        }

        clickListenerChat()         // 채팅하기 버튼 클릭 리스너

        // 상단 뒤로 가기 버튼 클릭 리스너 (Frag 백스택 유뮤)
        val fragmentTransaction: FragmentManager = requireActivity().supportFragmentManager
        (context as MyProfileActivity).findViewById<ImageView>(R.id.top_left_arrow_iv)
            .setOnClickListener {
                if (fragmentTransaction.backStackEntryCount >= 1) {   /* 백 스택 있으면 pop */
                    fragmentTransaction.popBackStack()
                } else {
                    requireActivity().finish()
                }
            }

    }

    // 채팅하기 버튼 클릭
    private fun clickListenerChat() {
        (context as MyProfileActivity).findViewById<AppCompatButton>(R.id.partner_profile_chatting_btn)
            .setOnClickListener {

                // 만약 이미 채팅을 했으면 goto 채팅방 (토스트 메세지만)  채팅방 불러오기 API 호출
                if (hasChatRoomAlready) {
                    ChatService.createChatRoom(this, myUserIdx, partnerUserIdx)
                }

                // 채팅방 없고 채팅 가능 횟수 있 -> 다이얼로그 이 다이얼로그 채팅 선택 시 채팅방으로 이동
                else if (!hasChatRoomAlready && todayChatAvaillCnt > 0) {
                    setCreateNewDialogSuccess(todayChatAvaillCnt)
                }

                // 채팅방 없고 채팅 가능 횟수 없 -> 다이얼로그 , API 호출 없음(오늘 채팅 가능한 횟수가 모두 소진되었습니다.
                else if (!hasChatRoomAlready && todayChatAvaillCnt == 0) {
                    setCreateNewDialogFail()
                }
            }
    }

    private fun deleteStarredFriend() {
        FriendListService.deleteStarredFriend(this, myUserIdx, partnerUserIdx)
        initIsNotStarred()
    }

    private fun addStarredFriend() {
        FriendListService.addStarredFriend(this, myUserIdx, partnerUserIdx)
        initIsStarred()
    }

    @SuppressLint("CutPasteId")
    private fun initIsStarred() {
        (context as MyProfileActivity).apply {
            findViewById<ImageButton>(R.id.player_is_starred_iv).apply {
                visibility = View.VISIBLE
                isEnabled = true
            }
            findViewById<ImageButton>(R.id.player_is_not_starred_iv).apply {
                visibility = View.INVISIBLE
                isEnabled = false
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun initIsNotStarred() {
        (context as MyProfileActivity).apply {
            findViewById<ImageButton>(R.id.player_is_starred_iv).apply {
                visibility = View.INVISIBLE
                isEnabled = false
            }
            findViewById<ImageButton>(R.id.player_is_not_starred_iv).apply {
                visibility = View.VISIBLE
                isEnabled = true
            }
        }
    }

    override fun onGetProfileInfoSuccess(partnerResDto: PartnerResDto) {
        setPartnerProfileInfo(partnerResDto) // 위쪽 프로필 데이터 설정
        setExperienceView()     // 구력 관련 글씨체 적용

        partnerProfileReviewDatas.clear()
        partnerProfileReviewDatas.addAll(partnerResDto.reviewResDto)
        startPostponedEnterTransition()

        // 채팅 가능 횟수, 이미 채팅방 있?
        todayChatAvaillCnt = partnerResDto.todayChatAvailCnt    // 전역 변수에 오늘 채팅 가능 남은 횟수
        hasChatRoomAlready = partnerResDto.hasChatRoomAlready   // 이미 채팅방이 생성되어있는지 세기

        // 리사이클러뷰 어댑터 연결, 데이터 연결, 레이아웃 매니저 설정
        val profileReviewRVAdapter = initRecyclerView() // 리뷰 불러오기

        goPlayerProfile(profileReviewRVAdapter) // 다른 회원 프로필로 이동 클릭 리스너
        goEveryReview(partnerResDto)            // 해당 회원의 모든 후기 보기 페이지로 이동 클릭 리스너
    }

    override fun onGetProfileInfoFailure(code: Int, message: String) {
        Log.d(TAG, "CODE : $code , MESSAGE : $message ")
        showToast("네크워크 상태 확인 후 다시 시도해주세요")
    }

    // 리뷰 리사이클러뷰
    private fun initRecyclerView(): PartnerProfileReviewRVAdapter {
        val profileReviewRVAdapter = PartnerProfileReviewRVAdapter(partnerProfileReviewDatas)
        binding.playingReviewContentRv.adapter = profileReviewRVAdapter
        binding.playingReviewContentRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        return profileReviewRVAdapter
    }

    /* 다른 회원 혹은 내 프로필로 이동*/
    private fun goPlayerProfile(profileReviewRVAdapter: PartnerProfileReviewRVAdapter) {
        profileReviewRVAdapter.clickPlayerReviewListener(object :
            PartnerProfileReviewRVAdapter.PlayerReviewItemClickListener {
            override fun onItemClick(partnerProfileReviewItem: ReviewResDto) {
                // 다른 회원 프로필로 이동
                if (partnerProfileReviewItem.writerIdx != getUserIdx()) {
                    val fragmentTransaction: FragmentTransaction =
                        (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                    fragmentTransaction.apply {
                        setReorderingAllowed(true)
                        setCustomAnimations(
                            R.anim.enter_from_right_anim,
                            R.anim.fade_out,
                            R.anim.fade_in,
                            R.anim.exit_to_right
                        )    // 애니메이션 효과 적용
                        replace(R.id.my_profile_into_fragment_container_fc, PlayerFragment().apply {
                            Log.d(TAG, "다른 회원 프로필에서 다른 회원 프로필로 이동")
                            arguments =
                                Bundle().apply {
                                    putInt("partnerUserIdx", partnerProfileReviewItem.writerIdx!!)
                                }
                        })
                        addToBackStack(null)
                        commit()
                    }
                }
                // 내 프로필로 이동
                else {
                    val fragmentTransaction: FragmentTransaction =
                        (context as MyProfileActivity).supportFragmentManager.beginTransaction()
                    fragmentTransaction.apply {
                        replace(R.id.my_profile_into_fragment_container_fc, MyProfileFragment())
                        fragmentTransaction.addToBackStack(null)
                        fragmentTransaction.commit()
                    }
                }
            }
        })
    }

    /* 해당 회원의 모든 후기 보기 페이지로 이동*/
    @SuppressLint("SetTextI18n")
    private fun goEveryReview(partnerResDto: PartnerResDto) {
        binding.playerPlayingReviewCountTv.setOnClickListener {
            val fragmentTransaction: FragmentTransaction =
                (context as MyProfileActivity).supportFragmentManager.beginTransaction()
            fragmentTransaction.apply {
                setReorderingAllowed(true)
                setCustomAnimations(
                    R.anim.enter_from_bottom_anim,
                    R.anim.fade_out,
                    R.anim.fade_in,
                    R.anim.exit_to_bottom
                )
                replace(R.id.my_profile_into_fragment_container_fc, EveryReviewFragment().apply {
                    arguments = Bundle().apply {
                        // 해당 회원의 프로필 정보를 gson.toJson 하여 보낸다.
                        val gson = Gson()
                        val profileJson = gson.toJson(partnerResDto.partnerInfoDto)
                        putString("profile", profileJson)
                        putBoolean("isFromPlayerProfile", true)
                    }
                })
                addToBackStack(null)
                commit()
            }

            (context as MyProfileActivity).apply {
                findViewById<TextView>(R.id.top_myProfile_tv).text =
                    "후기(${partnerResDto.partnerInfoDto.reviewCount.toString()}"
                findViewById<TextView>(R.id.edit_myProfile_tv).visibility = View.GONE
                findViewById<ConstraintLayout>(R.id.profile_bottom_chat_btn_cl).visibility =
                    View.GONE
            }
        }
    }

    // 파트너 프로필 정보 불러오기
    @SuppressLint("SetTextI18n")
    private fun setPartnerProfileInfo(partnerResDto: PartnerResDto) {

        partnerImgUrl = partnerResDto.partnerInfoDto.profilePhotoUrl
        partnerNickname = partnerResDto.partnerInfoDto.nickname

//        val profileGenderStr = toGenderStr(partnerResDto.partnerInfoDto.gender)
//        val locationName = partnerResDto.partnerInfoDto.locationName
//        val locationCategory = partnerResDto.partnerInfoDto.locationCategory
//        var location = "$locationCategory $locationName"
//        val profileRatingStr = toRatingStr(partnerResDto.partnerInfoDto.rating!!)
        val location =
            partnerResDto.partnerInfoDto.locationCategory + " " + partnerResDto.partnerInfoDto.locationName

        binding.playerNicknameTv.text = partnerNickname
        binding.playerGenerationTv.text = partnerResDto.partnerInfoDto.age
        binding.playerSexTv.text = toGenderStr(partnerResDto.partnerInfoDto.gender)
        binding.profileLocationTv.text = location
        binding.partnerProfileGradeRb.rating = partnerResDto.partnerInfoDto.rating!!
        binding.partnerProfileGradeNumTv.text =  toRatingStr(partnerResDto.partnerInfoDto.rating!!)
        binding.playerCareerYearNumTv.text = partnerResDto.partnerInfoDto.experience
        Glide.with(binding.playerProfileImgIv.context)
            .load(partnerImgUrl)
            .into(binding.playerProfileImgIv)
        binding.playerIntroductionTv.text = partnerResDto.partnerInfoDto.introduction
        binding.playerCareerPlayedNumTv.text = partnerResDto.partnerInfoDto.gameCount.toString()
        binding.playerPlayingReviewCountTv.text =
            "후기(${partnerResDto.partnerInfoDto.reviewCount.toString()})"

        isStarred = partnerResDto.partnerInfoDto.starred
        if (isStarred) {
            initIsStarred()
        } else {
            initIsNotStarred()
        }

    }

    private fun setExperienceView() {
        val textExperience = binding.playerCareerYearNumTv
        val textExperienceData: String = textExperience.text.toString()
        val textExperienceBuilder = SpannableStringBuilder(textExperienceData)
        val boldSpanEx = StyleSpan(Typeface.BOLD)
        textExperienceBuilder.setSpan(boldSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        val sizeBigSpanEx = RelativeSizeSpan(1.56f)
        textExperienceBuilder.setSpan(sizeBigSpanEx, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        // TextView 에 적용
        textExperience.text = textExperienceBuilder
    }

    override fun onAddStarredFriendSuccess() {
        Log.d(TAG, "친구 찜하기 성공")
        showToast("친구를 찜했습니다")
    }

    override fun onAddStarredFriendFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요")
    }

    override fun onDeleteStarredFriendSuccess() {
        showToast("친구를 찜 해제했습니다")
    }

    override fun onDeleteStarredFriendFailure(code: Int, message: String) {
        showToast("네트워크 상태 확인 후 다시 시도해주세요")
    }

    // 새 채팅방 생성하는 함수 : 만약 해당 회원과의 채팅방이 없으면 새로 룸에 INSERT 한다.//////////////////////////////////
    fun createRoom() {

        chatDB = ChatDatabase.getInstance(requireContext(), ChatDatabase.provideGson())!!
        val newChatRoom = ChatRoom(
            targetChatRoomIdx, partnerNickname, partnerImgUrl, participantList[0],
            "채팅을 해보세요", "오늘날짜 아님 null", false, -1
        )
        chatDB.chatRoomDao().insert(newChatRoom)
        Log.d(TAG, "새 채팅방 정보를 RoomDB에 넣기 : $newChatRoom")

    }

    //TODO 로딩 관련해서 오류가 나서 주석처리함 ////////////////////////////////////////////////////////////////////
//    fun startLoadingProgress() {
//        Log.d("로딩중", "채팅방 생성 api")
//        Handler(Looper.getMainLooper()).postDelayed(Runnable { progressOFF() }, 3500)
//    }

//    override fun onCreateChatRoomLoading() {
//        startLoadingProgress()
//    }


    //   채팅방이 이미 있을 때
    override fun onCreateChatRoomSuccess(createChatRoomResultData: CreateChatRoomResultData) {
        createdNewChatRoom = createChatRoomResultData.createdNewChatRoom //새로 생성했는가?
        targetChatRoomIdx = createChatRoomResultData.targetChatRoomIdx  // 생성한 채팅방 인덱스
        participantList =
            createChatRoomResultData.participantList      // 채팅방에 추가된 사람들 (나 포함임. 리스트 [1]이 나임.

        Log.d(
            TAG, "채팅방이 이미 있어서 해당 채팅방 Idx 호출. createdNewChatRoom : $createdNewChatRoom," +
                    "targetChatRoomIdx : $targetChatRoomIdx ," + " participantList : $participantList"
        )

        Toast.makeText(context, "이미 채팅방이 있어 해당 채팅방으로 이동합니다.", Toast.LENGTH_SHORT).show()
        // chatDB 불러와서 해당 채팅 정보를 불러온다.
        chatDB = ChatDatabase.getInstance(requireContext(), ChatDatabase.provideGson())!!
        var lastChatRoom = chatDB.chatRoomDao().getChatRoom(targetChatRoomIdx)

        if (lastChatRoom == null) { /* DB에 채팅방이 이미 있는데 해당 정보가 null 일 때 */
            // 새로 DB에 넣어주어야 해 약속 유무 정보 가져오기
            AppointmentService.isAppointmentExist(this, myUserIdx, partnerUserIdx)

            chatDB = ChatDatabase.getInstance(requireContext(), ChatDatabase.provideGson())!!
            lastChatRoom = ChatRoom(
                targetChatRoomIdx, partnerNickname, partnerImgUrl, participantList[0],
                "", "", appointmentIsExisted, appointmentIdx
            )
            chatDB.chatRoomDao().insert(lastChatRoom)
            Log.d(TAG, "채팅방 정보를 RoomDB에 넣기 : $lastChatRoom")

            lastChatRoom = chatDB.chatRoomDao().getChatRoom(targetChatRoomIdx)
        }

        val intent = Intent(activity, ChattingActivity::class.java)
        intent.apply {      /* ChatActivity 로 intent 정보 넘겨주기*/
            putExtra("isFromPlayerProfile", true)
            putExtra("createdNewChatRoom", createdNewChatRoom)  // 새로 생성된 채팅방인지 : Boolean
            putExtra("targetChatRoomIdx", targetChatRoomIdx)    // 채팅룸Idx
            putExtra("partnerIdx", participantList[0])          // 채팅 상대 Player Idx
            putExtra("partnerNickName", partnerNickname)        // 파트너 이름
            putExtra("isAppointmentExist", lastChatRoom.isAppointmentExist)     // 약속 유무
            putExtra("appointmentIdx", lastChatRoom.appointmentIdx)             // 약속 인덱스
        }
        startActivity(intent)
    }

    override fun onCreateChatRoomFailure(code: Int, message: String) {
        Toast.makeText(activity, "네트워크 상태 확인 후 다시 시도해주세요.", Toast.LENGTH_LONG).show()
        showToast("네트워크 상태 확인 후 다시 시도해주세요")
    }

    private fun setCreateNewDialogSuccess(count: Int) {
        PartnerProfileChatDialog.Builder(requireContext())
            .setCount(count)
            .setRightButton(object :
                PartnerProfileChatDialog.PartnerProfileChatDialogCallbackRight {
                override fun onClick(dialog: PartnerProfileChatDialog) {
                    // 채팅방 생성 OR 불러오기 API 호출
                    ChatService.createChatRoomInDialog(this, myUserIdx, partnerUserIdx)
                    dialog.dismiss()
                }

                override fun onCreateChatRoomSuccess(createChatRoomResultData: CreateChatRoomResultData) {
                    Log.d(TAG, "채팅방 생성 및 이동하기 API 호출 성공")
                    createdNewChatRoom = createChatRoomResultData.createdNewChatRoom
                    targetChatRoomIdx = createChatRoomResultData.targetChatRoomIdx
                    participantList = createChatRoomResultData.participantList

                    Log.d(
                        TAG, "신규 채팅방 생성, 채팅 가능 횟수 있음의 경우 : 원래 채팅방 있? : $hasChatRoomAlready " +
                                "남은 채팅 가능 횟수 : $todayChatAvaillCnt"
                    )
                    createRoom()
                    val intent = Intent(activity, ChattingActivity::class.java)
                    intent.apply {
                        putExtra("isFromPlayerProfile", true)
                        putExtra("createdNewChatRoom", createdNewChatRoom)
                        putExtra("targetChatRoomIdx", targetChatRoomIdx)
                        putExtra("partnerNickName", partnerNickname)
                        putExtra("partnerIdx", participantList[1])
                        putExtra("isAppointmentExist", false)
                        putExtra("appointmentIdx", -1)
                    }
                    Log.d(
                        TAG, "Intent에 전달할 값 넣기 새로 생성 됨? createdNewChatRoom : $createdNewChatRoom," +
                                "targetChatRoomIdx : $targetChatRoomIdx, 참여자 : ${participantList[1]}"
                    )
                    startActivity(intent)
                }

                override fun onCreateChatRoomFailure(code: Int, message: String) {
                    showToast("네트워크 상태 확인 후 다시 시도해주세요.")
                }

            })
            .setLeftButton(object : PartnerProfileChatDialog.PartnerProfileChatDialogCallbackLeft {
                override fun onClick(dialog: PartnerProfileChatDialog) {
                    dialog.dismiss()
                    Log.d(TAG, "취소 버튼을 누름")

                }

            })
            .show()
    }

    // 남은 채팅회수가 없을 때
    private fun setCreateNewDialogFail() {
        PartnerProfileChatUnavailableDialog.Builder(requireContext())
            .setLeftButton(object :
                PartnerProfileChatUnavailableDialog.PartnerProfileChatDialogCallbackLeft {
                override fun onClick(dialog: PartnerProfileChatUnavailableDialog) {
                    dialog.dismiss()
                }
            })
            .show()
    }

    // 약속 유무 불러오기
    override fun onAppointmentExistSuccess(isExisted: Boolean, appointmentIdx: Int) {
        this.appointmentIsExisted = isExisted
        this.appointmentIdx = appointmentIdx
    }

    override fun onAppointmentExistFailure(code: Int, message: String) {
        Log.d(TAG, "CODE : $code , MESSAGE : $message ")
        showToast("네트워크 연결 확인 후 다시 시도해주세요")
    }


}







