package com.example.assignment_1

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PastSessionsFragment : Fragment(R.layout.fragment_session_list) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var empty: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())
        recyclerView = view.findViewById(R.id.sessionsRecyclerView)
        empty = view.findViewById(R.id.emptySessions)
    }

    override fun onResume() {
        super.onResume()
        loadSessions()
    }

    private fun loadSessions() {
        val userName = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown"
        val role = requireActivity().intent.getStringExtra("SELECTED_ROLE") ?: "Student"

        val sessions = if (role == "Tutor") {
            dbHelper.getTutorSessions(userName, isUpcoming = false)
        } else {
            dbHelper.getStudentSessions(userName, isUpcoming = false)
        }

        if (sessions.isEmpty()) {
            recyclerView.visibility = View.GONE
            empty.visibility = View.VISIBLE
            empty.text = "No past sessions."
        } else {
            recyclerView.visibility = View.VISIBLE
            empty.visibility = View.GONE

            val userType = if (role == "Tutor") UserType.TUTOR else UserType.STUDENT
            val adapter = SessionAdapter(sessions, userType)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }
    }
}