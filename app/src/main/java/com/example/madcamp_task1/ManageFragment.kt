package com.example.madcamp_task1

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.SearchAdapter
import com.example.madcamp_task1.databinding.FragmentManageBinding
import com.example.madcamp_task1.roomdb.*

class ManageFragment : Fragment() {

    private lateinit var binding : FragmentManageBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var searchResults : LiveData<List<Profile>>
    private lateinit var searchAdapter: SearchAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageBinding.inflate(layoutInflater)
        initializeViews()
        initSearchView()
        return binding.root
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
        binding.rvSearchResultList.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearchResultList.adapter = searchAdapter

        searchResults = profileViewModel.allProfiles

        searchResults.observe(viewLifecycleOwner, { searchResultsList ->
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

        profileViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, { results ->
            Log.d("data", results.toString())
            results?.let {
                searchAdapter.submitList(results)
            }
        })
    }
}