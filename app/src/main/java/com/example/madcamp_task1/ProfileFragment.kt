package com.example.madcamp_task1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.ProfileRvAdapter
import com.example.madcamp_task1.databinding.FragmentProfileBinding
import com.example.madcamp_task1.roomdb.Game
import com.example.madcamp_task1.roomdb.Profile
import com.example.madcamp_task1.roomdb.ProfileViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.prolificinteractive.materialcalendarview.CalendarDay
import org.threeten.bp.LocalDate

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()
    private var currentProfile: Profile? = null

    private val adapter by lazy {
        ProfileRvAdapter(object : ProfileRvAdapter.OnItemClickListener {
            override fun onItemClick(profile: Profile) {
                currentProfile = profile
                val intent = Intent(requireContext(), GroupChangeActivity::class.java).apply {
                    putExtra("name", profile.name)
                    putExtra("phoneNum", profile.phonenum)
                    putExtra("groupName", profile.groupname)
                    putStringArrayListExtra("skills", ArrayList(profile.skills.map { it.toString()}))
                }
                startActivityForResult(intent, REQUEST_CODE_GROUP_NAME)
            }
        })
    }

    companion object {
        private const val READ_CONTACTS_PERMISSION_CODE = 1
        const val REQUEST_CODE_GROUP_NAME = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // Getting Permission for contact data
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d("ProfileFragment", "Requesting READ_CONTACTS permission")
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_CODE
            )
        } else {
            Log.d("ProfileFragment", "READ_CONTACTS permission already granted")
            loadContacts()
        }

        initializeViews()

        return binding.root
    }

    private fun initializeViews() {
        binding.profileRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.profileRecyclerView.adapter = adapter

        profileViewModel.allProfiles.observe(viewLifecycleOwner) { profiles ->
            profiles?.let {
                adapter.submitList(it)

                if (it.isEmpty()) {
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Observe LiveData for profile updates
        profileViewModel.allProfiles.observe(viewLifecycleOwner) { profiles ->
            profiles?.let {
                adapter.submitList(it)
            }
        }

        // 데이터베이스에서 프로필 로드
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            == PackageManager.PERMISSION_GRANTED) {
            loadContacts()
        }

        initSearchView()
    }

    private fun observeProfiles() {
        profileViewModel.allProfiles.observe(viewLifecycleOwner, Observer { profiles ->
            profiles?.let {
                // Sort profiles by name
                val sortedProfiles = profiles.sortedBy { it.name }
                adapter.submitList(sortedProfiles)
            }
        })
    }

    private fun initSearchView() {
        binding.searchView.apply {
            isSubmitButtonEnabled = false // optional: change if needed
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        searchDatabase(newText)
                    }
                    else {
                        observeProfiles() // Show sorted list when search view is empty
                    }
                    scrollToTop()
                    return true
                }
            })
        }
    }

    private fun searchDatabase(query: String) {
        if (query.isNotBlank()) {
            val searchQuery = "%$query%"
            profileViewModel.searchDatabase(searchQuery).observe(viewLifecycleOwner, Observer { results ->
                results?.let {
                    adapter.submitList(results)
                }
            })
        } else {
            observeProfiles() // Show sorted list when search query is empty
        }
    }

    private fun scrollToTop() {
        if (adapter.itemCount > 0) {
            binding.profileRecyclerView.smoothScrollToPosition(0)
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GROUP_NAME && resultCode == Activity.RESULT_OK) {
            val phonenum = data?.getStringExtra("phoneNum")
            val newgroupname = data?.getStringExtra("groupName")
            val skills = data?.getStringArrayListExtra("skills")
            if (phonenum != null) {
                currentProfile?.let {
                    it.groupname = if (newgroupname == null) "없음" else newgroupname
                    it.skills = skills?.map { skill -> skill.toFloat() } as ArrayList<Float>
                    profileViewModel.updateProfile(it)

                    // 변경된 프로필의 위치를 찾아서 RecyclerView 업데이트
                    val position = adapter.currentList.indexOfFirst { it.phonenum == phonenum }
                    if (position != -1) {
                        adapter.notifyItemChanged(position, newgroupname)
                    }
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun loadContacts() {
        Log.d("ProfileFragment", "loadContacts called")
        val contentResolver: ContentResolver = requireActivity().contentResolver
        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )

        cursor?.let {
            if (it.count > 0) {
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    // 기본 값을 가진 프로필 생성
                    val defaultProfile = Profile(
                        name = name,
                        phonenum =normalizePhoneNumber(number),
                        groupname = "없음",
                        skills = arrayListOf(8f, 8f, 8f, 8f, 8f)
                    )

                    // Room 데이터베이스에서 프로필 가져오기
                    profileViewModel.getProfileByPhoneNum(number).observe(viewLifecycleOwner) { existingProfile ->
                        if (existingProfile == null) {
                            // 데이터베이스에 프로필이 없으면 새로 삽입
                            profileViewModel.insertProfile(defaultProfile)
                            Log.d("ProfileFragment 확인", "Inserting default profile: $defaultProfile")
                        } else {
                            // 이미 존재하는 경우 아무 작업도 필요 없음
                            Log.d("ProfileFragment 확인", "Profile already exists: $existingProfile")
                        }
                    }

                    //val profile = Profile(name = name, phonenum = normalizePhoneNumber(number), groupname = "없음", skills = skills)
                    //profileViewModel.insertProfile(profile)
                    //Log.d("ProfileFragment", "Inserting profile: $profile")
                }
            } else {
                Log.d("ProfileFragment", "No contacts found")
            }
            it.close()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        when (requestCode) {
            READ_CONTACTS_PERMISSION_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("ProfileFragment", "READ_CONTACTS permission granted")
                    loadContacts()
                } else {
                    Log.d("ProfileFragment", "READ_CONTACTS permission denied")
                    requireActivity().finish()
                }
            }
        }
    }

    private fun normalizePhoneNumber(phoneNumber: String): String {
        // Remove all non-numeric characters
        val cleanNumber = phoneNumber.replace("[^0-9]".toRegex(), "")

        // Check if the phone number has 11 digits
        if (cleanNumber.length == 11) {
            return cleanNumber.substring(0, 3) + "-" + cleanNumber.substring(3, 7) + "-" + cleanNumber.substring(7)
        }
        else if (cleanNumber.length == 10){
            return cleanNumber.substring(0, 2) + "-" + cleanNumber.substring(2, 6) + "-" + cleanNumber.substring(7)
        }
        else {
            return cleanNumber
        }
    }
}
