package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
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

        binding.signupButton.text = "Sign Up as $selectedRole"

        binding.signupButton.setOnClickListener {
            val username = binding.signupUsername.text.toString()
            val password = binding.signupPassward.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty() && selectedRole != null) {
                val result = databaseHelper.insertUser(username, password, selectedRole!!)
                if (result != -1L) {
                    Toast.makeText(this, "Signup Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.putExtra("SELECTED_ROLE", selectedRole)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Signup Failed", Toast.LENGTH_SHORT).show()
                }
            } else {
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