package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_1.databinding.ActivityLoginBinding
import android.widget.ImageButton

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var dbHelper: DatabaseHelper
    private var intendedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dbHelper = DatabaseHelper(this)
        val backButton = findViewById<ImageButton>(R.id.backButton)

        backButton.setOnClickListener {
            finish()
        }

        intendedRole = intent.getStringExtra("SELECTED_ROLE")

        binding.loginButton.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()
            val fullName = dbHelper.getUserFullName(username)

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val realRole = dbHelper.checkUser(username, password)

                if (realRole != null) {
                    if (realRole == intendedRole) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        goToMain(realRole, fullName, username)
                    } else {
                        Toast.makeText(this, "Please log in as a $realRole", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }

        binding.signupRedirect.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            intent.putExtra("SELECTED_ROLE", intendedRole)
            startActivity(intent)
            finish()
        }
    }

    private fun goToMain(role: String, name:String, username: String) {

        if (intendedRole == "Admin") {
            val intent = Intent(this, AdminDashboardActivity::class.java)
            startActivity(intent)
        } else {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("SELECTED_ROLE", role)
            intent.putExtra("USER_FULLNAME", name)
            intent.putExtra("USER_USERNAME", username)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}