package com.example.duos.ui.main.chat

import android.util.Log
import com.example.duos.R
import com.example.duos.databinding.ActivityMakePlanBinding
import com.example.duos.ui.BaseActivity
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.CalendarMode
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*
import android.content.res.Configuration
import com.prolificinteractive.materialcalendarview.format.ArrayWeekDayFormatter
import com.prolificinteractive.materialcalendarview.format.MonthArrayTitleFormatter

import java.util.Locale




class MakePlanActivity: BaseActivity<ActivityMakePlanBinding>(ActivityMakePlanBinding::inflate) {
    override fun initAfterBinding() {

        var selectedDate: CalendarDay = CalendarDay.today()
        //var selectedYear: Int   // 년도 그대로
        //var selectedMonth: Int  // 0부터 시작 (0 = 1월)
        //var selectedDay: Int   // 1부터 시작
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
//        //val boldDecorator = BoldDecorator(stCalendarDay, enCalendarDay)
        val todayDecorator = TodayDecorator(this)
//        val eventDecorator = EventDecorator(this, selectedDate)


//        calendar.addDecorators(minMaxDecorator, todayDecorator, eventDecorator)
        calendar.addDecorators(minMaxDecorator, todayDecorator)
        //calendar.setDateTextAppearance(R.drawable.calendar_date_text_color)

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

        calendar.setOnDateChangedListener(object: OnDateSelectedListener{
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                Log.d("이전 selectedDate", selectedDate.toString())
                //var eventDecorator = EventDecorator(this@MakePlanActivity, selectedDate)
                calendar.removeDecorator(todayDecorator)    // 이전에 클릭했던 날짜의 decorator 삭제
                selectedDate = calendar.selectedDate
                Log.d("이후 selectedDate", selectedDate.toString())
                val eventDecorator = EventDecorator(this@MakePlanActivity, selectedDate)
                calendar.addDecorator(eventDecorator)
                //selectedYear = calendar.selectedDate.year
                //selectedMonth = calendar.selectedDate.month
                //selectedDay = calendar.selectedDate.day
//                Log.d("선택 년도", selectedYear.toString())
//                Log.d("선택 월", selectedMonth.toString())
//                Log.d("선택 날짜", selectedDay.toString())
            }
        })
    }
}