package com.example.assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FeaturedTutorAdapter(private val tutors: List<Tutor>) :
    RecyclerView.Adapter<FeaturedTutorAdapter.TutorViewHolder>() {

    var onItemClick: ((Tutor) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TutorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_featured_tutor, parent, false)
        return TutorViewHolder(view)
    }

    override fun onBindViewHolder(holder: TutorViewHolder, position: Int) {
        val tutor = tutors[position]
        holder.tutorName.text = tutor.name

        if (tutor.image != null) {
            holder.tutorImage.setImageBitmap(tutor.image)
        } else {
            holder.tutorImage.setImageResource(R.drawable.placeholder_img)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(tutor)
        }
    }

    override fun getItemCount(): Int = tutors.size

    class TutorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tutorImage: ImageView = itemView.findViewById(R.id.tutorImage)
        val tutorName: TextView = itemView.findViewById(R.id.tutorName)
    }
}