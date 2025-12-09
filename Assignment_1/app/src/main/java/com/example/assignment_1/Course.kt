package com.example.assignment_1

import android.graphics.Bitmap

data class Course(
    val id: String,
    val title: String,
    val authors: String,
    val description: String,
    val price: Double,
    val imageBitmap: Bitmap? = null
)
