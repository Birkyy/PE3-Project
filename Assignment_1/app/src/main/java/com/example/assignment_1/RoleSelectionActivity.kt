package com.example.assignment_1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class RoleSelectionActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_role_selection)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, RoleSelectionFragment())
                .commit()
        }
    }
}