package com.example.madcamp_task1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.madcamp_task1.databinding.ActivityGroupchangeBinding

class GroupChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupchangeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the profile data from the intent
        val name = intent.getStringExtra("name")
        val phonenum = intent.getStringExtra("phoneNum")
        val groupname = intent.getStringExtra("groupName")

        // Set the profile data to the views
        binding.nameTv.text = name
        binding.phonenumTv.text = phonenum
        binding.groupnameTv.text = groupname

        binding.submitButton.setOnClickListener {
            val newgroupname = binding.groupNameEditText.text.toString()
            val returnIntent = Intent().apply {
                putExtra("phoneNum", phonenum)
                putExtra("groupName", newgroupname)
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }
    }
}
