package com.example.madcamp_task1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.SearchAdapter
import com.example.madcamp_task1.databinding.ActivityGameeventBinding
import com.example.madcamp_task1.roomdb.Profile
import com.example.madcamp_task1.roomdb.ProfileViewModel

class GameEventActivity : AppCompatActivity() {

    private lateinit var editTextEventTitle: EditText
    private lateinit var buttonSaveEvent: Button
    private lateinit var selectedDate: String

    private lateinit var binding : ActivityGameeventBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var searchResults : LiveData<List<Profile>>
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameeventBinding.inflate(layoutInflater)
        initializeViews()
        initSearchView()

        editTextEventTitle = binding.editTextEventTitle
        buttonSaveEvent = binding.buttonSaveEvent

        selectedDate = intent.getStringExtra("selectedDate") ?: ""

        buttonSaveEvent.setOnClickListener {
            val eventTitle = editTextEventTitle.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("eventTitle", eventTitle)
            resultIntent.putExtra("selectedDate", selectedDate)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        setContentView(binding.root)
    }

    fun initializeViews() {
//        searchResults = profileViewModel.allProfiles
//
//        searchResults.observe(viewLifecycleOwner, { searchResultsList ->
//            binding.rvSearchResultList.adapter = SearchAdapter()
//        })
//
//        binding.rvSearchResultList.layoutManager = LinearLayoutManager(requireContext())
//        searchAdapter = SearchAdapter()
//        binding.rvSearchResultList.adapter = searchAdapter

        searchAdapter = SearchAdapter()
        binding.rvSearchResultList.layoutManager = LinearLayoutManager(this)
        binding.rvSearchResultList.adapter = searchAdapter

        searchResults = profileViewModel.allProfiles

        searchResults.observe(this, { searchResultsList ->
            searchAdapter.submitList(searchResultsList)
        })
    }

    private fun initSearchView() {
        binding.search.isSubmitButtonEnabled = true
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
