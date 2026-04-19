package com.example.sentimentvision.ui.therapist

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.sentimentvision.R
import com.example.sentimentvision.data.DatabaseHelper
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.textview.MaterialTextView

class TherapistViewPatientActivity : AppCompatActivity() {

    private lateinit var chart: LineChart
    private lateinit var dbHelper: DatabaseHelper
    private lateinit var nameText: MaterialTextView
    private lateinit var sentimentText: MaterialTextView
    private lateinit var summaryText: MaterialTextView
    private lateinit var btnAddAppointment: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_therapist_view_patient)

        chart = findViewById(R.id.lineChart)
        nameText = findViewById(R.id.textName)
        sentimentText = findViewById(R.id.textSentiment)
        summaryText = findViewById(R.id.textSummary)
        btnAddAppointment = findViewById(R.id.btnAddAppointment)

        dbHelper = DatabaseHelper(this)

        val name = intent.getStringExtra("name")
        val sentiment = intent.getStringExtra("sentiment")
        val summary = intent.getStringExtra("summary")

        nameText.text = name
        sentimentText.text = "Current Sentiment: $sentiment"
        summaryText.text = summary

        loadChartData()

        btnAddAppointment.setOnClickListener {
            showAppointmentDialog()
        }
    }

    private fun loadChartData() {
        val userId = 1 // Replace with actual mapped user ID
        val entries = dbHelper.getUserSentiments(userId)

        val chartEntries = entries.mapIndexed { index, e ->
            val y = when (e.sentiment.uppercase()) {
                "POSITIVE" -> 2f
                "NEUTRAL" -> 1f
                "NEGATIVE" -> 0f
                else -> 1f
            }
            Entry(index.toFloat(), y)
        }

        val dataSet = LineDataSet(chartEntries, "Sentiment Trend").apply {
            color = getColor(R.color.teal_700)
            circleRadius = 4f
            lineWidth = 2f
            setDrawValues(false)
        }

        val lineData = LineData(dataSet)
        chart.data = lineData
        chart.description = Description().apply {
            text = "Weekly Sentiment Pattern"
        }
        chart.invalidate()
    }

    private fun showAppointmentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_appointment, null)
        val dateField = dialogView.findViewById<EditText>(R.id.editDate)
        val notesField = dialogView.findViewById<EditText>(R.id.editNotes)

        AlertDialog.Builder(this)
            .setTitle("Add Appointment")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val date = dateField.text.toString()
                val notes = notesField.text.toString()
                if (date.isNotEmpty()) {
                    dbHelper.addAppointment(1, date, notes)
                    Toast.makeText(this, "Appointment added", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Date required", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
