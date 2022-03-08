package com.example.duos.ui.main.dailyMatching

import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.duos.ApplicationClass.Companion.TAG
import com.example.duos.BottomSheetDialog01
import com.example.duos.BottomSheetDialog02
import com.example.duos.BottomSheetDialog03
import com.example.duos.R
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.entities.dailyMatching.DailyMatchingDetail
import com.example.duos.data.entities.dailyMatching.DailyMatchingMessageParticipantIdx
import com.example.duos.data.entities.dailyMatching.DailyMatchingMessageResult
import com.example.duos.data.entities.dailyMatching.DailyMatchingOption
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.data.remote.chat.chat.ChatService
import com.example.duos.data.remote.chat.chat.CreateChatRoomResultData
import com.example.duos.data.remote.dailyMatching.DailyMatchingListService
import com.example.duos.data.remote.dailyMatching.DailyMatchingService
import com.example.duos.databinding.ActivityDailyMatchingDetailBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.appointment.AppointmentExistView
import com.example.duos.ui.main.chat.ChattingActivity
import com.example.duos.ui.main.chat.CreateChatRoomView
import com.example.duos.ui.main.mypage.myprofile.frag.PartnerProfileChatDialog
import com.example.duos.ui.main.mypage.myprofile.frag.PartnerProfileChatUnavailableDialog
import com.example.duos.utils.getUserIdx

