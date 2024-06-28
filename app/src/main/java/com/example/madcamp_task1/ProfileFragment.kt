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
//import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.madcamp_task1.adapter.ProfileRvAdapter
import com.example.madcamp_task1.data.Profile
import com.example.madcamp_task1.databinding.FragmentProfileBinding


class ProfileFragment : Fragment () {

    private lateinit var binding: FragmentProfileBinding
    private var profileList: ArrayList<Profile> = ArrayList()
    companion object {
        private const val READ_CONTACTS_PERMISSION_CODE = 1
        const val REQUEST_CODE_GROUP_NAME = 1
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(layoutInflater)

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
        profileList = profileList.distinct() as ArrayList<Profile>
        profileList.sortBy { it.name }

        val adapter = ProfileRvAdapter(profileList, object: ProfileRvAdapter.OnItemClickListener {
            override fun onItemClick(profile: Profile) {
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
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_GROUP_NAME && resultCode == Activity.RESULT_OK) {
            val phonenum = data?.getStringExtra("phoneNum")
            val newgroupname = data?.getStringExtra("groupName")
            if (newgroupname != null && phonenum != null) {
                // phoneNum에 해당하는 프로필을 찾고 그룹 이름을 업데이트
                val profile = profileList.find { it.phonenum == phonenum }
                profile?.let {
                    it.groupname = newgroupname
                    binding.profileRecyclerView.adapter?.notifyDataSetChanged() // Adapter에 데이터 변경을 알림
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

                    profileList.add(Profile(name, normalizePhoneNumber(number), "None"))
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
        // Remove all non-numeric characters
        val normalized = phoneNumber.replace("[^\\d]".toRegex(), "")

        // Format to "010-xxxx-xxxx"
        return if (normalized.length == 11) {
            "${normalized.substring(0, 3)}-${normalized.substring(3, 7)}-${normalized.substring(7)}"
        } else {
            // If the length is not exactly 11 digits, return as it is (handle edge cases)
            normalized
        }
    }
}