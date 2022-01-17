package com.example.duos.ui.main.chat

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.example.duos.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class EventDecorator(context: Context, date: CalendarDay): DayViewDecorator {
    private var date = date
    // val drawable = context.resources.getDrawable(R.drawable.style_only_radius_10)
    val drawable = context.getDrawable(R.drawable.calendar_date_selector)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }
    override fun decorate(view: DayViewFacade?) {
        //view?.addSpan(ForegroundColorSpan(Color.parseColor("#34A94B")))
        //view?.addSpan(object: ForegroundColorSpan(Color.parseColor("#34A94B")){})
        if (drawable != null) {
            //view?.setBackgroundDrawable(drawable)
            view?.setSelectionDrawable(drawable)
            Log.d("event_decorator","ok")
        }
        else{
            Log.d("event_decorator","is null")
        }
    }
}