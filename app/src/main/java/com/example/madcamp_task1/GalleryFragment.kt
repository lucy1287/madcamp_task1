package com.example.madcamp_task1

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.EventAdapter
import com.example.madcamp_task1.databinding.FragmentGalleryBinding
import com.example.madcamp_task1.roomdb.AppDatabase
import com.example.madcamp_task1.roomdb.Event
import com.example.madcamp_task1.roomdb.EventViewModel
import com.example.madcamp_task1.roomdb.EventViewModelFactory
import java.text.SimpleDateFormat
import java.util.*


class GalleryFragment : Fragment () {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var eventViewModel: EventViewModel
    private lateinit var events: LiveData<List<Event>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(layoutInflater)
        initializeViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNewEvent.setOnClickListener {

            val constraintLayout = View.inflate(requireActivity(), R.layout.dialog_new_event_title, null) as ConstraintLayout

            AlertDialog.Builder(requireActivity())
                .setView(constraintLayout)
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, whichButton ->
                    val etNewEventName = constraintLayout.findViewById<View>(R.id.et_new_event_title) as EditText
                    val value = etNewEventName.text.toString()
                    Log.d("이름", value)
                    dialog.dismiss()

                    val currentDate = Date()
                    val formatter = SimpleDateFormat("yyMMddHHmmss")
                    val currentDateString = formatter.format(currentDate)

                    val newEvent = Event(
                        title = value,
                        detail = "detail",
                        imageUrl = "ddd",
                        createdDate = currentDateString)
                    eventViewModel.addEvent(newEvent)

                })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, whichButton -> dialog.dismiss() })
                .show()
        }
    }

    private fun initializeViews(){
        val db = AppDatabase.getInstance(requireContext())
        val eventDao = db!!.eventDao()

        val factory = EventViewModelFactory(eventDao)
        eventViewModel = ViewModelProvider(this, factory).get(EventViewModel::class.java)

        events = eventViewModel.getAllEvents

        events.observe(viewLifecycleOwner, { eventList ->
            binding.rvEventList.adapter = EventAdapter(eventList)
        })

        binding.rvEventList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventList.adapter = events.value?.let { EventAdapter(it) }

    }

//    private fun sortEventsByDate() {
//        val formatter = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
//        events.sortByDescending { event -> formatter.parse(event.createdDate) }
//    }
}