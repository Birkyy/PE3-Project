package com.example.assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeaturedTutorAdapter(private val tutors: List<Tutor>) :
    RecyclerView.Adapter<FeaturedTutorAdapter.TutorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_tutor, parent, false)
        return TutorViewHolder(view)

    }

    override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
        val tutor = tutors[position]
        holder.tutorName.text = tutor.name
        holder.tutorSubject.text = tutor.subject
        holder.tutorImage.setImageResource(tutor.imageUrl)

    }

    override fun getItemCount(): Int = tutors.size

    class TutorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tutorImage: ImageView = itemView.findViewById(R.id.tutorImage)
        val tutorName: TextView = itemView.findViewById(R.id.tutorName)
        val tutorSubject: TextView = itemView.findViewById(R.id.tutorSubject)
    }
}