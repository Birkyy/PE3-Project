package com.example.assignment_1

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText

class AddCourseActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private var courseId: String? = null
    private var currentImageBytes: ByteArray? = null
    private lateinit var CourseImage: ImageView

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)

                CourseImage.setImageBitmap(bitmap)

                currentImageBytes = ImageUtil.getBytesFromBitmap(bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        dbHelper = DatabaseHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)
        supportActionBar?.title = "Add Course"

        // Find Views
        CourseImage = findViewById(R.id.course_image)
        val etTitle = findViewById<TextInputEditText>(R.id.title)
        val etDesc = findViewById<TextInputEditText>(R.id.desc)
        val etPrice = findViewById<TextInputEditText>(R.id.price)
        val btnSave = findViewById<Button>(R.id.saveButton)
        val btnDelete = findViewById<Button>(R.id.deleteButton)

        if (intent.hasExtra("COURSE_ID")) {
            courseId = intent.getStringExtra("COURSE_ID")
            supportActionBar?.title = "Edit Course"
            btnSave.text = "Update Course"
            btnDelete.visibility = View.VISIBLE

            val course = dbHelper.getCourseById(courseId!!)
            if (course != null) {
                etTitle.setText(course.title)
                etDesc.setText(course.description)
                etPrice.setText(course.price.toString())

                if (course.imageBitmap != null) {
                    CourseImage.setImageBitmap(course.imageBitmap)
                    currentImageBytes = ImageUtil.getBytesFromBitmap(course.imageBitmap)
                }
            }
        } else {
            val defaultBitmap = ImageUtil.drawableToBitmap(this, R.drawable.placeholder_img)
            currentImageBytes = ImageUtil.getBytesFromBitmap(defaultBitmap)
        }

        CourseImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val desc = etDesc.text.toString()
            val priceStr = etPrice.text.toString()

            val author = intent.getStringExtra("AUTHOR_NAME_PASSED") ?: "Unknown Tutor"

            if (title.isNotEmpty() && priceStr.isNotEmpty() && currentImageBytes != null) {
                val price = priceStr.toDoubleOrNull() ?: 0.0

                if (courseId == null) {
                    dbHelper.insertCourse(title, author, desc, price, currentImageBytes!!)
                    Toast.makeText(this, "Course Created", Toast.LENGTH_SHORT).show()
                } else {
                    dbHelper.updateCourse(courseId!!, title, desc, price, currentImageBytes!!)
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

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}