class DailyMatchingDetail : BaseActivity<ActivityDailyMatchingDetailBinding>(
    ActivityDailyMatchingDetailBinding::inflate
), GetDailyMatchingDetailView, GetDailyMatchingOptionView, DailyMatchingOptionListener,
    DailyMatchingEndView,
    DailyMatchingMessageView,
    DailyMatchingDeleteView, CreateChatRoomView , AppointmentExistView {
    var boardIdx: Int = -1
    lateinit var dailyMatchingInfo: DailyMatchingDetail
    var optionSize: Int = 0
    var myUserIdx: Int = getUserIdx()!!
    var partnerUserIdx: Int = 0

    lateinit var chatDB: ChatDatabase

    private var todayChatAvaillCnt: Int = 0
    private var hasChatRoomAlready: Boolean = false
    private var createdNewChatRoom: Boolean = false
    private var targetChatRoomIdx: String = ""

    private var partnerImgUrl: String = ""
    private var partnerNickname: String = ""
    private var participantList: List<Int> = emptyList<Int>()

    private var appointmentIsExisted: Boolean = false
    private var appointmentIdx: Int = -1


    override fun onResume() {
        super.onResume()
        binding.dailyMatchingDetailContentImage01Iv.visibility = View.INVISIBLE
        binding.dailyMatchingDetailContentImage02Iv.visibility = View.INVISIBLE
        binding.dailyMatchingDetailContentImage03Iv.visibility = View.INVISIBLE
        DailyMatchingListService.getDailyMatchingDetail(this, boardIdx, getUserIdx()!!)
        DailyMatchingService.getDailyMatchingOption(this, boardIdx, getUserIdx()!!)
    }

    override fun initAfterBinding() {

        val intent = intent
        boardIdx = intent.getIntExtra("boardIdx", -1)
        if (boardIdx == -1) {
            showToast("존재하지 않는 게시물 입니다.")
            finish()
        }

        binding.dailyMatchingDetailBackArrowIv.setOnClickListener {
            finish()
        }
        binding.dailyMatchingDetailOptionIconIv.setOnClickListener {
            when (optionSize) {
                1 -> {
                    val bottomSheet = BottomSheetDialog02()
                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                }
                2 -> {
                    val bottomSheet = BottomSheetDialog03()
                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                }
                3 -> {
                    val bottomSheet = BottomSheetDialog01(boardIdx)
                    bottomSheet.show(supportFragmentManager, bottomSheet.tag)
                }
            }
        }

        binding.dailyMatchingDetailBottomBtn.setOnClickListener {
            when (optionSize) {
                1 -> {
                    onClickChat()
                }
                2, 3 -> {
                }
            }
        }


    }

    override fun onGetDailyMatchingDetailSuccess(dailyMatchingDetail: DailyMatchingDetail) {
        dailyMatchingInfo = dailyMatchingDetail

        partnerUserIdx = dailyMatchingInfo.userIdx
        partnerNickname = dailyMatchingInfo.nickname
        partnerImgUrl = dailyMatchingInfo.profileUrl

        Glide.with(binding.dailyMatchingDetailProfileImageIv.context)
            .load(dailyMatchingDetail.profileUrl)
            .into(binding.dailyMatchingDetailProfileImageIv)
        binding.dailyMatchingDetailNicknameTv.text = dailyMatchingDetail.nickname
        binding.dailyMatchingDetailReviewCountTv.text = dailyMatchingDetail.review_Num.toString()
        binding.dailyMatchingDetailRatingTv.text = String.format("%.1f", dailyMatchingDetail.rating)
        binding.dailyMatchingPostPreviewLocationTv.text = dailyMatchingDetail.matchPlace
        binding.dailyMatchingDetailDateTv.text =
            dailyMatchingDetail.matchDate + " (" + dailyMatchingDetail.dayOfWeek + ")"
        binding.dailyMatchingDetailTimeTv.text =
            dailyMatchingDetail.startTime + " - " + dailyMatchingDetail.endTime
        binding.dailyMatchingDetailTimeCountTv.text = dailyMatchingDetail.duration.toString() + "시간"
        binding.dailyMatchingDetailContentTv.text = dailyMatchingDetail.content
        binding.dailyMatchingDetailContentTimeTv.text = dailyMatchingDetail.regBefore
        binding.dailyMatchingDetailSeeCountTv.text = dailyMatchingDetail.viewCount.toString()
        binding.dailyMatchingDetailChatCountTv.text = dailyMatchingDetail.messageCount.toString()
        binding.dailyMatchingDetailPreviewDateTv.text = dailyMatchingDetail.stringForMatchDateGap
        when (dailyMatchingDetail.urls.size) {
            1 -> {
                Log.d("사진크기", "1")
                binding.dailyMatchingDetailContentImage01Iv.visibility = View.VISIBLE
                Glide.with(binding.dailyMatchingDetailContentImage01Iv)
                    .load(dailyMatchingDetail.urls[0])
                    .into(binding.dailyMatchingDetailContentImage01Iv)
            }
            2 -> {
                Log.d("사진크기", "2")
                binding.dailyMatchingDetailContentImage02Iv.visibility = View.VISIBLE
                binding.dailyMatchingDetailContentImage01Iv.visibility = View.VISIBLE
                Glide.with(binding.dailyMatchingDetailContentImage01Iv)
                    .load(dailyMatchingDetail.urls[0])
                    .into(binding.dailyMatchingDetailContentImage01Iv)
                Glide.with(binding.dailyMatchingDetailContentImage02Iv)
                    .load(dailyMatchingDetail.urls[1])
                    .into(binding.dailyMatchingDetailContentImage02Iv)
            }
            3 -> {
                Log.d("사진크기", "3")
                binding.dailyMatchingDetailContentImage03Iv.visibility = View.VISIBLE
                binding.dailyMatchingDetailContentImage02Iv.visibility = View.VISIBLE
                binding.dailyMatchingDetailContentImage01Iv.visibility = View.VISIBLE
                Glide.with(binding.dailyMatchingDetailContentImage01Iv)
                    .load(dailyMatchingDetail.urls[0])
                    .into(binding.dailyMatchingDetailContentImage01Iv)
                Glide.with(binding.dailyMatchingDetailContentImage02Iv)
                    .load(dailyMatchingDetail.urls[1])
                    .into(binding.dailyMatchingDetailContentImage02Iv)
                Glide.with(binding.dailyMatchingDetailContentImage03Iv)
                    .load(dailyMatchingDetail.urls[2])
                    .into(binding.dailyMatchingDetailContentImage03Iv)
            }
        }
    }

    override fun onGetDailyMatchingDetailFailure(code: Int, message: String) {
        Log.d(TAG, code.toString() + " : " + message)
        finish()
    }

    override fun onGetDailyMatchingOptionSuccess(dailyMatchingOption: DailyMatchingOption) {
        optionSize = dailyMatchingOption.options.size
        if (optionSize == 3) {
            binding.dailyMatchingDetailBottomBtn.text =
                getString(R.string.daily_matching_detail_chatting_list)
        }
    }

    override fun onGetDailyMatchingOptionFailure(code: Int, message: String) {
        Log.d(TAG, code.toString() + " : " + message)
    }

    override fun onClickEdit() {
        val intent = Intent(this, DailyMatchingEdit::class.java)
        intent.putExtra("boardIdx", boardIdx)
        intent.putExtra("dailyMatchingInfo", dailyMatchingInfo)
        startActivity(intent)
    }

    override fun onClickDelete() {
        DailyMatchingService.dailyMatchingDelete(this, boardIdx, getUserIdx()!!)
    }

    override fun onClickChat() {
        val participantIdx = DailyMatchingMessageParticipantIdx(myUserIdx, partnerUserIdx)
        Log.d("채팅보내기정보", "$boardIdx, ${getUserIdx()!!}, ${dailyMatchingInfo.userIdx}")
        DailyMatchingService.dailyMatchingMessage(this, boardIdx, participantIdx)
    }

    override fun onClickEnd() {
        DailyMatchingService.dailyMatchingEnd(this, boardIdx, getUserIdx()!!)
    }

    override fun onDailyMatchingEndSuccess() {
        showToast("마감이 완료되었습니다.")
        finish()
    }

    override fun onDailyMatchingEndFailure(code: Int, message: String) {
        showToast(message)
    }

    override fun onDailyMatchingDeleteSuccess() {
        showToast("게시물 삭제가 완료되었습니다.")
        finish()
    }

    override fun onDailyMatchingDeleteFailure(code: Int, message: String) {
        showToast(message)
    }

    override fun onDailyMatchingMessageSuccess(dailyMatchingMessageResult: DailyMatchingMessageResult) {
        Log.d("result", dailyMatchingMessageResult.toString())
        hasChatRoomAlready = dailyMatchingMessageResult.createdNewChatRoom
        todayChatAvaillCnt = dailyMatchingMessageResult.remains
        if (hasChatRoomAlready == true) {
            // 만약 이미 채팅을 했으면 goto 채팅방 (토스트 메세지만)
            Log.d(TAG, "이미 채팅 있다")
            ChatService.createChatRoom(this, myUserIdx, partnerUserIdx) // 채팅방 불러오기 API 호출
        }
        // 채팅방 없고 채팅 가능 횟수 있 -> 다이얼로그 이 다이얼로그 채팅 선택 시 채팅방으로 이동
        else if (hasChatRoomAlready == false && todayChatAvaillCnt > 0) {
            Log.d(TAG, "채팅 없고 채팅방 생성 가능")
            setCreateNewDialogSuccess(todayChatAvaillCnt)
        }
        // 채팅방 없고 채팅 가능 횟수 없 -> 다이얼로그 , API 호출 없음(오늘 채팅 가능한 횟수가 모두 소진되었습니다.
        else if (hasChatRoomAlready == false && todayChatAvaillCnt == 0) {   // 신규 채팅방이지만, 오늘 가능 횟수 없음
            Log.d(TAG, "신규 채팅방 But, 채팅 가능 횟수 없음의 경우.")
            setCreateNewDialogFail()
            //                    Toast.makeText(context, "오늘 할 수 있는 채팅 수를 초과하였습니다.", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDailyMatchingMessageFailure(code: Int, message: String) {
        when (code) {
            6401 -> {}
            2014, 5021 -> {
                showToast("존재하지 않는 유저 인덱스입니다.")
            }
            2201 -> {
                showToast("존재하지 않는 게시글입니다")
            }

        }
    }

    override fun onCreateChatRoomSuccess(createChatRoomResultData: CreateChatRoomResultData) {
        createdNewChatRoom = createChatRoomResultData.createdNewChatRoom //새로 생성했는가?
        targetChatRoomIdx = createChatRoomResultData.targetChatRoomIdx  // 생성한 채팅방 인덱스
        participantList =
            createChatRoomResultData.participantList      // 채팅방에 추가된 사람들 (나 포함임. 리스트 [1]이 나임.

        Log.d(
            TAG, "채팅방이 이미 있어서 해당 채팅방 Idx 호출. createdNewChatRoom : $createdNewChatRoom," +
                    "targetChatRoomIdx : $targetChatRoomIdx ," + " participantList : $participantList"
        )

        Toast.makeText(this, "이미 채팅방이 있어 해당 채팅방으로 이동합니다.", Toast.LENGTH_SHORT).show()
        // chatDB 불러와서 해당 채팅 정보를 불러온다.
        chatDB = ChatDatabase.getInstance(this, ChatDatabase.provideGson())!!
        var lastChatRoom = chatDB.chatRoomDao().getChatRoom(targetChatRoomIdx)
        Log.d(TAG, "Room 불러와서 Romm 에 원래 있던 채팅방이니까 불러오기 $lastChatRoom")

        if (lastChatRoom == null) { /* DB에 채팅방이 이미 있는데 해당 정보가 null 일 때 */
            // 새로 DB에 넣어주어야 해 약속 유무 정보 가져오기
            AppointmentService.isAppointmentExist(this, myUserIdx, partnerUserIdx)

            chatDB = ChatDatabase.getInstance(this, ChatDatabase.provideGson())!!
            lastChatRoom = ChatRoom(
                targetChatRoomIdx, partnerNickname, partnerImgUrl, participantList[0],
                "", "", appointmentIsExisted, appointmentIdx
            )
            chatDB.chatRoomDao().insert(lastChatRoom)
            Log.d(TAG, "채팅방 정보를 RoomDB에 넣기 : $lastChatRoom")

            lastChatRoom = chatDB.chatRoomDao().getChatRoom(targetChatRoomIdx)
        }

        val intent = Intent(this, ChattingActivity::class.java)
        intent.apply {      /* ChatActivity 로 intent 정보 넘겨주기*/
            putExtra("isFromPlayerProfile", true)         // 파트너 프로필에서 옴
            putExtra("createdNewChatRoom", createdNewChatRoom)  // 새로 생성된 채팅방인지 : Boolean
            putExtra("targetChatRoomIdx", targetChatRoomIdx)    // 채팅룸Idx
            putExtra("partnerIdx", participantList[0])          // 채팅 상대 Player Idx
            putExtra("partnerNickName", partnerNickname)        // 파트너 이름
            putExtra("isAppointmentExist", lastChatRoom.isAppointmentExist)     // 약속 유무
            putExtra("appointmentIdx", lastChatRoom.appointmentIdx)             // 약속 인덱스
        }
        Log.d(TAG, "Intent에 값 넣기")
        startActivity(intent)
    }

    override fun onCreateChatRoomFailure(code: Int, message: String) {
        TODO("Not yet implemented")
    }


    private fun setCreateNewDialogSuccess(count: Int) {
        PartnerProfileChatDialog.Builder(this)
            .setCount(count)
            .setRightButton(object :
                PartnerProfileChatDialog.PartnerProfileChatDialogCallbackRight {
                override fun onClick(dialog: PartnerProfileChatDialog) {
                    Log.d(TAG, "채팅하기 선택")
                    ChatService.createChatRoomInDialog(
                        this,
                        myUserIdx,
                        partnerUserIdx
                    ) // 채팅방 생성 OR 불러오기 API 호출
                    dialog.dismiss()
                }

                override fun onCreateChatRoomSuccess(createChatRoomResultData: CreateChatRoomResultData) {
                    Log.d(TAG, "채팅방 생성 및 이동하기 API 호출 성공")
                    createdNewChatRoom =
                        createChatRoomResultData.createdNewChatRoom //TODO 새로 생성했는가?
                    targetChatRoomIdx =
                        createChatRoomResultData.targetChatRoomIdx  // TODO 생성한 채팅방 인덱스
                    participantList =
                        createChatRoomResultData.participantList      // TODO 채팅방에 추가된 사람들 (나 포함임. 리스트 [1]이 나임.

                    Log.d(
                        TAG, "신규 채팅방 생성, 채팅 가능 횟수 있음의 경우 : 원래 채팅방 있? : $hasChatRoomAlready " +
                                "남은 채팅 가능 횟수 : $todayChatAvaillCnt"
                    )
                    createRoom()    // TODO 새 채팅방 RoomDB에 업데이트
                    val intent = Intent(this@DailyMatchingDetail, ChattingActivity::class.java)
                    intent.apply {      // TODO ChatActivity 로 intent 정보 넘겨주기
                        putExtra("isFromPlayerProfile", true)         //TODO 파트너 프로필에서 옴
                        putExtra(
                            "createdNewChatRoom",
                            createdNewChatRoom
                        )  //TODO 새로 생성된 채팅방인가? : Boolean
                        putExtra("targetChatRoomIdx", targetChatRoomIdx)    //TODO 채팅룸Idx
                        putExtra("partnerNickName", partnerNickname)        //TODO 파트너 이름u
                        putExtra("partnerIdx", participantList[1])          //TODO 채팅 상대 Player Idx
                        putExtra("isAppointmentExist", false)   //TODO 첫 채팅방 생성이므로 약속 없음
                        putExtra("appointmentIdx", -1)          // TODO 첫 채팅방 생성이므로 약속 없음
                    }
                    Log.d(
                        TAG, "Intent에 전달할 값 넣기 새로 생성 됨? createdNewChatRoom : $createdNewChatRoom," +
                                "targetChatRoomIdx : $targetChatRoomIdx, 참여자 : ${participantList[1]}"
                    )
                    startActivity(intent)
                }

                override fun onCreateChatRoomFailure(code: Int, message: String) {
                    showToast("네트워크 상태 확인 후 다시 시도해주세요.")
                    //Toast.makeText(activity, "code: $code, message: $message", Toast.LENGTH_LONG).show()
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

    private fun setCreateNewDialogFail() {
        PartnerProfileChatUnavailableDialog.Builder(this)
            .setLeftButton(object :
                PartnerProfileChatUnavailableDialog.PartnerProfileChatDialogCallbackLeft {
                override fun onClick(dialog: PartnerProfileChatUnavailableDialog) {
                    dialog.dismiss()
                    Log.d(TAG, "남은 횟수 없어서 못해요 당신")
                }
            })
            .show()
    }

    // 새 채팅방 생성하는 함수 : 만약 해당 회원과의 채팅방이 없으면 새로 룸에 INSERT 한다.//////////////////////////////////
    fun createRoom() {
        Log.d(
            TAG,
            "채팅방 생성한 user의 userIdx : $myUserIdx , 채팅바 생성 : 상대 user의 userIdx : $partnerUserIdx"
        )
        chatDB = ChatDatabase.getInstance(this, ChatDatabase.provideGson())!!
        val newChatRoom = ChatRoom(
            targetChatRoomIdx, partnerNickname, partnerImgUrl, participantList[0],
            "채팅을 해보세요", "오늘날짜 아님 null", false, -1
        )
        chatDB.chatRoomDao().insert(newChatRoom)
        Log.d(TAG, "새 채팅방 정보를 RoomDB에 넣기 : $newChatRoom")

    }

    override fun onAppointmentExistSuccess(isExisted: Boolean, appointmentIdx: Int) {
        this.appointmentIsExisted = isExisted
        this.appointmentIdx = appointmentIdx
    }

    override fun onAppointmentExistFailure(code: Int, message: String) {
        Toast.makeText(this, code, Toast.LENGTH_LONG).show()
        Log.d(TAG, "CODE : $code , MESSAGE : $message ")
    }
}