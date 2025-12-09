package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_1.databinding.ActivitySignupBinding

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var selectedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        selectedRole = intent.getStringExtra("SELECTED_ROLE")
        val backButton = findViewById<ImageButton>(R.id.backButton)
        binding.signupButton.text = "Sign Up as $selectedRole"

        backButton.setOnClickListener {
            finish()
        }

        binding.signupButton.setOnClickListener {
            val fullname = binding.signupName.text.toString()
            val username = binding.signupUsername.text.toString()
            val password = binding.signupPassword.text.toString()
            val confirmPassword = binding.signupConfirmPassword.text.toString()

            if(databaseHelper.checkUsernameExists(username)){
                Toast.makeText(this, "User already exists! Please choose another username.", Toast.LENGTH_SHORT).show()
            }
            else if (fullname.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty() && selectedRole != null) {
                val result = databaseHelper.insertUser(fullname, username, password, selectedRole!!)
                if (result != -1L) {
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("SELECTED_ROLE", selectedRole)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
                }
            }
            else if(password != confirmPassword){
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
            }
            else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.loginRedirect.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SELECTED_ROLE", selectedRole)
            startActivity(intent)
            finish()
        }
    }
}