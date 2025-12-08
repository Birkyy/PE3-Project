package com.example.assignment_1

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.assignment_1.databinding.FragmentStudentBinding

class StudentFragment : Fragment(R.layout.fragment_student) {

    private var _binding: FragmentStudentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentStudentBinding.bind(view)
        replaceFragment(StudentHomeFragment())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.student_home -> replaceFragment(StudentHomeFragment())
                R.id.student_schedule -> replaceFragment(StudentScheduleFragment())
                else -> {

                }
            }

            true
        }


        val toolbar: androidx.appcompat.widget.Toolbar = view.findViewById(R.id.toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar

        actionBar?.setDisplayShowTitleEnabled(false)
        actionBar?.setDisplayHomeAsUpEnabled(true)
        actionBar?.setHomeAsUpIndicator(R.drawable.back_arrow)

        val menuHost: MenuHost = requireActivity()

        menuHost.addMenuProvider(object : MenuProvider {

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {

                    android.R.id.home -> {
                        val roleSelectionFragment = RoleSelectionFragment()
                        parentFragmentManager.beginTransaction()
                            .replace(R.id.main_fragment_container, roleSelectionFragment)
                            .addToBackStack(null)
                            .commit()
                        return true
                    }

                    R.id.action_notification -> {
                        Toast.makeText(requireContext(), "Notification Clicked", Toast.LENGTH_SHORT).show()
                        return true
                    }

                    R.id.action_contact -> {
                        val intent = Intent(requireContext(), ContactActivity::class.java)
                        startActivity(intent)
                        return true
                    }

                    R.id.action_exit -> {
                        activity?.finishAffinity()
                        return true
                    }

                }
                return false
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = parentFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        val actionBar = (requireActivity() as AppCompatActivity).supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(false)
        actionBar?.setHomeAsUpIndicator(null)
        actionBar?.setDisplayShowTitleEnabled(true)
    }
}