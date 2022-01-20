package com.example.duos.ui.main.chat

import android.content.Context
import android.graphics.Color
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.example.duos.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class TodayDecorator(context: Context): DayViewDecorator {
    private var date = CalendarDay.today()
    // val drawable = context.resources.getDrawable(R.drawable.style_only_radius_10)
    val drawable = context.getDrawable(R.drawable.selected_date_decorator_downscaled_ratio_adjusted)
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.equals(date)!!
    }
    override fun decorate(view: DayViewFacade?) {
        //view?.addSpan(ForegroundColorSpan(Color.parseColor("#34A94B")))
        if (drawable != null) {
            view?.setSelectionDrawable(drawable)
        }
        else{
            Log.d("today_decorator","is null")
        }
    }
}