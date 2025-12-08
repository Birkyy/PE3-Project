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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_home, container, false)

        featuredTutorsRecyclerView = view.findViewById(R.id.recyclerViewFeaturedTutors)
        coursesRecyclerView = view.findViewById(R.id.recyclerViewCourses)

        setupFeaturedTutorsRecyclerView()
        setupCoursesRecyclerView()
        return view
    }

    private fun setupFeaturedTutorsRecyclerView() {
        val tutors = listOf(
            Tutor("t1", "Mr. Caleb", "Piano", R.mipmap.tutor_placeholder_round),
            Tutor("t2", "Mr. Jaden", "Mathematic", R.mipmap.tutor_placeholder_1_round),
            Tutor("t3", "Mr. Jordan", "English", R.mipmap.tutor_placeholder_2_round),
            Tutor("t4", "Mr. Wong", "Physics", R.mipmap.tutor_placeholder_3_round)
        )

        val adapter = FeaturedTutorAdapter(tutors)
        featuredTutorsRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        featuredTutorsRecyclerView.adapter = adapter

    }

    private fun setupCoursesRecyclerView() {
        val courses = listOf(
            Course(
                "c1",
                "Advanced Mathematics",
                "Ms. Sarah Chen",
                "hi",
                25.00,
                R.mipmap.math_course_foreground
            ),
            Course(
                "c2",
                "Physics Fundamentals",
                "Mr. Ahmad Ibrahim",
                "hi",
                30.00,
                R.mipmap.physic_course_foreground
            ),
            Course(
                "c3",
                "English Language & Literature",
                "Ms. Emily Watson",
                "hi",
                20.00,
                R.mipmap.english_course_foreground
            ),
        )

        val adapter = CourseAdapter(courses)
        coursesRecyclerView.layoutManager = LinearLayoutManager(context)
        coursesRecyclerView.adapter = adapter
    }
}