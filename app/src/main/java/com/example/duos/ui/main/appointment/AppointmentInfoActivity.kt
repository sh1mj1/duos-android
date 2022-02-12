package com.example.duos.ui.main.appointment

import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.duos.CustomDialog
import com.example.duos.R
import com.example.duos.data.entities.DeleteAppointment
import com.example.duos.data.entities.EditAppointment
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.data.remote.appointment.ShowAppointmentResultData
import com.example.duos.databinding.ActivityAppointmentInfoBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.utils.getUserIdx

class AppointmentInfoActivity: BaseActivity<ActivityAppointmentInfoBinding>(ActivityAppointmentInfoBinding::inflate),
ShowAppointmentView, DeleteAppointmentView{
    lateinit var chatRoomIdx : String
    lateinit var chatDB: ChatDatabase
    lateinit var chatRoom: ChatRoom

    override fun onStart() {
        super.onStart()
        Log.d("생명주기","onstart")
        AppointmentService.showAppointment(this, chatRoom.appointmentIdx!!, getUserIdx()!!)
    }

    override fun initAfterBinding() {
        chatRoomIdx = intent.getStringExtra("chatRoomIdx")!!
        chatDB = ChatDatabase.getInstance(this, ChatDatabase.provideGson())!!
        chatRoom = chatDB.chatRoomDao().getChatRoom(chatRoomIdx)

        setDialog()

        binding.planInfoReviseTv.setOnClickListener {
            val intent = Intent(this, AppointmentEditActivity::class.java)
            intent.putExtra("chatRoomIdx", chatRoom.chatRoomIdx)
            startActivity(intent)
        }
        binding.planInfoBackIv.setOnClickListener {
            finish()
        }
    }

    private fun setDialog() {

        val dialogBuilder = CustomDialog.Builder(this)// 만약 액티비티에서 사용한다면 requireContext() 가 아닌 context를 사용하면 됨.
                .setCommentMessage(getString(R.string.plan_info_delete_dialog_message)) // Dialog 텍스트 설정하기
                .setRightButton(getString(R.string.plan_info_delete_dialog_delete_btn), object : CustomDialog.CustomDialogCallback {
                    override fun onClick(
                        dialog: CustomDialog,
                        message: String
                    ) {
                        deleteAppointment()
                        Log.d("CustomDialog in SetupFrag", message.toString())  // 테스트 로그
                        dialog.dismiss()
                    }
                })
                .setLeftButton(getString(R.string.plan_info_delete_dialog_cancel_btn), object : CustomDialog.CustomDialogCallback {
                    override fun onClick(
                        dialog: CustomDialog,
                        message: String
                    ) {
                        dialog.dismiss()
                    }
                })
        binding.planInfoDeleteBtn.setOnClickListener {
            dialogBuilder.show()
        }
    }

    fun deleteAppointment(){
        val deleteAppointment = DeleteAppointment(chatRoomIdx, getUserIdx()!!, chatRoom.participantIdx!!, chatRoom.appointmentIdx
        !!)
        AppointmentService.deleteAppointment(this, chatRoom.appointmentIdx!!, getUserIdx()!!, deleteAppointment)
    }

    override fun onShowAppointmentSuccess(result: ShowAppointmentResultData) {
        Log.d("result",result.toString())
        binding.planInfoDateTv.text = result.appointmentData
        binding.planInfoTimeTv.text = result.appointmentTime
        chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx, result.appointmentIdx)
    }

    override fun onShowAppointmentFailure(code: Int, message: String) {
        Log.d("약속정보조회 실패",code.toString() + ": " + message)
    }

    override fun onDeleteAppointmentSuccess() {
        chatDB.chatRoomDao().updateAppointmentExist(chatRoomIdx, false)
        chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx, null)
        finish()
    }

    override fun onDeleteAppointmentFailure(code: Int, message: String) {
        showToast(code.toString() + " : " + message)
    }

}