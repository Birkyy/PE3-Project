package com.example.assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class CourseAdapter(private val courses: List<Course>) :
    RecyclerView.Adapter<CourseAdapter.CourseViewHolder>() {

    var onItemClick: ((Course) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CourseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_course_card, parent, false)
        return CourseViewHolder(view)

    }

    override fun onBindViewHolder(holder: CourseViewHolder, position: Int) {
        val course = courses[position]
        holder.courseImage.setImageResource(course.imageUrl)
        holder.courseTitle.text = course.title
        holder.courseAuthors.text = course.authors

        val decimalFormat = DecimalFormat("RM#,##0.00 / hour")
        holder.coursePrice.text = decimalFormat.format(course.price)

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(course)
        }
    }

    override fun getItemCount(): Int = courses.size

    class CourseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courseImage: ImageView = itemView.findViewById(R.id.courseImage)
        val courseTitle: TextView = itemView.findViewById(R.id.courseTitle)
        val courseAuthors: TextView = itemView.findViewById(R.id.courseAuthors)
        val coursePrice: TextView = itemView.findViewById(R.id.coursePrice)
    }
}