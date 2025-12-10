package com.example.assignment_1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AdminUserAdapter(
    private val userList: List<User>,
    private val onDeleteClick: (String) -> Unit
) : RecyclerView.Adapter<AdminUserAdapter.UserViewHolder>() {

    var onItemClick: ((User) -> Unit)? = null

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.admin_user_name)
        val role: TextView = view.findViewById(R.id.admin_user_role)
        val btnDelete: ImageButton = view.findViewById(R.id.deleteUserButton)

        val card: View = view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_admin_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.name.text = user.name
        holder.role.text = user.role

        holder.btnDelete.setOnClickListener {
            onDeleteClick(user.id)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(user)
        }
    }

    override fun getItemCount() = userList.size
}