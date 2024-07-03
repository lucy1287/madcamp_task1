package com.example.madcamp_task1

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.madcamp_task1.databinding.ActivityGroupchangeBinding
import com.example.madcamp_task1.roomdb.Event
import com.github.mikephil.charting.charts.RadarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.RadarData
import com.github.mikephil.charting.data.RadarDataSet
import com.github.mikephil.charting.data.RadarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class GroupChangeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGroupchangeBinding
    private lateinit var radarChart: RadarChart

    private var currentStrengthValue = 8
    private var currentAgilityValue = 8
    private var currentEnduranceValue = 8
    private var currentSkillValue = 8
    private var currentTeamworkValue = 8
    private lateinit var skills: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGroupchangeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val phonenum = intent.getStringExtra("phoneNum")
        val groupname = intent.getStringExtra("groupName")
        val skillsFromIntent = intent.getStringArrayListExtra("skills")

        binding.nameTv.text = name
        binding.phonenumTv.text = phonenum
        binding.groupnameTv.text = groupname

        if (skillsFromIntent?.isNotEmpty() == true) {
            currentStrengthValue = skillsFromIntent[0].toFloat().toInt()
            currentAgilityValue = skillsFromIntent[1].toFloat().toInt()
            currentEnduranceValue = skillsFromIntent[2].toFloat().toInt()
            currentSkillValue = skillsFromIntent[3].toFloat().toInt()
            currentTeamworkValue = skillsFromIntent[4].toFloat().toInt()
        }

        binding.submitButton.setOnClickListener {
            val newgroupname = binding.groupNameEditText.text.toString()
            val returnIntent = Intent().apply {
                putExtra("name", name)
                putExtra("phoneNum", phonenum)
                putExtra("groupName", newgroupname)
                putStringArrayListExtra("skills", ArrayList(skills))
            }
            setResult(Activity.RESULT_OK, returnIntent)
            finish()
        }

        binding.tvGraphChange.setOnClickListener {
            showGraphChangeDialog()
        }

        radarChart = binding.mapsearchdetailRadarChart
        skills = arrayListOf(
            currentStrengthValue.toString(),
            currentAgilityValue.toString(),
            currentEnduranceValue.toString(),
            currentSkillValue.toString(),
            currentTeamworkValue.toString()
        )
        makeChart(currentStrengthValue, currentAgilityValue, currentEnduranceValue, currentSkillValue, currentTeamworkValue)
    }

    private fun dataValue(strengthValue: Int, agilityValue: Int, enduranceValue: Int, skillValue: Int, teamworkValue: Int): ArrayList<RadarEntry> {
        val dataVals = ArrayList<RadarEntry>()
        dataVals.add(RadarEntry(strengthValue.toFloat()))
        dataVals.add(RadarEntry(agilityValue.toFloat()))
        dataVals.add(RadarEntry(enduranceValue.toFloat()))
        dataVals.add(RadarEntry(skillValue.toFloat()))
        dataVals.add(RadarEntry(teamworkValue.toFloat()))
        return dataVals
    }

    private fun makeChart(strengthValue: Int, agilityValue: Int, enduranceValue: Int, skillValue: Int, teamworkValue: Int) {
        val dataSet = RadarDataSet(dataValue(strengthValue, agilityValue, enduranceValue, skillValue, teamworkValue), "DATA")
        dataSet.color = Color.BLUE
        val data = RadarData()
        data.addDataSet(dataSet)
        val labels = arrayOf("근력", "민첩성", "지구력", "기술", "협동력")
        val xAxis: XAxis = radarChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        radarChart.data = data
        radarChart.invalidate()
    }

    private fun showGraphChangeDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_graph_change, null)

        val seekBarStrength: SeekBar = dialogView.findViewById(R.id.seekBar_strength)
        val textViewStrengthValue: TextView = dialogView.findViewById(R.id.textView_strength_value)
        val seekBarAgility: SeekBar = dialogView.findViewById(R.id.seekBar_agility)
        val textViewAgilityValue: TextView = dialogView.findViewById(R.id.textView_agility_value)
        val seekBarEndurance: SeekBar = dialogView.findViewById(R.id.seekBar_endurance)
        val textViewEnduranceValue: TextView = dialogView.findViewById(R.id.textView_endurance_value)
        val seekBarSkill: SeekBar = dialogView.findViewById(R.id.seekBar_skill)
        val textViewSkillValue: TextView = dialogView.findViewById(R.id.textView_skill_value)
        val seekBarTeamwork: SeekBar = dialogView.findViewById(R.id.seekBar_teamwork)
        val textViewTeamworkValue: TextView = dialogView.findViewById(R.id.textView_teamwork_value)

        seekBarStrength.progress = 10 - currentStrengthValue
        textViewStrengthValue.text = currentStrengthValue.toString()
        seekBarAgility.progress = 10 - currentAgilityValue
        textViewAgilityValue.text = currentAgilityValue.toString()
        seekBarEndurance.progress = 10 - currentEnduranceValue
        textViewEnduranceValue.text = currentEnduranceValue.toString()
        seekBarSkill.progress = 10 - currentSkillValue
        textViewSkillValue.text = currentSkillValue.toString()
        seekBarTeamwork.progress = 10 - currentTeamworkValue
        textViewTeamworkValue.text = currentTeamworkValue.toString()

        val seekBars = listOf(seekBarStrength to textViewStrengthValue, seekBarAgility to textViewAgilityValue,
            seekBarEndurance to textViewEnduranceValue, seekBarSkill to textViewSkillValue, seekBarTeamwork to textViewTeamworkValue)

        seekBars.forEach { (seekBar, textView) ->
            seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    textView.text = (10 - progress).toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }

        val dialog = AlertDialog.Builder(this)
            .setTitle("Adjust Skills")
            .setView(dialogView)
            .setPositiveButton("OK") { _, _ ->
                currentStrengthValue = 10 - seekBarStrength.progress
                currentAgilityValue = 10 - seekBarAgility.progress
                currentEnduranceValue = 10 - seekBarEndurance.progress
                currentSkillValue = 10 - seekBarSkill.progress
                currentTeamworkValue = 10 - seekBarTeamwork.progress

                skills = arrayListOf(
                    currentStrengthValue.toString(),
                    currentAgilityValue.toString(),
                    currentEnduranceValue.toString(),
                    currentSkillValue.toString(),
                    currentTeamworkValue.toString()
                )
                makeChart(currentStrengthValue, currentAgilityValue, currentEnduranceValue, currentSkillValue, currentTeamworkValue)
            }
            .setNegativeButton("Cancel", null)
            .create()

        dialog.show()
    }
}