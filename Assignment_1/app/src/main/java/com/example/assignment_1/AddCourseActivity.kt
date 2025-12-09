package com.example.assignment_1

import android.graphics.BitmapFactory
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

        val Title = findViewById<EditText>(R.id.title)
        val Desc = findViewById<EditText>(R.id.desc)
        val Price = findViewById<EditText>(R.id.price)
        val btnSave = findViewById<Button>(R.id.saveButton)
        val btnDelete = findViewById<Button>(R.id.deleteButton)
        val Header = findViewById<TextView>(R.id.header)

        if (intent.hasExtra("COURSE_ID")) {
            courseId = intent.getStringExtra("COURSE_ID")
            Title.setText(intent.getStringExtra("COURSE_TITLE"))
            Desc.setText(intent.getStringExtra("COURSE_DESC"))
            Price.setText(intent.getDoubleExtra("COURSE_PRICE", 0.0).toString())

            Header.text = "Edit Course"
            btnSave.text = "Update Course"
            btnDelete.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            val title = Title.text.toString()
            val desc = Desc.text.toString()
            val priceStr = Price.text.toString()
            val defaultImage = ImageUtil.drawableToBitmap(this, R.drawable.placeholder_img)

            val imageBytes = ImageUtil.getBytesFromBitmap(defaultImage)

            if (title.isNotEmpty() && priceStr.isNotEmpty()) {
                val price = priceStr.toDouble()

                if (courseId == null) {
                    dbHelper.insertCourse(title, desc, price, imageBytes)
                    Toast.makeText(this, "Course Created", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.updateCourse(courseId!!, title, desc, price, imageBytes)
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