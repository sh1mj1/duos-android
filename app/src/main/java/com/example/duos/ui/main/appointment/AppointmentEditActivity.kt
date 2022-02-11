package com.example.duos.ui.main.appointment

import android.util.Log
import android.widget.TimePicker
import com.example.duos.R
import com.example.duos.data.entities.EditAppointment
import com.example.duos.data.entities.MakeAppointment
import com.example.duos.data.entities.chat.ChatRoom
import com.example.duos.data.local.ChatDatabase
import com.example.duos.data.remote.appointment.AppointmentService
import com.example.duos.databinding.ActivityAppointmentEditBinding
import com.example.duos.ui.BaseActivity
import com.example.duos.ui.main.chat.calendarDecorators.EventDecorator
import com.example.duos.ui.main.chat.calendarDecorators.MinMaxDecorator
import com.example.duos.ui.main.chat.calendarDecorators.TodayDecorator
import com.example.duos.utils.ViewModel
import com.example.duos.utils.getUserIdx
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter
import org.threeten.bp.LocalDateTime
import java.util.*

class AppointmentEditActivity: BaseActivity<ActivityAppointmentEditBinding>(ActivityAppointmentEditBinding::inflate),
EditAppointmentView{

    lateinit var chatDB: ChatDatabase
    lateinit var viewModel: ViewModel
    lateinit var chatRoomIdx : String
    lateinit var appointmentTime : String

    override fun initAfterBinding() {

        val intent = intent
        chatRoomIdx = intent.getStringExtra("chatRoomIdx")!!
        chatDB = ChatDatabase.getInstance(this)!!
        val chatRoom : ChatRoom = chatDB.chatRoomDao().getChatRoom(chatRoomIdx)

        initCalendar()

        binding.editPlanApplyTv.setOnClickListener {
            Thread.sleep(100)
            val editAppointment = EditAppointment(chatRoomIdx,
                getUserIdx()!!,chatRoom.participantIdx!! ,appointmentTime)
            Log.d("약속수정",editAppointment.toString())
            AppointmentService.editAppointment(this, chatRoom.appointmentIdx!!, editAppointment)
        }

        binding.editPlanBackIv.setOnClickListener {
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
        calendar = binding.editPlanCalendar
        calendar.setSelectedDate(CalendarDay.today())


        var startTimeCalendar = Calendar.getInstance()
        var endTimeCalendar = Calendar.getInstance()

        val currentYear = startTimeCalendar.get(Calendar.YEAR)
        val currentMonth = startTimeCalendar.get(Calendar.MONTH)
        val currentDate = startTimeCalendar.get(Calendar.DATE)

        endTimeCalendar.set(Calendar.MONTH, currentMonth+3)     // 이번 달부터 3달 후까지만 보여지도록 함

        val stCalendarDay = CalendarDay.from(currentYear, currentMonth, currentDate)
        val enCalendarDay = CalendarDay.from(endTimeCalendar.get(Calendar.YEAR), endTimeCalendar.get(
            Calendar.MONTH), endTimeCalendar.get(Calendar.DATE))

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
            .setMaximumDate(
                CalendarDay.from(currentYear, currentMonth+3, endTimeCalendar.getActualMaximum(
                    Calendar.DAY_OF_MONTH)))
            .setCalendarDisplayMode(CalendarMode.MONTHS)
            .commit()

        selectedDate = calendar.selectedDate
        val timePicker = binding.editPlanTimePicker
        timePicker.setIs24HourView(true)
        selectedHour = timePicker.hour
        selectedMinute = timePicker.minute
        appointmentTime = LocalDateTime.of(selectedDate.year,selectedDate.month+1, selectedDate.day, selectedHour, selectedMinute).toString() + ":00"

        calendar.setOnDateChangedListener(object: OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                calendar.removeDecorator(todayDecorator)    // 이전에 클릭했던 날짜의 decorator 삭제
                selectedDate = calendar.selectedDate
                Log.d("selectedDate", selectedDate.toString())
                val eventDecorator = EventDecorator(this@AppointmentEditActivity, selectedDate)
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

    override fun onEditAppointmentSuccess() {
        finish()
    }

    override fun onEditAppointmentFailure(code: Int, message: String) {
        showToast(code.toString() + " : " + message)
    }
}