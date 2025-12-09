package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class TutorFragment : Fragment(R.layout.fragment_tutor) {

    private lateinit var tutorSessionsRecyclerView: RecyclerView
    private lateinit var dbHelper: DatabaseHelper

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbHelper = DatabaseHelper(requireContext())

        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar

        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)

        tutorSessionsRecyclerView = view.findViewById(R.id.tutorSessionsRecyclerView)
        setupTutorSessionsRecyclerView()

        val add = view.findViewById<FloatingActionButton>(R.id.addButton)
        add.setOnClickListener {
            val intent = Intent(requireContext(), AddCourseActivity::class.java)
            startActivity(intent)
        }

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {

                    android.R.id.home -> {
                        val roleSelectionFragment = RoleSelectionFragment()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_container, roleSelectionFragment)
                            .addToBackStack(null)
                            .commit()
                        return true
                    }

                    R.id.action_notification -> {
                        Toast.makeText(requireContext(), "Notification Clicked", Toast.LENGTH_SHORT).show()
                        return true
                    }

                    R.id.action_contact -> {
                        val intent = Intent(requireContext(), ContactActivity::class.java)
                        startActivity(intent)
                        return true
                    }

                    R.id.action_exit -> {
                        activity?.finishAffinity()
                        return true
                    }

                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupTutorSessionsRecyclerView() {
        val sessions = listOf(
            Session("s1", "Mr. Caleb", "Advanced Mathematics", "14 Nov 2025", "4:00 PM", "Confirmed", "Wong Rui Bin"),
            Session("s2", "Mr. Caleb", "Advanced Mathematics", "15 Nov 2025", "10:00 AM", "Pending", "Jaden Smith"),
            Session("s3", "Mr. Caleb", "Advanced Mathematics", "16 Nov 2025", "2:00 PM", "Cancelled", "Jordan Lee"),
            Session("s4", "Mr. Caleb", "Advanced Mathematics", "10 Nov 2025", "3:00 PM", "Completed", "Alex Wong")
        )
        val adapter = SessionAdapter(sessions, UserType.TUTOR)
        tutorSessionsRecyclerView.layoutManager = LinearLayoutManager(context)
        tutorSessionsRecyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadCourses()
    }

    private fun loadCourses() {
        // FETCH FROM DATABASE
        val courseList = dbHelper.getAllCourses()

        // SETUP ADAPTER
        val adapter = CourseAdapter(courseList)

        // HANDLE EDIT CLICK
        adapter.onItemClick = { course ->
            val intent = Intent(requireContext(), AddCourseActivity::class.java)
            intent.putExtra("COURSE_ID", course.id)
            intent.putExtra("COURSE_TITLE", course.title)
            intent.putExtra("COURSE_DESC", course.description)
            intent.putExtra("COURSE_PRICE", course.price)
            startActivity(intent)
        }

        tutorSessionsRecyclerView.layoutManager = LinearLayoutManager(context)
        tutorSessionsRecyclerView.adapter = adapter
    }
}