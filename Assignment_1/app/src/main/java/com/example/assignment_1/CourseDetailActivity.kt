package com.example.assignment_1

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CourseDetailActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var courseId: String? = null
    private var courseTitle: String? = null
    private var tutorName: String? = null
    private lateinit var duration: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course_detail)

        dbHelper = DatabaseHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
        supportActionBar?.title = "Course Details"

        val image = findViewById<ImageView>(R.id.course_detail)
        val title = findViewById<TextView>(R.id.detail_title)
        val author = findViewById<TextView>(R.id.detail_author)
        val price = findViewById<TextView>(R.id.detail_price)
        val desc = findViewById<TextView>(R.id.detail_desc)
        val btnBook = findViewById<Button>(R.id.bookButton)
        duration = findViewById(R.id.duration)

        courseId = intent.getStringExtra("COURSE_ID")

        if (courseId != null) {
            val course = dbHelper.getCourseById(courseId!!)
            if (course != null) {
                courseTitle = course.title
                tutorName = course.authors

                title.text = course.title
                author.text = course.authors
                price.text = String.format("RM %.2f / hour", course.price)
                desc.text = course.description

                if (course.imageBitmap != null) {
                    image.setImageBitmap(course.imageBitmap)
                }
            }
        }

        btnBook.setOnClickListener {
            val durationStr = duration.text.toString()
            if (durationStr.isNotEmpty() && durationStr.toInt() > 0) {
                showDatePicker()
            } else {
                Toast.makeText(this, "Please enter a valid duration (e.g., 1 or 2 hours)", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(this, { _, selectedYear, selectedMonth, selectedDay ->
            val date = "$selectedDay/${selectedMonth + 1}/$selectedYear"
            showTimePicker(date)
        }, year, month, day)

        datePicker.datePicker.minDate = System.currentTimeMillis() - 1000
        datePicker.show()
    }

    private fun showTimePicker(date: String) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePicker = TimePickerDialog(this, { _, selectedHour, selectedMinute ->
            val durationHours = duration.text.toString().toInt()
            val startTime = Calendar.getInstance()
            startTime.set(Calendar.HOUR_OF_DAY, selectedHour)
            startTime.set(Calendar.MINUTE, selectedMinute)
            val endTime = startTime.clone() as Calendar
            endTime.add(Calendar.HOUR_OF_DAY, durationHours)
            val timeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val formattedStart = timeFormat.format(startTime.time)
            val formattedEnd = timeFormat.format(endTime.time)
            val timeRange = "$formattedStart - $formattedEnd"
            saveBooking(date, timeRange, durationHours)
        }, hour, minute, false)

        timePicker.show()
    }

    private fun saveBooking(date: String, timeRange: String, duration: Int) {
        val studentName = intent.getStringExtra("STUDENT_NAME") ?: "Unknown Student"

        if (courseTitle != null && tutorName != null) {
            dbHelper.bookSession(tutorName!!, studentName, courseTitle!!, date, timeRange, duration)

            Toast.makeText(this, "Session Booked: $date ($timeRange)", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, "Error: Could not book session", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}