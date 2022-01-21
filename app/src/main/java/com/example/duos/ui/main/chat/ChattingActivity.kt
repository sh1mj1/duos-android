package com.example.duos.ui.main.chat

import android.content.Intent
import android.widget.EditText
import android.widget.ImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.duos.databinding.ActivityChattingBinding
import com.example.duos.ui.BaseActivity
import androidx.recyclerview.widget.DefaultItemAnimator

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.duos.data.entities.MessageItem
import com.example.duos.data.entities.ChatType

import android.util.Log

import android.R

import android.view.View
import com.example.duos.data.entities.MessageData
import java.text.SimpleDateFormat
import java.util.*


class ChattingActivity: BaseActivity<ActivityChattingBinding>(ActivityChattingBinding::inflate) {
    var roomIdx: Int = 0
    var userId: String = "나나"
    var partnerId: String = "상대방"
    private var layoutManager: LayoutManager? = null
    lateinit var chattingMessagesRVAdapter: ChattingMessagesRVAdapter
    lateinit var chattingRV: RecyclerView
    lateinit var chattingEt: EditText

    override fun initAfterBinding() {
        binding.chattingMakePlanBtn.setOnClickListener ({
            val intent = Intent(this, MakePlanActivity::class.java)
            startActivity(intent)
        })
        chattingEt = binding.chattingEt
        chattingRV = binding.chattingMessagesRv

        var sendBtn: ImageButton = binding.chattingSendBtn

        chattingRV.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this)
        chattingRV.setLayoutManager(layoutManager)
        chattingRV.setItemAnimator(DefaultItemAnimator())
        chattingMessagesRVAdapter = ChattingMessagesRVAdapter()
        chattingRV.setAdapter(chattingMessagesRVAdapter)

        var chattingMessage = MessageData("ENTER", userId, roomIdx.toString(),"2022년 01월 21일", System.currentTimeMillis())
        addChat(chattingMessage)
        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)

        sendBtn.setOnClickListener{
                v -> sendMessage(v)
        }
    }

    // 리사이클러뷰에 채팅 추가
    private fun addChat(data: MessageData) {
        this.runOnUiThread {
            if (data.type.equals("ENTER") || data.type.equals("LEFT")) {
                chattingMessagesRVAdapter.addItem(
                    MessageItem(
                        data.from,
                        data.content,
                        toDate(data.sendTime),
                        ChatType.CENTER_MESSAGE
                    )
                )
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
            } else {
                chattingMessagesRVAdapter.addItem(
                    MessageItem(
                        data.from,
                        data.content,
                        toDate(data.sendTime),
                        ChatType.LEFT_MESSAGE
                    )
                )
                chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
            }
        }
    }

    private fun sendMessage(view: View) {
        Log.d(
            "MESSAGE", MessageData(
                "MESSAGE",
                userId, roomIdx.toString() + "",
                chattingEt.text.toString(),
                System.currentTimeMillis()
            ).toString()
        )
        chattingMessagesRVAdapter.addItem(
            MessageItem(
                userId,
                chattingEt.text.toString(),
                toDate(System.currentTimeMillis()),
                ChatType.RIGHT_MESSAGE
            )
        )
        chattingRV.scrollToPosition(chattingMessagesRVAdapter.itemCount - 1)
        chattingEt.setText("")
    }

    // System.currentTimeMillis를 몇시:몇분 am/pm 형태의 문자열로 반환
    private fun toDate(currentMiliis: Long): String {
        return SimpleDateFormat("a hh:mm").format(Date(currentMiliis))
    }
}