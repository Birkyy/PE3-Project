package com.example.assignment_1

data class Session(
    val id: String,
    val tutorName: String,
    val subject: String,
    val date: String,
    val time: String,
    val status: String,
    val studentName: String?,
)