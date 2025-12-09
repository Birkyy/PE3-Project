package com.example.assignment_1

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "ProjectDatabase.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_USERS = "users"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_PASSWORD = "password"
        const val COLUMN_ROLE = "role"
        const val TABLE_COURSES = "courses"
        const val COLUMN_COURSE_ID = "course_id"
        const val COLUMN_COURSE_TITLE = "title"
        const val COLUMN_COURSE_DESC = "description"
        const val COLUMN_COURSE_PRICE = "price"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createUsersTable = ("CREATE TABLE $TABLE_USERS (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_USERNAME TEXT, " +
                "$COLUMN_PASSWORD TEXT, " +
                "$COLUMN_ROLE TEXT)")
        db?.execSQL(createUsersTable)

        val createCoursesTable = ("CREATE TABLE $TABLE_COURSES (" +
                "$COLUMN_COURSE_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "$COLUMN_COURSE_TITLE TEXT, " +
                "$COLUMN_COURSE_DESC TEXT, " +
                "$COLUMN_COURSE_PRICE REAL)")
        db?.execSQL(createCoursesTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_COURSES")
        onCreate(db)
    }

    fun insertUser(username: String, password: String, role: String): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USERNAME, username)
            put(COLUMN_PASSWORD, password)
            put(COLUMN_ROLE, role)
        }
        return db.insert(TABLE_USERS, null, values)
    }

    fun checkUser(username: String, password: String): String? {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_ROLE),
            "$COLUMN_USERNAME = ? AND $COLUMN_PASSWORD = ?",
            arrayOf(username, password), null, null, null)

        var role: String? = null
        if (cursor.moveToFirst()) {
            role = cursor.getString(0)
        }
        cursor.close()
        return role
    }

    fun checkUsernameExists(username: String): Boolean {
        val db = readableDatabase
        val cursor = db.query(TABLE_USERS, arrayOf(COLUMN_USERNAME),
            "$COLUMN_USERNAME = ?",
            arrayOf(username), null, null, null)

        val exists = cursor.count > 0 // true if we found a user, false if not
        cursor.close()
        return exists
    }

    fun insertCourse(title: String, desc: String, price: Double): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COURSE_TITLE, title)
            put(COLUMN_COURSE_DESC, desc)
            put(COLUMN_COURSE_PRICE, price)
        }
        return db.insert(TABLE_COURSES, null, values)
    }

    fun getAllCourses(): List<Course> {
        val courseList = mutableListOf<Course>()
        val db = readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $TABLE_COURSES", null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_COURSE_ID)).toString()
                val title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_TITLE))
                val desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_COURSE_DESC))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_COURSE_PRICE))

                courseList.add(Course(id, title, "You", desc, price, R.mipmap.math_course))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return courseList
    }

    fun updateCourse(id: String, title: String, desc: String, price: Double): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_COURSE_TITLE, title)
            put(COLUMN_COURSE_DESC, desc)
            put(COLUMN_COURSE_PRICE, price)
        }
        return db.update(TABLE_COURSES, values, "$COLUMN_COURSE_ID = ?", arrayOf(id))
    }

    fun deleteCourse(id: String): Int {
        val db = writableDatabase
        return db.delete(TABLE_COURSES, "$COLUMN_COURSE_ID = ?", arrayOf(id))
    }
}