package com.example.duos.ui.main.appointment

import android.content.Intent
import android.util.Log
import com.example.duos.data.entities.EditAppointment
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.data.remote.appointment.ShowAppointmentResultData
import com.example.duos.databinding.ActivityAppointmentInfoBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.utils.getUserIdx

class AppointmentInfoActivity: BaseActivity<ActivityAppointmentInfoBinding>(ActivityAppointmentInfoBinding::inflate),
ShowAppointmentView{
    lateinit var chatRoomIdx : String
    lateinit var chatDB: ChatDatabase

    override fun initAfterBinding() {
        val intent = intent
        chatRoomIdx = intent.getStringExtra("chatRoomIdx")!!
        chatDB = ChatDatabase.getInstance(this)!!
        val chatRoom : ChatRoom = chatDB.chatRoomDao().getChatRoom(chatRoomIdx)
        AppointmentService.showAppointment(this, chatRoom.appointmentIdx!!, getUserIdx()!!)

        binding.planInfoReviseTv.setOnClickListener {
            val intent = Intent(this, AppointmentEditActivity::class.java)
            intent.putExtra("chatRoomIdx", chatRoom.chatRoomIdx)
            startActivity(intent)
        }
    }

    override fun onShowAppointmentSuccess(result: ShowAppointmentResultData) {
        binding.planInfoDateTv.text = result.appointmentData
        binding.planInfoTimeTv.text = result.appointmentTime
    }

    override fun onShowAppointmentFailure(code: Int, message: String) {
        Log.d("약속정보조회 실패",code.toString() + ": " + message)
    }

}