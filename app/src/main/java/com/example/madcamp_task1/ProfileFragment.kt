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
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.ProfileRvAdapter
import com.example.madcamp_task1.roomdb.Profile
import com.example.madcamp_task1.databinding.FragmentProfileBinding
import com.example.madcamp_task1.roomdb.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private val profileViewModel: ProfileViewModel by viewModels()

    private val adapter by lazy {
        ProfileRvAdapter(object : ProfileRvAdapter.OnItemClickListener {
            override fun onItemClick(profile: Profile) {
                val intent = Intent(requireContext(), GroupChangeActivity::class.java).apply {
                    putExtra("name", profile.name)
                    putExtra("phoneNum", profile.phonenum)
                    putExtra("groupName", profile.groupname)
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
                    Toast.makeText(requireContext(), "Profile list is empty.", Toast.LENGTH_SHORT).show()
                }
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
                profileViewModel.getProfileByPhoneNum(phonenum).observe(viewLifecycleOwner) { profile ->
                    profile?.let {
                        it.groupname = if (newgroupname.isEmpty()) "None" else newgroupname
                        profileViewModel.updateProfile(it)
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
//                Toast.makeText(requireContext(), "Contacts found: ${it.count}.", Toast.LENGTH_SHORT).show()
                while (it.moveToNext()) {
                    val name = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                    val number = it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val profile = Profile(name = name, phonenum = normalizePhoneNumber(number), groupname = "None")
                    profileViewModel.insertProfile(profile)
                    Log.d("ProfileFragment", "Inserting profile: $profile")

                }
            }
            else {
                Log.d("ProfileFragment", "No contacts found")
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
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Log.d("ProfileFragment", "READ_CONTACTS permission granted")
                    loadContacts()
                } else {
                    // Permission denied, show message or handle accordingly
                    // For now, we simply close the app
                    Log.d("ProfileFragment", "READ_CONTACTS permission denied")
                    requireActivity().finish()
                }
            }
        }
    }

    private fun normalizePhoneNumber(phoneNumber: String): String {
        return phoneNumber.replace("[^0-9]".toRegex(), "")
    }
}
