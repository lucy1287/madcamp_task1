package com.example.madcamp_task1

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.SearchAdapter
import com.example.madcamp_task1.databinding.ActivityGameeventBinding
import com.example.madcamp_task1.databinding.DialogAddParticipantBinding
import com.example.madcamp_task1.roomdb.GameViewModel
import com.example.madcamp_task1.roomdb.Profile
import com.example.madcamp_task1.roomdb.ProfileViewModel


class GameEventActivity : AppCompatActivity() {

    private lateinit var editTextEventTitle: EditText
    private lateinit var editTextEventScore: EditText
    private lateinit var buttonSaveEvent: Button
    private lateinit var selectedDate: String
    private lateinit var gameTitle: String
    private lateinit var gameScore: String
    private lateinit var gameMembers: ArrayList<String>

    private lateinit var binding: ActivityGameeventBinding
    private lateinit var dialogBinding: DialogAddParticipantBinding
    private lateinit var alertDialog: AlertDialog
    private val profileViewModel: ProfileViewModel by viewModels()
    private val gameViewModel: GameViewModel by viewModels()
    private lateinit var searchResults: LiveData<List<Profile>>
    private lateinit var searchAdapter: SearchAdapter

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameeventBinding.inflate(layoutInflater)
        dialogBinding = DialogAddParticipantBinding.inflate(layoutInflater)

        setContentView(binding.root)

        initializeViews()
        initSearchView()

        editTextEventTitle = binding.editTextEventTitle
        editTextEventScore = binding.editTextEventScore

        buttonSaveEvent = binding.buttonSaveEvent


        val intent = intent
        selectedDate = intent.getStringExtra("selectedDate") ?: ""

//        Toast.makeText(this, "Received Game Info: {$selectedDate}", Toast.LENGTH_SHORT).show()

        // RoomDB에서 해당 날짜의 게임 정보를 가져와 UI에 표시
        gameViewModel.getGameByDate(selectedDate).observe(this, Observer { game ->
            game?.let {
                if (game != null) {
                    gameTitle = game.gameTitle
                    gameScore = game.gameScore
                    gameMembers = game.gameMembers // gameMembers가 null이 아닌지 확인하고 초기화

                    // UI 업데이트
                    binding.tvEventTitle.text = gameTitle
                    binding.tvEventScore.text = gameScore

                    // 기존에 있던 참가자들을 모두 삭제
                    binding.linearLayoutProfiles.removeAllViews()

                    // 현재 참가자들을 띄우기
                    for (name in gameMembers) {
                        val textView = createProfileTextView(name)
                        binding.linearLayoutProfiles.addView(textView)
                    }
                } else {
                    binding.tvEventTitle.text = "경기 제목"
                    binding.tvEventScore.text = "경기 결과"
                }
            }
        })


        buttonSaveEvent.setOnClickListener {
            val eventTitle = editTextEventTitle.text.toString()
            val eventScore = editTextEventScore.text.toString()

            // 현재 UI에 표시된 모든 참가자들의 이름을 가져오기
            val eventMembers = mutableListOf<String>()
            for (i in 0 until binding.linearLayoutProfiles.childCount) {
                val view = binding.linearLayoutProfiles.getChildAt(i)
                if (view is TextView) {
                    val profileName = view.text.toString().trim().replace("  X", "")
                    eventMembers.add(profileName)
                }
            }

            // Intent에 데이터 추가
            val resultIntent = Intent().apply {
                putExtra("eventTitle", eventTitle)
                putExtra("eventScore", eventScore)
                putExtra("selectedDate", selectedDate)
                putStringArrayListExtra("gameMembers", ArrayList(eventMembers))
            }

            // 결과 전송
            setResult(Activity.RESULT_OK, resultIntent)
//            Toast.makeText(this, "Sending Back Game Info: {$selectedDate, $eventTitle, $eventScore}", Toast.LENGTH_SHORT).show()
//            Toast.makeText(this, "Members: {$eventMembers}", Toast.LENGTH_SHORT).show()

            finish()
        }

    }

    @RequiresApi(Build.VERSION_CODES.N)
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

    @RequiresApi(Build.VERSION_CODES.N)
    private fun createProfileTextView(profileName: String): TextView {
        val textView = TextView(this).apply {
            text = " $profileName  X"
            setTextColor(ContextCompat.getColor(this@GameEventActivity, android.R.color.white))
            setBackgroundResource(R.drawable.item_background_event_participant)
            setPadding(30, 15, 30, 15)
            setTypeface(null, Typeface.BOLD)
            setOnClickListener {
                (this@GameEventActivity.binding.linearLayoutProfiles as ViewGroup).removeView(this)
            }
        }

        val layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            setMargins(0, 7, 0, 0)
        }
        textView.layoutParams = layoutParams

        return textView
    }

    private fun initSearchView() {
        dialogBinding.search.isSubmitButtonEnabled = true
        dialogBinding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
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
