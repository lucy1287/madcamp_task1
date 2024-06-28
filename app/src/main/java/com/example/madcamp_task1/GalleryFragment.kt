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
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.EventAdapter
import com.example.madcamp_task1.data.Event
import com.example.madcamp_task1.databinding.FragmentGalleryBinding


class GalleryFragment : Fragment () {

    private lateinit var binding: FragmentGalleryBinding

    private val events = listOf(
        Event("이탈리아 여행", "와인 투어와 중세 도시 여행", "ddd"),
        Event("생일 파티", "오랜만에 모여서 생일 파티", "ddd"),
        Event("생일 파티", "오랜만에 모여서 생일 파티", "ddd"),
        Event("생일 파티", "오랜만에 모여서 생일 파티", "ddd"),
        Event("생일 파티", "오랜만에 모여서 생일 파티", "ddd"),
        Event("생일 파티", "오랜만에 모여서 생일 파티", "ddd"),
        Event("생일 파티", "오랜만에 모여서 생일 파티", "ddd")
    );

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



                })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, whichButton -> dialog.dismiss() })
                .show()
        }
    }

    private fun initializeViews(){
        binding.rvEventList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvEventList.adapter = EventAdapter(events)

    }
}