package com.example.assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SessionAdapter(private val sessions: List<Session>, private val userType: UserType) :
    RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_session_card, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]


        if (userType == UserType.STUDENT) {

            holder.studentNameTextView.text = "with ${session.tutorName}"
        } else {

            if (session.studentName.isNullOrEmpty()) {
                holder.studentNameTextView.text = "No student assigned"
            } else {
                holder.studentNameTextView.text = "with ${session.studentName}"
            }
        }


        holder.subjectTextView.text = session.subject
        holder.dateTextView.text = session.date
        holder.timeTextView.text = session.time
        holder.statusTextView.text = session.status

        when (session.status) {
            "Confirmed" -> holder.statusTextView.setBackgroundResource(R.drawable.status_background_green)
            "Completed" -> holder.statusTextView.setBackgroundResource(R.drawable.status_background_green)
            "Cancelled" -> holder.statusTextView.setBackgroundResource(R.drawable.status_background_red)
            "Pending" -> holder.statusTextView.setBackgroundResource(R.drawable.status_background_orange)
            else -> holder.statusTextView.setBackgroundResource(R.drawable.status_background_green)
        }
    }

    override fun getItemCount(): Int = sessions.size

    class SessionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val subjectTextView: TextView = itemView.findViewById(R.id.subjectTextView)
        val studentNameTextView: TextView = itemView.findViewById(R.id.studentNameTextView) // This view is used for both
        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val timeTextView: TextView = itemView.findViewById(R.id.timeTextView)
        val statusTextView: TextView = itemView.findViewById(R.id.statusTextView)
    }
}