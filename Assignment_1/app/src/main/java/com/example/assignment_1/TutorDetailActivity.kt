package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class TutorDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var coursesRecyclerView: RecyclerView
    private lateinit var noCourses: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutor_detail)

        dbHelper = DatabaseHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
        supportActionBar?.title = "Tutor Details"

        val profile = findViewById<ImageView>(R.id.tutor_profile)
        val name = findViewById<TextView>(R.id.tutor_name)
        coursesRecyclerView = findViewById(R.id.recyclerViewTutorCourses)
        noCourses = findViewById(R.id.no_courses1)

        val tutorName = intent.getStringExtra("TUTOR_NAME") ?: "Unknown Tutor"
        name.text = tutorName

        val dbImage = dbHelper.getTutorImage(tutorName)

        if (dbImage != null) {
            profile.setImageBitmap(dbImage)
        } else {
            profile.setImageResource(R.drawable.placeholder_img)
        }

        loadTutorCourses(tutorName)
    }

    private fun loadTutorCourses(tutorName: String) {
        val courses = dbHelper.getCoursesByAuthor(tutorName)

        if (courses.isEmpty()) {
            coursesRecyclerView.visibility = View.GONE
            noCourses.visibility = View.VISIBLE
        } else {
            coursesRecyclerView.visibility = View.VISIBLE
            noCourses.visibility = View.GONE

            val adapter = CourseAdapter(courses)

            adapter.onItemClick = { course ->
                val intent = Intent(this, CourseDetailActivity::class.java)
                intent.putExtra("COURSE_ID", course.id)

                val studentName = getIntent().getStringExtra("STUDENT_NAME")
                intent.putExtra("STUDENT_NAME", studentName)

                startActivity(intent)
            }

            coursesRecyclerView.layoutManager = LinearLayoutManager(this)
            coursesRecyclerView.adapter = adapter
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}