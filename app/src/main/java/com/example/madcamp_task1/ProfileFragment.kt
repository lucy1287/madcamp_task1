package com.example.madcamp_task1

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.ProfileRvAdapter
import com.example.madcamp_task1.data.Profile
import com.example.madcamp_task1.data.AppDatabase
import com.example.madcamp_task1.databinding.FragmentProfileBinding
import com.example.madcamp_task1.repository.ProfileRepository
import com.example.madcamp_task1.viewmodel.ProfileViewModel
import com.example.madcamp_task1.viewmodel.ProfileViewModelFactory

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels {
        val database = AppDatabase.getDatabase(requireContext())
        val repository = ProfileRepository(database.profileDao())
        ProfileViewModelFactory(repository)
    }
    private var profileList: ArrayList<Profile> = ArrayList()
    // For data manage

    companion object {
        private const val READ_CONTACTS_PERMISSION_CODE = 1
        const val REQUEST_CODE_GROUP_NAME = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)

        // Getting Permission for contact data
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // Permission is not granted, request the permission
            this.requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                READ_CONTACTS_PERMISSION_CODE
            )
        } else {
            // Permission has already been granted
            loadContacts()
        }

        initializeViews()

        return binding.root
    }

    private fun initializeViews(){
        profileList = profileList.distinct().toMutableList() as ArrayList<Profile>
        profileList.sortBy { it.name }

        val adapter = ProfileRvAdapter(profileList, object: ProfileRvAdapter.OnItemClickListener {
            override fun onItemClick(profile: Profile) {
                // If user clicks item in Recyclerview, pop up new page with user info
                val intent = Intent(requireContext(), GroupChangeActivity::class.java).apply {
                    putExtra("name", profile.name)
                    putExtra("phoneNum", profile.phonenum)
                    putExtra("groupName", profile.groupname)
                }
                startActivityForResult(intent, REQUEST_CODE_GROUP_NAME)
            }
        })

        binding.profileRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.profileRecyclerView.adapter = adapter

        profileViewModel.allProfiles.observe(viewLifecycleOwner) { profiles ->
            profiles?.let {
                profileList.clear()
                profileList.addAll(it)
                adapter.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GROUP_NAME && resultCode == Activity.RESULT_OK) {
            val phonenum = data?.getStringExtra("phoneNum")
            val newgroupname = data?.getStringExtra("groupName")
            if (newgroupname != null && phonenum != null) {
                // find profile for phoneNum and update groupname
                val profile = profileList.find { it.phonenum == phonenum }
                profile?.let {
                    it.groupname = newgroupname
                    profileViewModel.updateProfile(it)
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun loadContacts() {
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
                    val name =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number =
                        it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val profile = Profile(name = name, phonenum = normalizePhoneNumber(number), groupname = "None")
                    profileViewModel.insertProfile(profile)
                }
            }
            it.close()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            READ_CONTACTS_PERMISSION_CODE -> {
                // If request is cancelled, the result arrays are empty.
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, load contacts
                    loadContacts()
                } else {
                    // Permission denied, show message or handle accordingly
                    // For now, we simply close the app
                    requireActivity().finish()
                }
                return
            }
            // Add more cases as necessary for other permissions
        }
    }

    private fun normalizePhoneNumber(phoneNumber: String): String {
        // Remove all non-numeric characters from the phone number
        return phoneNumber.replace("[^0-9]".toRegex(), "")
    }
}
