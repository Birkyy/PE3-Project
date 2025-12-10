package com.example.assignment_1

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.textfield.TextInputEditText

class ProfileActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var profile: ImageView
    private lateinit var profile_name: TextInputEditText
    private lateinit var profile_username: TextInputEditText
    private lateinit var profile_pass: TextInputEditText
    private lateinit var btnSave: Button
    private var currentImageBytes: ByteArray? = null
    private var currentUsername: String = ""

    private val pickImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val imageUri: Uri? = result.data?.data
            if (imageUri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver, imageUri)
                profile.setImageBitmap(bitmap)
                currentImageBytes = ImageUtil.getBytesFromBitmap(bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        dbHelper = DatabaseHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)

        profile = findViewById(R.id.iv_profile_image)
        profile_name = findViewById(R.id.profile_fullname)
        profile_username = findViewById(R.id.profile_username)
        profile_pass = findViewById(R.id.profile_password)
        btnSave = findViewById<Button>(R.id.btn_save_profile)

        currentUsername = intent.getStringExtra("USER_USERNAME") ?: ""

        profile_username.setText(currentUsername)

        loadUserData()

        profile.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            pickImageLauncher.launch(intent)
        }

        btnSave.setOnClickListener {
            val newName = profile_name.text.toString()
            val newPass = profile_pass.text.toString()

            if (newName.isNotEmpty() && newPass.isNotEmpty()) {
                val success = dbHelper.updateUserProfile(currentUsername, newName, newPass, currentImageBytes)
                if (success) {
                    Toast.makeText(this, "Profile Updated Successfully!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Update Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loadUserData() {
        if (currentUsername.isNotEmpty()) {
            val details = dbHelper.getUserDetails(currentUsername)
            if (details != null) {
                val (name, pass, bitmap) = details
                profile_name.setText(name)
                profile_pass.setText(pass)

                if (bitmap != null) {
                    profile.setImageBitmap(bitmap)
                    currentImageBytes = ImageUtil.getBytesFromBitmap(bitmap)
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}