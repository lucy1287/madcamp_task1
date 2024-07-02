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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.madcamp_task1.databinding.FragmentCalendarBinding
import com.example.madcamp_task1.roomdb.Game
import com.example.madcamp_task1.roomdb.GameViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import org.threeten.bp.LocalDate


class CalendarFragment : Fragment() {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var binding: FragmentCalendarBinding
    private lateinit var gameViewModel: GameViewModel
    private lateinit var selectedDate: String
    private val events = mutableMapOf<CalendarDay, String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameViewModel = ViewModelProvider(this).get(GameViewModel::class.java)

        // CalendarMaterialView 초기화 및 설정
        calendarView = binding.calendarView
        calendarView.setOnDateChangedListener { widget, date, selected ->
            selectedDate = date.date.toString()
            openGameEventActivity(selectedDate)
        }

        // RoomDB에서 게임 데이터를 가져와 달력에 표시
        gameViewModel.getAllGames.observe(viewLifecycleOwner, Observer { games ->
            games?.let {
                // games를 이용해서 달력에 게임 정보를 표시하는 작업 수행
                for (game in games) {
                    val calendarDay = CalendarDay.from(LocalDate.parse(game.gameDate))
                    events[calendarDay] = game.gameTitle
                    calendarView.invalidateDecorators()
                    addEventDecorator()
                }
            }
        })
    }

    private fun openGameEventActivity(selectedDate: String) {
        // GameEventActivity를 호출하는 코드
        // Intent를 사용하여 selectedDate를 전달
        val intent = Intent(activity, GameEventActivity::class.java)
        intent.putExtra("selectedDate", selectedDate)
        Toast.makeText(requireContext(), "Sending {$selectedDate}", Toast.LENGTH_SHORT).show()
        startActivityForResult(intent, 1)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1 && resultCode == Activity.RESULT_OK && data != null) {
            val gameTitle = data.getStringExtra("eventTitle")
            val gameScore = data.getStringExtra("eventScore")
            val gameDateStr = data.getStringExtra("selectedDate")
            val gameMembers = data.getStringArrayListExtra("gameMembers") // Should Convert from JSON

            if (!gameTitle.isNullOrBlank() && !gameDateStr.isNullOrBlank()) {
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

                Toast.makeText(requireContext(), "Insert into Database", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), "Insert Group Members {$gameMembers}", Toast.LENGTH_SHORT).show()
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
}