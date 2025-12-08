package com.example.assignment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PastSessionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_session_list, container, false)

        val recyclerView: RecyclerView = view.findViewById(R.id.sessionsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val pastSessions = listOf(
            Session("p1", "Dr. Emily", "Chemistry (Organic)", "01 October 2025", "1:00 PM", "Completed", ""),
            Session("p2", "Mr. David", "History (World War II)", "20 September 2025", "3:00 PM", "Completed", ""),
            Session("p3", "Ms. Grace", "Biology (Genetics)", "05 August 2025", "11:00 AM", "Cancelled", "")
        )

        val adapter = SessionAdapter(pastSessions, UserType.STUDENT)
        recyclerView.adapter = adapter

        return view
    }
}