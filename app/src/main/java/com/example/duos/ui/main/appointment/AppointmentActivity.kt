package com.example.duos.ui.main.appointment

import android.content.Intent
import android.util.Log
import android.widget.TimePicker
import com.example.duos.R
import com.example.duos.data.entities.MakeAppointment
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.data.remote.appointment.MakeAppointmentResult
import com.example.duos.databinding.ActivityAppointmentBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.chat.ChattingActivity
import com.example.duos.ui.main.chat.calendarDecorators.EventDecorator
import com.example.duos.ui.main.chat.calendarDecorators.MinMaxDecorator
import com.example.duos.ui.main.chat.calendarDecorators.TodayDecorator
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getUserIdx
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import org.threeten.bp.LocalDateTime

class AppointmentActivity: BaseActivity<ActivityAppointmentBinding>(ActivityAppointmentBinding::inflate), MakeAppointmentView {

    lateinit var chatDB: ChatDatabase
    lateinit var viewModel: ViewModel
    lateinit var chatRoomIdx : String
    var partnerIdx : Int = 0
    lateinit var appointmentTime : String

    override fun initAfterBinding() {

        val intent = intent
        chatRoomIdx = intent.getStringExtra("chatRoomIdx")!!
        partnerIdx = intent.getIntExtra("partnerIdx", 0)

        chatDB = ChatDatabase.getInstance(this, ChatDatabase.provideGson())!!
        val chatRoom : ChatRoom = chatDB.chatRoomDao().getChatRoom(chatRoomIdx)
        Log.d("약속현황액티비티 - 채팅방 확인", chatDB.chatRoomDao().getChatRoom(chatRoomIdx).toString())
        Log.d("약속현황액티비티 - 채팅방 리스트 확인", chatDB.chatRoomDao().getChatRoomList().toString())

        initCalendar()

        binding.makePlanApplyTv.setOnClickListener {
            Thread.sleep(100)
            Log.d("유저인덱스", getUserIdx().toString())
            val makeAppointment = MakeAppointment(chatRoomIdx, getUserIdx()!!, partnerIdx, appointmentTime)
            Log.d("약속body",makeAppointment.toString())
            AppointmentService.makeAppointment(this, makeAppointment)
        }

        binding.makePlanBackIv.setOnClickListener {
            finish()
        }
    }


    fun initCalendar(){
        var selectedDate: CalendarDay = CalendarDay.today()
        //var selectedYear: Int   // 년도 그대로
        //var selectedMonth: Int  // 0부터 시작 (0 = 1월)
        //var selectedDay: Int   // 1부터 시작

        var selectedHour: Int
        var selectedMinute: Int

        lateinit var calendar: MaterialCalendarView
        calendar = binding.makePlanCalendar
        calendar.setSelectedDate(CalendarDay.today())


        var startTimeCalendar = Calendar.getInstance()
        var endTimeCalendar = Calendar.getInstance()

        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDate = startTimeCalendar.get(Calendar.DATE)

        endTimeCalendar.set(Calendar.MONTH, currentMonth+3)     // 이번 달부터 3달 후까지만 보여지도록 함

        val stCalendarDay = CalendarDay.from(currentYear, currentMonth, currentDate)
        val enCalendarDay = CalendarDay.from(endTimeCalendar.get(Calendar.YEAR), endTimeCalendar.get(Calendar.MONTH), endTimeCalendar.get(Calendar.DATE))

        //val sundayDecorator = SundayDecorator()
        //val saturdayDecorator = SaturdayDecorator()
        val minMaxDecorator = MinMaxDecorator(stCalendarDay, enCalendarDay)
        //val boldDecorator = BoldDecorator(stCalendarDay, enCalendarDay)
        val todayDecorator = TodayDecorator(this)

        calendar.addDecorators(minMaxDecorator, todayDecorator)

        calendar.setDateTextAppearance(R.style.CustomDateTextAppearance)
        calendar.setWeekDayTextAppearance(R.style.CustomWeekDayAppearance)
        calendar.setHeaderTextAppearance(R.style.CustomHeaderTextAppearance)

        calendar.setTitleFormatter(MonthArrayTitleFormatter(resources.getTextArray(R.array.custom_months)))
        calendar.setWeekDayFormatter(ArrayWeekDayFormatter(resources.getTextArray(R.array.custom_weekdays)))

        calendar.state().edit()
            .setMinimumDate(CalendarDay.from(currentYear, currentMonth, 1))
            .setMaximumDate(CalendarDay.from(currentYear, currentMonth+3, endTimeCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        selectedDate = calendar.selectedDate
        val timePicker = binding.makePlanTimePicker
        timePicker.setIs24HourView(true)
        selectedHour = timePicker.hour
        selectedMinute = timePicker.minute
        appointmentTime = LocalDateTime.of(selectedDate.year,selectedDate.month+1, selectedDate.day, selectedHour, selectedMinute).toString() + ":00"

        calendar.setOnDateChangedListener(object: OnDateSelectedListener{
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                calendar.removeDecorator(todayDecorator)    // 이전에 클릭했던 날짜의 decorator 삭제
                selectedDate = calendar.selectedDate
                Log.d("selectedDate", selectedDate.toString())
                val eventDecorator = EventDecorator(this@AppointmentActivity, selectedDate)
                calendar.addDecorator(eventDecorator)
                appointmentTime = LocalDateTime.of(selectedDate.year,selectedDate.month+1, selectedDate.day, selectedHour, selectedMinute).toString() + ":00"
                Log.d("약속시간",appointmentTime)
            }
        })

        timePicker.setOnTimeChangedListener(object: TimePicker.OnTimeChangedListener{
            override fun onTimeChanged(p0: TimePicker?, p1: Int, p2: Int) {
                selectedHour = timePicker.hour
                selectedMinute = timePicker.minute
                Log.d("selectedHour", selectedHour.toString())
                Log.d("selectedMinute", selectedMinute.toString())
                appointmentTime = LocalDateTime.of(selectedDate.year,selectedDate.month+1, selectedDate.day, selectedHour, selectedMinute).toString() + ":00"
                Log.d("약속시간",appointmentTime)
            }
        })

    }

    override fun onMakeAppointmentSuccess(makeAppointmentResult: MakeAppointmentResult) {
        Log.d("약속잡기","성공")
        chatDB.chatRoomDao().updateAppointmentExist(chatRoomIdx, true)
        Log.d("약속잡기 - appointmentIdx", makeAppointmentResult.appointmentIdx.toString())
        chatDB.chatRoomDao().updateAppointmentIdx(chatRoomIdx,  makeAppointmentResult.appointmentIdx)
        // response result로 오는 것이 없어, updateAppointmentIdx는 약속 현황에서 update함
        setResult(RESULT_OK)
        finish()
    }

    override fun onMakeAppointmentFailure(code: Int, message: String) {
        if(code == 2118){
            showToast("당일 약속의 경우, 약속 시간은 현재 시각으로부터 한시간 이후로 설정할 수 있습니다.")
        }else{
            showToast(message)
        }
    }
}