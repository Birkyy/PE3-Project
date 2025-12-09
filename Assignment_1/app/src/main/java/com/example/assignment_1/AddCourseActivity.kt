package com.example.assignment_1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class AddCourseActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var courseId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        dbHelper = DatabaseHelper(this)

        val etTitle = findViewById<EditText>(R.id.et_title)
        val etDesc = findViewById<EditText>(R.id.et_desc)
        val etPrice = findViewById<EditText>(R.id.et_price)
        val btnSave = findViewById<Button>(R.id.btn_save)
        val btnDelete = findViewById<Button>(R.id.btn_delete)
        val tvHeader = findViewById<TextView>(R.id.tv_header)

        if (intent.hasExtra("COURSE_ID")) {
            courseId = intent.getStringExtra("COURSE_ID")
            etTitle.setText(intent.getStringExtra("COURSE_TITLE"))
            etDesc.setText(intent.getStringExtra("COURSE_DESC"))
            etPrice.setText(intent.getDoubleExtra("COURSE_PRICE", 0.0).toString())

            tvHeader.text = "Edit Course"
            btnSave.text = "Update Course"
            btnDelete.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val desc = etDesc.text.toString()
            val priceStr = etPrice.text.toString()

            if (title.isNotEmpty() && priceStr.isNotEmpty()) {
                val price = priceStr.toDouble()

                if (courseId == null) {
                    dbHelper.insertCourse(title, desc, price)
                    Toast.makeText(this, "Course Created", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.updateCourse(courseId!!, title, desc, price)
                    Toast.makeText(this, "Course Updated", Toast.LENGTH_SHORT).show()
                }
                finish()
            } else {
                Toast.makeText(this, "Please fill required fields", Toast.LENGTH_SHORT).show()
            }
        }

        btnDelete.setOnClickListener {
            if (courseId != null) {
                dbHelper.deleteCourse(courseId!!)
                Toast.makeText(this, "Course Deleted", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}