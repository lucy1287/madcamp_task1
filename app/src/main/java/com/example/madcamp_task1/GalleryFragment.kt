package com.example.madcamp_task1

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
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


class GalleryFragment : Fragment() {

    private lateinit var binding: FragmentGalleryBinding
    private lateinit var eventViewModel: EventViewModel
    private lateinit var events: LiveData<List<Event>>
    private val REQUEST_CODE_READ_EXTERNAL_STORAGE = 1
    private var isViewInitialized = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGalleryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한이 허용되지 않았으면 요청
            requestStoragePermission()
            initializeViews()
        } else {
            // 권한이 이미 허용된 경우, 콘텐츠 접근을 진행
            initializeViews()
        }

        binding.btnNewEvent.setOnClickListener {
            if (isViewInitialized) {
                showNewEventDialog()
            } else {
                // 초기화가 완료되지 않은 경우
                Log.e("Error", "View is not initialized yet")
            }
        }
    }

    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_CODE_READ_EXTERNAL_STORAGE
        )
    }

    private fun initializeViews() {
        val db = AppDatabase.getInstance(requireContext())
        val eventDao = db!!.eventDao()

        val factory = EventViewModelFactory(eventDao)
        eventViewModel = ViewModelProvider(this, factory).get(EventViewModel::class.java)

        events = eventViewModel.getAllEvents

        events.observe(viewLifecycleOwner, { eventList ->
            binding.rvEventList.adapter = EventAdapter(eventList)
        })

        binding.rvEventList.layoutManager = LinearLayoutManager(requireContext())
        isViewInitialized = true
    }

    private fun showNewEventDialog() {
        val constraintLayout =
            View.inflate(requireActivity(), R.layout.dialog_new_event_title, null) as ConstraintLayout

        AlertDialog.Builder(requireActivity())
            .setView(constraintLayout)
            .setPositiveButton("확인") { dialog, whichButton ->
                val etNewEventName =
                    constraintLayout.findViewById<View>(R.id.et_new_event_title) as EditText
                val etNewEventDetail =
                    constraintLayout.findViewById<View>(R.id.et_new_event_detail) as EditText
                val titleValue = etNewEventName.text.toString()
                val detailValue = etNewEventDetail.text.toString()

                dialog.dismiss()

                val currentDate = Date()
                val formatter = SimpleDateFormat("yyMMddHHmmss", Locale.getDefault())
                val currentDateString = formatter.format(currentDate)

                val newEvent = Event(
                    title = titleValue,
                    detail = detailValue,
                    imageUrl = "ddd",
                    createdDate = currentDateString
                )
                eventViewModel.addEvent(newEvent)
            }
            .setNegativeButton("취소") { dialog, whichButton -> dialog.dismiss() }
            .show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_READ_EXTERNAL_STORAGE) {
            // 권한 요청의 결과 확인
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 권한이 허용된 경우, 콘텐츠 접근을 진행
                initializeViews()
            } else {
                // 권한이 거부된 경우, 처리
                requireActivity().finish()
            }
        }
    }
}