package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.card.MaterialCardView

class RoleSelectionFragment : Fragment(R.layout.fragment_role_selection) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val studentButton = view.findViewById<MaterialCardView>(R.id.card_student)
        val tutorButton = view.findViewById<MaterialCardView>(R.id.card_tutor)

        studentButton.setOnClickListener {
            val studentFragment = StudentFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, studentFragment)
                .addToBackStack(null)
                .commit()
        }

        tutorButton.setOnClickListener {
            val tutorFragment = TutorFragment()

            parentFragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, tutorFragment)
                .addToBackStack(null)
                .commit()
        }
    }
}