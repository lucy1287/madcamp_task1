package com.example.madcamp_task1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import org.threeten.bp.LocalDate

class CalendarFragment : Fragment() {

    private lateinit var calendarView: MaterialCalendarView
    private val events = mutableMapOf<CalendarDay, String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendarView)

        calendarView.setOnDateChangedListener { widget, date, selected ->
            val intent = Intent(activity, GameEventActivity::class.java)
            intent.putExtra("selectedDate", date.date.toString())
            startActivityForResult(intent, 1)
        }

        addEventDecorator()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val eventTitle = data.getStringExtra("eventTitle")
            val selectedDateStr = data.getStringExtra("selectedDate")

            if (!eventTitle.isNullOrBlank() && !selectedDateStr.isNullOrBlank()) {
                val selectedDate = LocalDate.parse(selectedDateStr)  // Parse String to LocalDate
                val calendarDay = CalendarDay.from(selectedDate)
                events[calendarDay] = eventTitle
                calendarView.invalidateDecorators()
            }
        }
    }

    private fun addEventDecorator() {
        calendarView.addDecorator(EventDecorator(events))
    }

    private class EventDecorator(private val events: Map<CalendarDay, String>) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return events.containsKey(day)
        }

        override fun decorate(view: DayViewFacade?) {
            view?.addSpan(DotSpan(5F, Color.RED)) // 이벤트 있는 날짜에 빨간 점 추가
        }
    }
}

