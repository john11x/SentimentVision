package com.example.sentimentvision.ui.therapist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sentimentvision.R
import com.google.android.material.textview.MaterialTextView

data class TherapistPatient(
    val name: String,
    val sentiment: String,
    val summary: String
)

class TherapistAdapter(
    private val patients: List<TherapistPatient>,
    private val onClick: (TherapistPatient) -> Unit
) : RecyclerView.Adapter<TherapistAdapter.PatientViewHolder>() {

    inner class PatientViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: MaterialTextView = itemView.findViewById(R.id.textPatientName)
        val sentiment: MaterialTextView = itemView.findViewById(R.id.textSentiment)
        val summary: MaterialTextView = itemView.findViewById(R.id.textSummary)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatientViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_therapist_patient, parent, false)
        return PatientViewHolder(view)
    }

    override fun onBindViewHolder(holder: PatientViewHolder, position: Int) {
        val patient = patients[position]
        holder.name.text = patient.name
        holder.sentiment.text = patient.sentiment
        holder.summary.text = patient.summary
        holder.itemView.setOnClickListener { onClick(patient) }
    }

    override fun getItemCount() = patients.size
}
