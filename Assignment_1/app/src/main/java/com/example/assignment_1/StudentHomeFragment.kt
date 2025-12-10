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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_home, container, false)
        dbHelper = DatabaseHelper(requireContext())
        featuredTutorsRecyclerView = view.findViewById(R.id.recyclerViewFeaturedTutors)
        coursesRecyclerView = view.findViewById(R.id.recyclerViewCourses)

        setupFeaturedTutorsRecyclerView()
        return view
    }

    override fun onResume() {
        super.onResume()
        setupCoursesRecyclerView()
    }

    private fun setupFeaturedTutorsRecyclerView() {
        val tutors = dbHelper.getAllTutors()

        val adapter = FeaturedTutorAdapter(tutors)
        featuredTutorsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        featuredTutorsRecyclerView.adapter = adapter

        if (tutors.isEmpty()) {

        }
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