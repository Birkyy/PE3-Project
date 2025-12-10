package com.example.assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminCourseAdapter(
    private val courseList: List<Course>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<AdminCourseAdapter.CourseViewHolder>() {

    var onItemClick: ((Course) -> Unit)? = null

    class CourseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.admin_course_title)
        val author: TextView = view.findViewById(R.id.admin_course_author)
        val imgCourse: ImageView = view.findViewById(R.id.admin_img_course)
        val btnDelete: ImageButton = view.findViewById(R.id.deleteCourseButton)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_course, parent, false)
        return CourseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courseList[position]
        holder.title.text = course.title
        holder.author.text = "by ${course.authors}"
        if (course.imageBitmap != null) {
            holder.imgCourse.setImageBitmap(course.imageBitmap)
        }

        holder.btnDelete.setOnClickListener {
            onDeleteClick(course.id)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(course)
        }
    }

    override fun getItemCount() = courseList.size
}