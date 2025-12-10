package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.TextView
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
    private lateinit var tutorCoursesRecyclerView: RecyclerView
    private lateinit var noSessions: TextView
    private lateinit var noCourses: TextView
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
        tutorCoursesRecyclerView = view.findViewById(R.id.tutorCoursesRecyclerView)
        noSessions = view.findViewById(R.id.no_sessions)
        noCourses = view.findViewById(R.id.no_courses)

        setupTutorSessionsRecyclerView()

        val add = view.findViewById<FloatingActionButton>(R.id.addButton)
        add.setOnClickListener {
            val currentAuthorName = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown Tutor"
            val intent = Intent(requireContext(), AddCourseActivity::class.java)
            intent.putExtra("AUTHOR_NAME_PASSED", currentAuthorName)
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

                    R.id.action_profile -> {
                        val username = requireActivity().intent.getStringExtra("USER_USERNAME") ?: ""

                        val intent = Intent(requireContext(), ProfileActivity::class.java)
                        intent.putExtra("USER_USERNAME", username)
                        startActivity(intent)
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

        val seeAllButton = view.findViewById<TextView>(R.id.see_all_sessions)

        seeAllButton.setOnClickListener {
            val currentAuthor = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown Tutor"
            val intent = Intent(requireContext(), AllSessionsActivity::class.java)
            intent.putExtra("TUTOR_NAME", currentAuthor)
            startActivity(intent)
        }
    }

    private fun setupTutorSessionsRecyclerView() {
        val currentTutor = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown"

        val sessions = dbHelper.getTutorSessions(currentTutor, isUpcoming = true)

        if (sessions.isEmpty()) {
            tutorSessionsRecyclerView.visibility = View.GONE
            noSessions.visibility = View.VISIBLE
        } else {
            tutorSessionsRecyclerView.visibility = View.VISIBLE
            noSessions.visibility = View.GONE

            val adapter = SessionAdapter(sessions, UserType.TUTOR)

            adapter.onItemClick = { session ->
                showSessionActionDialog(session)
            }

            tutorSessionsRecyclerView.layoutManager = LinearLayoutManager(context)
            tutorSessionsRecyclerView.adapter = adapter
        }
    }

    private fun showSessionActionDialog(session: Session) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(requireContext())
        builder.setTitle("Manage Session")
        builder.setMessage("Student: ${session.studentName}\nCourse: ${session.subject}\nDate: ${session.date}")

        builder.setPositiveButton("Confirm") { dialog, _ ->
            dbHelper.updateSessionStatus(session.id, "Confirmed")
            Toast.makeText(context, "Session Confirmed!", Toast.LENGTH_SHORT).show()
            setupTutorSessionsRecyclerView()
        }

        builder.setNegativeButton("Reject") { dialog, _ ->
            dbHelper.updateSessionStatus(session.id, "Cancelled")
            Toast.makeText(context, "Session Rejected.", Toast.LENGTH_SHORT).show()
            setupTutorSessionsRecyclerView()
        }

        builder.setNeutralButton("Back", null)

        builder.show()
    }

    override fun onResume() {
        super.onResume()
        loadMyCourses()
        updateEarnings()
    }

    private fun loadMyCourses() {

        val currentAuthor = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown Tutor"

        val myCourses = dbHelper.getCoursesByAuthor(currentAuthor)

        if (myCourses.isEmpty()) {
            tutorCoursesRecyclerView.visibility = View.GONE
            noCourses.visibility = View.VISIBLE
        } else {
            tutorCoursesRecyclerView.visibility = View.VISIBLE
            noCourses.visibility = View.GONE

            val adapter = CourseAdapter(myCourses)

            adapter.onItemClick = { course ->
                val intent = Intent(requireContext(), AddCourseActivity::class.java)
                intent.putExtra("COURSE_ID", course.id)
                intent.putExtra("AUTHOR_NAME_PASSED", currentAuthor)
                startActivity(intent)
            }

            tutorCoursesRecyclerView.layoutManager = LinearLayoutManager(context)
            tutorCoursesRecyclerView.adapter = adapter
        }
    }

    private fun updateEarnings() {
        val currentAuthor = requireActivity().intent.getStringExtra("USER_FULLNAME") ?: "Unknown Tutor"
        val totalEarnings = dbHelper.calculateTutorEarnings(currentAuthor)
        val earnings = view?.findViewById<TextView>(R.id.tutor_earnings)

        earnings?.text = String.format("RM %.2f", totalEarnings)
    }
}