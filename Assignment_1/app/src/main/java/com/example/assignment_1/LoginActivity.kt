package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_1.databinding.ActivityLoginBinding
import android.widget.ImageButton

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var databaseHelper: DatabaseHelper
    private var selectedRole: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseHelper = DatabaseHelper(this)
        selectedRole = intent.getStringExtra("SELECTED_ROLE")
        val backButton = findViewById<ImageButton>(R.id.backButton)
        binding.loginButton.text = "$selectedRole Login"

        backButton.setOnClickListener {
            finish()
        }

        binding.loginButton.setOnClickListener {
            val username = binding.loginUsername.text.toString()
            val password = binding.loginPassword.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                val dbRole = databaseHelper.checkUser(username, password)

                if (dbRole != null) {
                    if (dbRole == selectedRole) {
                        Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                        goToMain(dbRole)
                    } else {
                        Toast.makeText(this, "Please log in as a $dbRole", Toast.LENGTH_LONG).show()
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
            intent.putExtra("SELECTED_ROLE", selectedRole)
            startActivity(intent)
            finish()
        }
    }

    private fun goToMain(role: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("USER_ROLE", role)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}