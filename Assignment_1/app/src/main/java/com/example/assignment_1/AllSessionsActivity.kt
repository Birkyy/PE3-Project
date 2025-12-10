package com.example.assignment_1

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Locale

class AllSessionsActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmpty: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_sessions)

        dbHelper = DatabaseHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)

        recyclerView = findViewById(R.id.recyclerViewAllSessions)
        tvEmpty = findViewById(R.id.tv_empty)
    }

    override fun onResume() {
        super.onResume()
        loadAllSessions()
    }

    private fun loadAllSessions() {
        val currentTutor = intent.getStringExtra("TUTOR_NAME") ?: "Unknown"

        var sessions = dbHelper.getTutorSessions(currentTutor, isUpcoming = true)

        val dateFormat = SimpleDateFormat("d/M/yyyy", Locale.getDefault())

        sessions = sessions.sortedBy { session ->
            try {
                dateFormat.parse(session.date)?.time ?: 0L
            } catch (e: Exception) {
                Long.MAX_VALUE
            }
        }

        if (sessions.isEmpty()) {
            recyclerView.visibility = View.GONE
            tvEmpty.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvEmpty.visibility = View.GONE

            val adapter = SessionAdapter(sessions, UserType.TUTOR)

            adapter.onItemClick = { session ->
                showSessionActionDialog(session)
            }

            recyclerView.layoutManager = LinearLayoutManager(this)
            recyclerView.adapter = adapter
        }
    }

    private fun showSessionActionDialog(session: Session) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Manage Session")
        builder.setMessage("Student: ${session.studentName}\nSubject: ${session.subject}\nDate: ${session.date}")

        builder.setPositiveButton("Confirm") { _, _ ->
            dbHelper.updateSessionStatus(session.id, "Confirmed")
            Toast.makeText(this, "Session Confirmed", Toast.LENGTH_SHORT).show()
            loadAllSessions()
        }

        builder.setNegativeButton("Reject") { _, _ ->
            dbHelper.updateSessionStatus(session.id, "Cancelled")
            Toast.makeText(this, "Session Rejected", Toast.LENGTH_SHORT).show()
            loadAllSessions()
        }

        builder.setNeutralButton("Back", null)
        builder.show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}