package com.example.assignment_1

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerUsers: RecyclerView
    private lateinit var recyclerCourses: RecyclerView
    private lateinit var btnShowUsers: Button
    private lateinit var btnShowCourses: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        dbHelper = DatabaseHelper(this)

        recyclerUsers = findViewById(R.id.recycler_admin_users)
        recyclerCourses = findViewById(R.id.recycler_admin_courses)
        btnShowUsers = findViewById(R.id.btn_show_users)
        btnShowCourses = findViewById(R.id.btn_show_courses)
        val btnLogout = findViewById<Button>(R.id.btn_admin_logout)

        recyclerUsers.layoutManager = LinearLayoutManager(this)
        recyclerCourses.layoutManager = LinearLayoutManager(this)

        loadUsers()

        btnShowUsers.setOnClickListener {
            showUsersView()
        }

        btnShowCourses.setOnClickListener {
            showCoursesView()
        }

        btnLogout.setOnClickListener {
            val intent = Intent(this, ModeSelectionActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun showUsersView() {
        recyclerUsers.visibility = View.VISIBLE
        recyclerCourses.visibility = View.GONE

        btnShowUsers.setBackgroundColor(Color.parseColor("#4b886a"))
        btnShowCourses.setBackgroundColor(Color.parseColor("#888888"))

        loadUsers()
    }

    private fun showCoursesView() {
        recyclerUsers.visibility = View.GONE
        recyclerCourses.visibility = View.VISIBLE

        btnShowUsers.setBackgroundColor(Color.parseColor("#888888"))
        btnShowCourses.setBackgroundColor(Color.parseColor("#4b886a"))

        loadCourses()
    }

    private fun loadUsers() {
        val users = dbHelper.getAllUsers()
        val adapter = AdminUserAdapter(users) { userId ->
            confirmDelete("User") {
                dbHelper.deleteUser(userId)
                loadUsers()
                Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.onItemClick = { user ->
            val intent = Intent(this, ProfileActivity::class.java)
            intent.putExtra("USER_USERNAME", user.username)
            startActivity(intent)
        }
        recyclerUsers.adapter = adapter
    }

    private fun loadCourses() {
        val courses = dbHelper.getAllCourses()
        val adapter = AdminCourseAdapter(courses) { courseId ->
            confirmDelete("Course") {
                dbHelper.deleteCourse(courseId)
                loadCourses()
                Toast.makeText(this, "Course deleted", Toast.LENGTH_SHORT).show()
            }
        }

        adapter.onItemClick = { course ->
            val intent = Intent(this, CourseDetailActivity::class.java)
            intent.putExtra("COURSE_ID", course.id)
            intent.putExtra("STUDENT_NAME", "Administrator")
            startActivity(intent)
        }
        recyclerCourses.adapter = adapter
    }

    private fun confirmDelete(itemType: String, onConfirm: () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("Delete $itemType")
            .setMessage("Are you sure you want to delete this $itemType? This cannot be undone.")
            .setPositiveButton("Delete") { _, _ -> onConfirm() }
            .setNegativeButton("Cancel", null)
            .show()
    }
}