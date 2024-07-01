package com.example.madcamp_task1

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class GameEventActivity : AppCompatActivity() {

    private lateinit var editTextEventTitle: EditText
    private lateinit var buttonSaveEvent: Button
    private lateinit var selectedDate: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gameevent)

        editTextEventTitle = findViewById(R.id.editTextEventTitle)
        buttonSaveEvent = findViewById(R.id.buttonSaveEvent)

        selectedDate = intent.getStringExtra("selectedDate") ?: ""

        buttonSaveEvent.setOnClickListener {
            val eventTitle = editTextEventTitle.text.toString()
            val resultIntent = Intent()
            resultIntent.putExtra("eventTitle", eventTitle)
            resultIntent.putExtra("selectedDate", selectedDate)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}
