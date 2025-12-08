package com.example.assignment_1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class StudentScheduleFragment : Fragment() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_student_schedule, container, false)

        tabLayout = view.findViewById(R.id.scheduleTabLayout)
        viewPager = view.findViewById(R.id.scheduleViewPager)

        setupViewPagerAndTabs()

        return view
    }

    private fun setupViewPagerAndTabs() {
        val adapter = SchedulePagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Upcoming"
                1 -> "Past"
                else -> ""
            }
        }.attach()
    }

    private inner class SchedulePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 2

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> UpcomingSessionsFragment()
                1 -> PastSessionsFragment()
                else -> throw IllegalStateException("Invalid position: $position")
            }
        }
    }
}