package com.example.sentimentvision.ui.therapist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.sentimentvision.R
import com.example.sentimentvision.data.model.UserEntity
import com.google.android.material.textview.MaterialTextView

class TherapistUserAdapter(
    private val users: List<UserEntity>,
    private val onClick: (UserEntity) -> Unit
) : RecyclerView.Adapter<TherapistUserAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: MaterialTextView = view.findViewById(R.id.textUserName)
        val email: MaterialTextView = view.findViewById(R.id.textUserEmail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user_row, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = users[position]
        holder.name.text = user.displayName ?: user.username
        holder.email.text = user.username
        holder.itemView.setOnClickListener { onClick(user) }
    }

    override fun getItemCount() = users.size
}