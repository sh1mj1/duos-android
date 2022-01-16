package com.example.duos.ui.main.chat

import android.content.Context
import android.util.Log
import com.example.duos.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator(context: Context): DayViewDecorator {
    private var date = CalendarDay.today()
    // val drawable = context.resources.getDrawable(R.drawable.style_only_radius_10)
    val drawable = context.getDrawable(R.drawable.today_decorator)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }
    override fun decorate(view: DayViewFacade?) {
        if (drawable != null) {
            view?.setBackgroundDrawable(drawable)
        }
        else{
            Log.d("today_decorator","is null")
        }
    }
}