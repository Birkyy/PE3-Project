package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentHomeFragment : Fragment() {

    private lateinit var featuredTutorsRecyclerView: RecyclerView
    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    private lateinit var cardNextSession: View
    private lateinit var noNextSession: TextView
    private lateinit var subject: TextView
    private lateinit var tutor: TextView
    private lateinit var date: TextView
    private lateinit var time: TextView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_home, container, false)
        dbHelper = DatabaseHelper(requireContext())
        featuredTutorsRecyclerView = view.findViewById(R.id.recyclerViewFeaturedTutors)
        coursesRecyclerView = view.findViewById(R.id.recyclerViewCourses)

        cardNextSession = view.findViewById(R.id.card_next_session)
        noNextSession = view.findViewById(R.id.no_next_session)
        subject = view.findViewById(R.id.next_session_subject)
        tutor = view.findViewById(R.id.next_session_tutor)
        date = view.findViewById(R.id.next_session_date)
        time = view.findViewById(R.id.next_session_time)

        setupFeaturedTutorsRecyclerView()
        return view
    }

    override fun onResume() {
        super.onResume()
        setupCoursesRecyclerView()
        loadNextSession()
    }

    private fun loadNextSession() {
        val currentStudent = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown"

        val nextSession = dbHelper.getNextUpcomingSession(currentStudent)

        if (nextSession != null) {
            cardNextSession.visibility = View.VISIBLE
            noNextSession.visibility = View.GONE
            subject.text = nextSession.subject
            tutor.text = "with ${nextSession.tutorName}"
            date.text = nextSession.date
            time.text = nextSession.time
        } else {
            cardNextSession.visibility = View.GONE
            noNextSession.visibility = View.VISIBLE
        }
    }

    private fun setupFeaturedTutorsRecyclerView() {
        val tutors = dbHelper.getAllTutors()
        val adapter = FeaturedTutorAdapter(tutors)

        adapter.onItemClick = { tutor ->
            val intent = Intent(requireContext(), TutorDetailActivity::class.java)
            intent.putExtra("TUTOR_NAME", tutor.name)
            val currentStudent = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown"
            intent.putExtra("STUDENT_NAME", currentStudent)

            startActivity(intent)
        }

        featuredTutorsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        featuredTutorsRecyclerView.adapter = adapter

    }

    private fun setupCoursesRecyclerView() {
        val courses = dbHelper.getAllCourses()
        val adapter = CourseAdapter(courses)

        adapter.onItemClick = { course ->
            val intent = Intent(requireContext(), CourseDetailActivity::class.java)
            intent.putExtra("COURSE_ID", course.id)

            val studentName = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Student"
            intent.putExtra("STUDENT_NAME", studentName)

            startActivity(intent)
        }

        coursesRecyclerView.layoutManager = LinearLayoutManager(context)
        coursesRecyclerView.adapter = adapter
    }
}