package com.example.assignment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class UpcomingSessionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_session_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.sessionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val upcomingSessions = listOf(
            Session("s1", "Dr. Johny", "Mathematics (Algebra)", "14 November 2025", "4:00 PM", "Confirmed", ""),
            Session("s2", "Ms. Alice", "English (Grammar)", "15 November 2025", "10:00 AM", "Confirmed", ""),
            Session("s3", "Mr. Bob", "Physics (Mechanics)", "16 November 2025", "2:30 PM", "Pending", "")
        )

        val adapter = SessionAdapter(upcomingSessions, UserType.STUDENT)
        recyclerView.adapter = adapter

        return view
    }
}