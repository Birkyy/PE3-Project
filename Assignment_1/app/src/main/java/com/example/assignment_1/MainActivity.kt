package com.example.assignment_1

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_fragment_container)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val role = intent.getStringExtra("USER_ROLE")

        if (savedInstanceState == null) {
            if (role == "Tutor") {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, TutorFragment())
                    .commit()
            } else if (role == "Student") {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, StudentFragment())
                    .commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_fragment_container, RoleSelectionFragment())
                    .commit()
            }
        }
    }
}