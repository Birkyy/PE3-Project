package com.example.assignment_1

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SessionAdapter(
    private val sessions: List<Session>,
    private val userType: UserType
) : RecyclerView.Adapter<SessionAdapter.SessionViewHolder>() {

    var onItemClick: ((Session) -> Unit)? = null

    class SessionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val courseName: TextView = view.findViewById(R.id.session_course_name)
        val personName: TextView = view.findViewById(R.id.session_student_name)
        val date: TextView = view.findViewById(R.id.session_date)
        val time: TextView = view.findViewById(R.id.session_time)
        val status: TextView = view.findViewById(R.id.session_status)
        val btnManage: Button = view.findViewById(R.id.btn_manage_session)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SessionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_tutor_session, parent, false)
        return SessionViewHolder(view)
    }

    override fun onBindViewHolder(holder: SessionViewHolder, position: Int) {
        val session = sessions[position]

        holder.courseName.text = session.subject
        holder.date.text = session.date
        holder.time.text = session.time
        holder.status.text = session.status

        if (userType == UserType.TUTOR) {
            holder.personName.text = "with ${session.studentName}"
            holder.btnManage.text = "Manage Session"
        } else {
            holder.personName.text = "with Tutor ${session.tutorName}"
            holder.btnManage.text = "View Details"
        }

        when(session.status) {
            "Confirmed" -> {
                holder.status.setTextColor(Color.parseColor("#4CAF50")) // Green
                holder.status.setBackgroundColor(Color.parseColor("#1A4CAF50"))
            }
            "Cancelled" -> {
                holder.status.setTextColor(Color.parseColor("#F44336")) // Red
                holder.status.setBackgroundColor(Color.parseColor("#1AF44336"))
            }
            else -> {
                holder.status.setTextColor(Color.parseColor("#FFA726")) // Orange
                holder.status.setBackgroundColor(Color.parseColor("#1AFFA726"))
            }
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(session)
        }

        holder.btnManage.setOnClickListener {
            onItemClick?.invoke(session)
        }
    }

    override fun getItemCount() = sessions.size
}