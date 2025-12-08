package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.card.MaterialCardView

class RoleSelectionFragment : Fragment(R.layout.fragment_role_selection) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val studentButton = view.findViewById<MaterialCardView>(R.id.card_student)
        val tutorButton = view.findViewById<MaterialCardView>(R.id.card_tutor)

        studentButton.setOnClickListener {
            goToLogin("Student")
        }

        tutorButton.setOnClickListener {
            goToLogin("Tutor")
        }
    }

    private fun goToLogin(role: String) {
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.putExtra("SELECTED_ROLE", role)
        startActivity(intent)
    }
}