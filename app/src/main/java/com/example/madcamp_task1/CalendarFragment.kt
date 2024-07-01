package com.example.madcamp_task1

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.room.TypeConverter
import com.example.madcamp_task1.roomdb.Game
import com.example.madcamp_task1.roomdb.GameViewModel
import com.example.madcamp_task1.roomdb.GameViewModelFactory
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import org.threeten.bp.LocalDate

class CalendarFragment : Fragment() {

    private lateinit var calendarView: MaterialCalendarView
    private val events = mutableMapOf<CalendarDay, String>()
    private lateinit var gameViewModel: GameViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)

        calendarView = view.findViewById(R.id.calendarView)

        calendarView.setOnDateChangedListener { widget, date, selected ->
            val intent = Intent(activity, GameEventActivity::class.java)
            gameViewModel.getGameByDate(date.date.toString()).observe(viewLifecycleOwner, {game ->
                if (game != null) {
                    intent.putExtra("gameTitle", game.gameTitle)
                    intent.putExtra("gameDate", game.gameDate)
                    intent.putExtra("gameScore", game.gameScore)
                    intent.putExtra("gameMembers", game.gameMembers)

                    val title = game.gameTitle
                    val score = game.gameScore

                    // Show each value as a Toast message
//                    Toast.makeText(requireContext(), "Game Info: $title", Toast.LENGTH_SHORT).show()
//                    Toast.makeText(requireContext(), "Game Score: $score", Toast.LENGTH_SHORT).show()
                }
            })
            intent.putExtra("selectedDate", date.date.toString())
            startActivityForResult(intent, 1)
        }

        val application = requireNotNull(this.activity).application
        gameViewModel = ViewModelProvider(this, GameViewModelFactory(application)).get(GameViewModel::class.java)

        addEventDecorator()

        return view
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val gameTitle = data.getStringExtra("eventTitle")
            val gameScore = data.getStringExtra("eventScore")
            val gameDateStr = data.getStringExtra("selectedDate")
            val gameMembers = data.getStringArrayListExtra("gameMembers") // Should Convert to JSON

            if (!gameTitle.isNullOrBlank() && !gameDateStr.isNullOrBlank() ) {
                val gameDate = LocalDate.parse(gameDateStr)  // Parse String to LocalDate
                val calendarDay = CalendarDay.from(gameDate)
                events[calendarDay] = gameTitle
                calendarView.invalidateDecorators()
                addEventDecorator()

                // Insert the game data into the database
                val game = Game(
                    gameDate = gameDateStr,
                    gameTitle = gameTitle,
                    gameScore = gameScore ?: "",
                    gameMembers = gameMembers ?: ArrayList()
                )
                gameViewModel.insert(game)

//                Toast.makeText(requireContext(), "Insert into Database", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun addEventDecorator() {
        events.forEach { (day, _) ->
            calendarView.addDecorator(object : DayViewDecorator {
                override fun shouldDecorate(d: CalendarDay?): Boolean {
                    return d == day
                }

                override fun decorate(view: DayViewFacade) {
                    view.addSpan(DotSpan(5F, Color.RED))
                    val gameTitle = events[day]
                    if (!gameTitle.isNullOrBlank()) {
                        view.addSpan(TextSpan(gameTitle, Color.BLACK))
                    }
                }
            })
        }
    }


    private class EventDecorator(private val events: Map<CalendarDay, String>) : DayViewDecorator {

        override fun shouldDecorate(day: CalendarDay?): Boolean {
            return events.containsKey(day)
        }

        override fun decorate(view: DayViewFacade) {
            view.addSpan(DotSpan(5F, Color.RED)) // Add the dot
            // Since we don't have access to 'day' here, we'll handle it differently
        }

        fun decorate(view: DayViewFacade, day: CalendarDay) {
            if (events.containsKey(day)) {
                view.addSpan(DotSpan(5F, Color.RED)) // Add the dot
                val gameTitle = events[day]
                if (!gameTitle.isNullOrBlank()) {
                    view.addSpan(TextSpan(gameTitle, Color.BLACK)) // Add the game title
                }
            }
        }
    }

}
