package com.example.madcamp_task1

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.SearchAdapter
import com.example.madcamp_task1.databinding.ActivityGameeventBinding
import com.example.madcamp_task1.databinding.DialogAddParticipantBinding
import com.example.madcamp_task1.roomdb.Profile
import com.example.madcamp_task1.roomdb.ProfileViewModel

class GameEventActivity : AppCompatActivity() {

    private lateinit var editTextEventTitle: EditText
  //  private lateinit var editTextEventScore: EditText
    private lateinit var buttonSaveEvent: Button
    private lateinit var selectedDate: String

    private lateinit var binding : ActivityGameeventBinding
    private lateinit var dialogBinding: DialogAddParticipantBinding
    private lateinit var alertDialog: AlertDialog
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var searchResults : LiveData<List<Profile>>
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameeventBinding.inflate(layoutInflater)
        dialogBinding = DialogAddParticipantBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initializeViews()
        initSearchView()

        editTextEventTitle = binding.editTextEventTitle
  //      editTextEventScore = binding.editTextEventScore

        buttonSaveEvent = binding.buttonSaveEvent


        selectedDate = intent.getStringExtra("selectedDate") ?: ""

        buttonSaveEvent.setOnClickListener {
            val eventTitle = editTextEventTitle.text.toString()
         //   val eventScore = editTextEventScore.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("eventTitle", eventTitle)
        //    resultIntent.putExtra("eventScore", eventScore)
            resultIntent.putExtra("selectedDate", selectedDate)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    fun initializeViews() {

        searchAdapter = SearchAdapter { profile ->
            // 새로운 TextView를 동적으로 생성하고 레이아웃에 추가
            val textView = createProfileTextView(profile.name)
            binding.linearLayoutProfiles.addView(textView)
            alertDialog?.dismiss()
        }
//        searchResults = profileViewModel.allProfiles
//
//        searchResults.observe(viewLifecycleOwner, { searchResultsList ->
//            binding.rvSearchResultList.adapter = SearchAdapter()
//        })
//
//        binding.rvSearchResultList.layoutManager = LinearLayoutManager(requireContext())
//        searchAdapter = SearchAdapter()
//        binding.rvSearchResultList.adapter = searchAdapter

        //searchAdapter = SearchAdapter()

        binding.btnAddParticipant.setOnClickListener {

            val constraintLayout = dialogBinding.root as ConstraintLayout

            // 부모 뷰가 있는지 확인하고 제거
            val parent = constraintLayout.parent as? ViewGroup
            parent?.removeView(constraintLayout)

            alertDialog = AlertDialog.Builder(this)
                .setView(constraintLayout)
                .setPositiveButton("확인", DialogInterface.OnClickListener { dialog, whichButton ->

                })
                .setNegativeButton("취소",
                    DialogInterface.OnClickListener { dialog, whichButton -> dialog.dismiss() })
                .show()

            dialogBinding.rvSearchResultList.layoutManager = LinearLayoutManager(this)
            dialogBinding.rvSearchResultList.adapter = searchAdapter

            searchResults = profileViewModel.allProfiles

            searchResults.observe(this, { searchResultsList ->
                searchAdapter.submitList(searchResultsList)
            })
        }
    }

    private fun createProfileTextView(profileName: String): TextView {
        val textView = TextView(this).apply {
            text = "$profileName  X"
            setTextColor(ContextCompat.getColor(this@GameEventActivity, android.R.color.black))
            setBackgroundResource(R.drawable.item_background_event_participant)
            setPadding(16, 8, 16, 8)
            setOnClickListener {
                (this@GameEventActivity.binding.linearLayoutProfiles as ViewGroup).removeView(this)
            }
        }
        return textView
    }

    private fun initSearchView() {
        dialogBinding.search.isSubmitButtonEnabled = true
        dialogBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if(newText != null){
                    searchDatabase(newText)
                }
                return true
            }
        })
    }

    private fun searchDatabase(query: String) {
        val searchQuery = "%$query%"

        profileViewModel.searchDatabase(searchQuery).observe(this, { results ->
            Log.d("data", results.toString())
            results?.let {
                searchAdapter.submitList(results)
            }
        })
    }
}
