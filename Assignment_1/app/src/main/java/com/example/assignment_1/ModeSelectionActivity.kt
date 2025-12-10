package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class ModeSelectionActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        enableEdgeToEdge()
        setContentView(R.layout.activity_mode_selection)

        val cardUser = findViewById<CardView>(R.id.card_user)
        val cardAdmin = findViewById<CardView>(R.id.card_admin)

        cardUser.setOnClickListener {
            val intent = Intent(this, RoleSelectionActivity::class.java)
            startActivity(intent)
        }

        cardAdmin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("SELECTED_ROLE", "Admin")
            startActivity(intent)
        }
    }
}